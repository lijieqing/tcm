package com.hualee.tcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            delay(1000)
            initState = 2
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
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val controller = rememberNavController()
                    val viewModel = viewModel<DataInitViewModel>()
                    LaunchedEffect(viewModel.initState) {
                        if (viewModel.initState == 2) {
                            controller.navigate("home")
                        }
                    }
                    NavHost(
                        navController = controller,
                        startDestination = "data_init",
                    ) {
                        composable("data_init") {
                            DataInitLayout(viewModel.initState)
                        }
                        composable("home") {
                            HomeLayout()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {}
}

@Composable
private fun DataInitLayout(
    initState: Int
) {
    when (initState) {
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
private fun HomeLayout() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "首页",
            modifier = Modifier.align(Alignment.Center),
        )
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