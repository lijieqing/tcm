package com.hualee.tcm.page

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import com.hualee.tcm.App
import com.hualee.tcm.db.DBUtils
import com.hualee.tcm.db.HerbEntity

class SearchViewModel : ViewModel() {

    val searchModeList = mutableStateListOf(
        "药材名称",
        "性能特点",
        "药材功效",
    )

    var searchMode by mutableIntStateOf(0)
        private set

    fun updateSearchMode(mode: Int) {
        searchMode = mode
    }


    var searchValue by mutableStateOf("")
        private set

    fun updateSearchValue(value: String) {
        searchValue = value
    }

    var isSearchActive by mutableStateOf(false)
        private set

    fun updateSearchActive(active: Boolean) {
        isSearchActive = active
    }

    private fun getHerbNamePagingData(): PagingSource<Int, HerbEntity> {
        return DBUtils.queryHerbByNameLikeWithPaging(searchValue)
    }

    private fun getHerbEffectPagingData(): PagingSource<Int, HerbEntity> {
        return DBUtils.queryHerbByEffectLikeWithPaging(searchValue)
    }

    private fun getHerbFeaturePagingData(): PagingSource<Int, HerbEntity> {
        return DBUtils.queryHerbByFeatureLikeWithPaging(searchValue)
    }


    fun getPagingData(): PagingSource<Int, HerbEntity> {
        return when (searchMode) {
            0 -> getHerbNamePagingData()
            1 -> getHerbFeaturePagingData()
            else -> getHerbEffectPagingData()
        }
    }

}

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeLayout() {

    val viewModel = viewModel<SearchViewModel>()

    val pager = remember {
        Pager(
            config = PagingConfig(pageSize = 4),
            pagingSourceFactory = {
                Log.d(App.TAG, "paging loading herbs")
                viewModel.getPagingData()
            }
        )
    }
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        FlowRow(
            modifier = Modifier
                .padding(top = 18.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 30.dp),
        ) {
            Text(
                text = "检索类别：",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            repeat(viewModel.searchModeList.size) {
                FilterChip(
                    modifier = Modifier.padding(start = 10.dp),
                    selected = viewModel.searchMode == it,
                    onClick = {
                        viewModel.updateSearchMode(it)
                        lazyPagingItems.refresh()
                    },
                    label = {
                        Text(text = viewModel.searchModeList[it])
                    },
                    leadingIcon = {
                        if (viewModel.searchMode == it) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    },
                )
            }
        }

        SearchBar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            query = viewModel.searchValue,
            onQueryChange = {
                viewModel.updateSearchValue(it)
                lazyPagingItems.refresh()
            },
            onSearch = {
                viewModel.updateSearchValue(it)
                lazyPagingItems.refresh()
            },
            active = viewModel.isSearchActive,
            onActiveChange = {
                viewModel.updateSearchActive(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                if (viewModel.isSearchActive) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            if (TextUtils.isEmpty(viewModel.searchValue)) {
                                viewModel.updateSearchActive(false)
                            } else {
                                viewModel.updateSearchValue("")
                                lazyPagingItems.refresh()
                            }
                        },
                    )
                }
            },
            content = {
                HerbNameList(lazyPagingItems = lazyPagingItems)
            },
        )
    }
}