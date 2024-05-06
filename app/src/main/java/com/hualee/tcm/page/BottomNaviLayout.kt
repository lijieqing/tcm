package com.hualee.tcm.page

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hualee.tcm.R
import com.hualee.tcm.ui.theme.HeYeGreen
import com.hualee.tcm.ui.theme.YueYingWhite

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
) {
    object RouteHome : Screen("home", R.string.home)
    object RouteSettings : Screen("settings", R.string.settings)
}

@Preview
@Composable
fun BottomNaviTabLayout() {
    val items = remember {
        listOf(
            Screen.RouteHome,
            Screen.RouteSettings,
        )
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = YueYingWhite,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            when (screen) {
                                Screen.RouteHome -> {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = null,
                                    )
                                }

                                Screen.RouteSettings -> {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = null,
                                    )
                                }
                            }
                        },
                        label = {
                            Box(modifier = Modifier.wrapContentHeight()) {
                                Text(
                                    text = stringResource(screen.resourceId),
                                    color = if (isSelected) HeYeGreen else HeYeGreen.copy(alpha = 0.4F),
                                )
                            }
                        },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.RouteHome.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.RouteHome.route) { HomeLayout() }
            composable(Screen.RouteSettings.route) { }
        }
    }
}