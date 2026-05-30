package com.xclone.feature_settings.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.domain.model.WorkProfile
import com.xclone.feature_settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel =
        hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Work Profile",
            style =
                MaterialTheme.typography.titleMedium
        )

        WorkProfile.entries.forEach { profile ->

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(profile.name)

                RadioButton(
                    selected =
                        uiState.selectedProfile
                                == profile,
                    onClick = {

                        viewModel.updateProfile(
                            profile
                        )
                    }
                )
            }
        }

        HorizontalDivider()

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("Dark Mode")

            Switch(
                checked =
                    uiState.darkModeEnabled,
                onCheckedChange = {

                    viewModel.updateDarkMode(it)
                }
            )
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("Auto Save")

            Switch(
                checked =
                    uiState.autoSaveEnabled,
                onCheckedChange = {

                    viewModel.updateAutoSave(it)
                }
            )
        }
    }
}