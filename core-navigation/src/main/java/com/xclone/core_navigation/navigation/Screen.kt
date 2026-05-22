package com.xclone.core_navigation.navigation

sealed class Screen(val route: String) {
    object Scan : Screen("scan")
    object History : Screen("history")
    object Settings : Screen("settings")
}