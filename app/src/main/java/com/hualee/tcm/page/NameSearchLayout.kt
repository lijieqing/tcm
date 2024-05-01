package com.hualee.tcm.page

import android.util.Log
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


class NameSearchViewModel : ViewModel() {
    var herbName by mutableStateOf("")
        private set

    fun updateHerbName(name: String) {
        herbName = name
    }

    fun getPagingData(): PagingSource<Int, HerbEntity> {
        return DBUtils.queryHerbByNameLikeWithPaging(herbName)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NameSearchLayout() {
    val viewModel = viewModel<NameSearchViewModel>()
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = viewModel.herbName,
            onValueChange = {
                viewModel.updateHerbName(it)
                lazyPagingItems.refresh()
            },
            label = {
                Text(text = "输入药材名称进行检索")
            },
        )
        HerbsList(
            lazyPagingItems = lazyPagingItems,
        )
    }


}
