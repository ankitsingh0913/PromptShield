package com.xclone.core_navigation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xclone.feature_history.presentation.screen.HistoryDetailScreen
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
            HistoryScreen(
                onPromptClick = { historyId ->
                    navController.navigate(
                        Screen.HistoryDetail.createRoute(historyId)
                    )
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.HistoryDetail.route,
            arguments = listOf(
                navArgument(Screen.HistoryDetail.ARG_HISTORY_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            HistoryDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}