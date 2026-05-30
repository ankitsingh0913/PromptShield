package com.xclone.feature_settings.presentation.state

import com.xclone.domain.model.WorkProfile

data class SettingsUiState(

    val selectedProfile:
    WorkProfile =
        WorkProfile.DEVELOPER,

    val darkModeEnabled:
    Boolean = false,

    val autoSaveEnabled:
    Boolean = true
)