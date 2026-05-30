package com.xclone.domain.repository

import com.xclone.domain.model.WorkProfile
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val selectedProfile:
            Flow<WorkProfile>

    val darkModeEnabled:
            Flow<Boolean>

    val autoSaveEnabled:
            Flow<Boolean>

    suspend fun setWorkProfile(
        profile: WorkProfile
    )

    suspend fun setDarkMode(
        enabled: Boolean
    )

    suspend fun setAutoSave(
        enabled: Boolean
    )
}