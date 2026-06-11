package com.xclone.core_ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xclone.core_navigation.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem(Screen.Scan, "Scan", Icons.Default.Scanner),
        BottomNavItem(Screen.History, "History", Icons.Default.History),
        BottomNavItem(Screen.Settings, "Settings", Icons.Default.Settings)
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Scan.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = { Text(item.label) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.label} Icon"
                    )
                }
            )
        }
    }
}
