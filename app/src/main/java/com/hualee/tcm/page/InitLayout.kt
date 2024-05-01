package com.hualee.tcm.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hualee.tcm.App
import com.hualee.tcm.R
import com.hualee.tcm.db.DBUtils
import com.hualee.tcm.global.AppSettings
import com.hualee.tcm.global.HerbJsonParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DataInitViewModel : ViewModel() {
    var initState by mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            initState = 0
            // 检查版本
            val verJson =
                AppSettings.appContext.assets.open("version.json").bufferedReader().readText()
            val jsonVer = HerbJsonParser.parseVersion(verJson)
            val dbVer = DBUtils.queryVer().firstOrNull()
            Log.d(App.TAG, "jsonVer=$jsonVer dbVer=${dbVer?.version}")
            if (jsonVer > 0 && jsonVer != dbVer?.version) {
                // 更新 DB 数据
                val herbJson =
                    AppSettings.appContext.assets.open("data.json").bufferedReader().readText()
                val jsonList = HerbJsonParser.parseToHerbEntity(herbJson)
                jsonList.forEach { jsonEntity ->
                    val dbList = DBUtils.queryHerbByName(jsonEntity.name)
                    if (dbList.isEmpty()) {
                        DBUtils.insertHerb(jsonEntity)
                    } else {
                        dbList.forEach { dbEntity ->
                            val newData = dbEntity.copy(
                                name = jsonEntity.name,
                                type = jsonEntity.type,
                                subType = jsonEntity.subType,
                                source = jsonEntity.source,
                                imageList = jsonEntity.imageList,
                                property = jsonEntity.property,
                                effect = jsonEntity.effect,
                                indications = jsonEntity.indications,
                                combination = jsonEntity.combination,
                                feature = jsonEntity.feature,
                            )
                            DBUtils.updateHerb(newData)
                        }
                    }
                }
                if (dbVer == null) {
                    DBUtils.insertVer(jsonVer)
                } else {
                    DBUtils.updateVer(dbVer.copy(version = jsonVer))
                }
            }
            delay(1000)
            initState = 1
            delay(1000)
            initState = 2
        }
    }
}

@Composable
fun DataInitLayout(
    onInitFinished: () -> Unit,
) {
    val viewModel = viewModel<DataInitViewModel>()
    LaunchedEffect(viewModel.initState) {
        if (viewModel.initState == 2) {
            onInitFinished()
        }
    }
    when (viewModel.initState) {
        0 -> {
            DataInitLoading()
        }

        1 -> {
            DataInitFinished()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataInitLoading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(200.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "数据加载中，请稍后",
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataInitFinished() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(200.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.data_init_success),
                contentDescription = "data init success",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.FillBounds,
            )
            Text(
                text = "数据加载完成",
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}