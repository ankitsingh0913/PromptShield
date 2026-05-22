package com.xclone.core_ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xclone.core_navigation.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        Screen.Scan,
        Screen.History,
        Screen.Settings
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route)
                },
                label = {
                    Text(screen.route.replaceFirstChar { it.uppercase() })
                },
                icon = {}
            )
        }
    }
}
