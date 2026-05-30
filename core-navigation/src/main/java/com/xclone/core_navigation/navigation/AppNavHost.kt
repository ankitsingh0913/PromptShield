package com.xclone.core_navigation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xclone.feature_history.presentation.screen.HistoryScreen
import com.xclone.feature_settings.presentation.screen.SettingsScreen
import com.xclone.feature_scan.presentation.screen.ScanScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    sharedText: String?
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Scan.route
    ) {

        composable(Screen.Scan.route) {
            ScanScreen(sharedText)
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}