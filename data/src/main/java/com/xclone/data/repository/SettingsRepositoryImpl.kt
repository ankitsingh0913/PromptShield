package com.xclone.data.repository

import com.xclone.data.preferences.SettingsDataStore
import com.xclone.domain.model.WorkProfile
import com.xclone.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl
@Inject constructor(
    private val dataStore:
    SettingsDataStore
) : SettingsRepository {

    override val selectedProfile:
            Flow<WorkProfile> =
        dataStore.selectedProfile

    override val darkModeEnabled:
            Flow<Boolean> =
        dataStore.darkModeEnabled

    override val autoSaveEnabled:
            Flow<Boolean> =
        dataStore.autoSaveEnabled

    override suspend fun setWorkProfile(
        profile: WorkProfile
    ) {

        dataStore.setWorkProfile(profile)
    }

    override suspend fun setDarkMode(
        enabled: Boolean
    ) {

        dataStore.setDarkMode(enabled)
    }

    override suspend fun setAutoSave(
        enabled: Boolean
    ) {

        dataStore.setAutoSave(enabled)
    }
}