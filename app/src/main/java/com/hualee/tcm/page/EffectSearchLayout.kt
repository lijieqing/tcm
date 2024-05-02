package com.hualee.tcm.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import com.hualee.tcm.App
import com.hualee.tcm.db.DBUtils
import com.hualee.tcm.db.HerbEntity
import com.hualee.tcm.ui.theme.YueYingWhite


class EffectSearchViewModel : ViewModel() {
    var herbEffect by mutableStateOf("")
        private set

    fun updateHerbEffect(name: String) {
        herbEffect = name
    }

    fun getPagingData(): PagingSource<Int, HerbEntity> {
        return DBUtils.queryHerbByEffectLikeWithPaging(herbEffect)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EffectSearchLayout() {
    val viewModel = viewModel<EffectSearchViewModel>()
    val pager = remember {
        Pager(
            config = PagingConfig(pageSize = 18),
            pagingSourceFactory = {
                Log.d(App.TAG, "paging loading herbs")
                viewModel.getPagingData()
            }
        )
    }
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = YueYingWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HerbsList(
            modifier = Modifier.weight(1F),
            lazyPagingItems = lazyPagingItems,
        )
        OutlinedTextField(
            value = viewModel.herbEffect,
            onValueChange = {
                viewModel.updateHerbEffect(it)
                lazyPagingItems.refresh()
            },
            label = {
                Text(text = "输入药材功效进行检索")
            },
        )
    }

}
