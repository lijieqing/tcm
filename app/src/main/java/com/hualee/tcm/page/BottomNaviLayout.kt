package com.hualee.tcm.page

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    object NameSearch : Screen("name_search", R.string.name_search)
    object EffectSearch : Screen("effect_search", R.string.effect_search)
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNaviTabLayout() {
    val items = remember {
        listOf(
            Screen.NameSearch,
            Screen.EffectSearch,
        )
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = YueYingWhite,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    BottomNavigationItem(
                        selectedContentColor = HeYeGreen,
                        unselectedContentColor = HeYeGreen.copy(alpha = 0.4F),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                modifier = Modifier.padding(vertical = 5.dp),
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.resourceId),
                                color = if (isSelected) HeYeGreen else HeYeGreen.copy(alpha = 0.4F),
                            )
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
            startDestination = Screen.NameSearch.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.NameSearch.route) { NameSearchLayout() }
            composable(Screen.EffectSearch.route) { EffectSearchLayout() }
        }
    }
}