package com.hualee.tcm.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.hualee.tcm.db.HerbEntity
import com.hualee.tcm.ui.theme.Round16
import com.hualee.tcm.ui.theme.XuanQing

@Composable
fun HerbNameList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<HerbEntity>,
) {
    LazyColumn(
        modifier = modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(count = lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            item?.let {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 10.dp, horizontal = 15.dp),
                    text = it.name,
                )
            }
        }
    }
}


@Composable
fun HerbsList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<HerbEntity>,
) {
    LazyColumn(
        modifier = modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        items(count = lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            HerbItem(item)
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HerbItem(
    herbEntity: HerbEntity? = null
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White.copy(alpha = 0.4F), shape = Round16)
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${herbEntity?.name}",
                color = XuanQing,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${herbEntity?.subType}",
                color = XuanQing,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "功效：",
                color = XuanQing,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${herbEntity?.effect}",
                color = XuanQing,
                fontSize = 20.sp,
            )
        }


        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "性味归经：",
                color = XuanQing,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${herbEntity?.property}",
                color = XuanQing,
                fontSize = 20.sp,
            )
        }


        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "来源：",
                color = XuanQing,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${herbEntity?.source}",
                color = XuanQing,
                fontSize = 20.sp,
            )
        }

        val indicationList = remember {
            herbEntity?.indicationList() ?: emptyList()
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(
                text = "主治",
                color = XuanQing,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            repeat(indicationList.size) {
                Text(
                    text = indicationList[it],
                    color = XuanQing,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                )
            }
        }

    }
}