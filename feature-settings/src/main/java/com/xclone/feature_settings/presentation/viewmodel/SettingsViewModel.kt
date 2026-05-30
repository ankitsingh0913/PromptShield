package com.xclone.feature_settings.presentation.viewmodel

import androidx.lifecycle.*
import com.xclone.domain.model.WorkProfile
import com.xclone.domain.repository.SettingsRepository
import com.xclone.feature_settings.presentation.state.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState()
    )

    val uiState = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        combine(
            repository.selectedProfile,
            repository.darkModeEnabled,
            repository.autoSaveEnabled
        ) {
            profile,
            darkMode,
            autoSave ->
            SettingsUiState (
                selectedProfile = profile,
                darkModeEnabled = darkMode,
                autoSaveEnabled = autoSave
            )
        }.onEach {
            _uiState.value = it
        }.launchIn(viewModelScope)
    }

    fun updateProfile(profile: WorkProfile) {
        viewModelScope.launch {
            repository.setWorkProfile(profile)
        }
    }

    fun updateDarkMode(enable: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enable)
        }
    }

    fun updateAutoSave(enable: Boolean) {
        viewModelScope.launch {
            repository.setAutoSave(enable)
        }
    }
}