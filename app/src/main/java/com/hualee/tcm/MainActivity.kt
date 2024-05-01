package com.hualee.tcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hualee.tcm.page.DataInitLayout
import com.hualee.tcm.page.HomeLayout
import com.hualee.tcm.ui.theme.TcmTheme


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
                    NavHost(
                        navController = controller,
                        startDestination = "data_init",
                    ) {
                        composable("data_init") {
                            DataInitLayout(
                                onInitFinished = { controller.navigate("home") },
                            )
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
