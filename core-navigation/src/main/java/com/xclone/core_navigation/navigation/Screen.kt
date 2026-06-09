package com.xclone.core_navigation.navigation

sealed class Screen(val route: String) {
    object Scan : Screen("scan")
    object History : Screen("history")
    object Settings : Screen("settings")
    object HistoryDetail : Screen("history_detail/{historyId}") {
        const val ARG_HISTORY_ID = "historyId"

        fun createRoute(historyId: Long): String {
            return "history_detail/$historyId"
        }
    }
}