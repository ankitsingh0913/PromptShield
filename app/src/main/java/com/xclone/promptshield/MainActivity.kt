package com.xclone.promptshield

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xclone.core_navigation.navigation.AppNavHost
import com.xclone.core_navigation.navigation.Screen
import com.xclone.core_ui.components.BottomNavBar
import com.xclone.feature_settings.presentation.viewmodel.SettingsViewModel
import com.xclone.promptshield.ui.theme.PromptShieldTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent?.getStringExtra(
            Intent.EXTRA_TEXT
        )

        setContent {

            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            PromptShieldTheme(
                darkTheme = settingsState.darkModeEnabled
            ) {

                val navController = rememberNavController()

                val backStackEntry by
                navController.currentBackStackEntryAsState()

                val currentRoute =
                    backStackEntry?.destination?.route

                val topLevelRoutes = setOf(
                    Screen.Scan.route,
                    Screen.History.route,
                    Screen.Settings.route
                )

                val showBottomBar =
                    currentRoute in topLevelRoutes

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->

                    Box(
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        AppNavHost(
                            navController = navController,
                            sharedText = sharedText
                        )
                    }
                }
            }
        }
    }
}