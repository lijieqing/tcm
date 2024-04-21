package com.hualee.tcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hualee.tcm.ui.theme.TcmTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DataInitViewModel : ViewModel() {
    var initState by mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            initState = 0
            delay(3000)
            initState = 1
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TcmTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataInitLayout()
                }
            }
        }
    }
}

@Composable
fun DataInitLayout() {
    val viewModel = viewModel<DataInitViewModel>()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (viewModel.initState) {
            0 -> {
                DataInitLoading(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            1 -> {
                DataInitFinished(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataInitLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(200.dp)
            .wrapContentHeight()
            .background(color = Color.White.copy(alpha = 0.6F)),
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

@Preview(showBackground = true)
@Composable
private fun DataInitFinished(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(200.dp)
            .wrapContentHeight()
            .background(color = Color.White.copy(alpha = 0.6F)),
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