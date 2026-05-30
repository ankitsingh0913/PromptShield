package com.xclone.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xclone.domain.model.WorkProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by
preferencesDataStore(
    name = "promptshield_settings"
)

class SettingsDataStore
@Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    companion object {

        private val WORK_PROFILE =
            stringPreferencesKey(
                "work_profile"
            )

        private val DARK_MODE =
            booleanPreferencesKey(
                "dark_mode"
            )

        private val AUTO_SAVE =
            booleanPreferencesKey(
                "auto_save"
            )
    }

    val selectedProfile: Flow<WorkProfile> =
        context.dataStore.data.map { preferences ->

            val profileName =
                preferences[WORK_PROFILE]
                    ?: WorkProfile.DEVELOPER.name

            WorkProfile.valueOf(profileName)
        }

    val darkModeEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[DARK_MODE] ?: false
        }

    val autoSaveEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[AUTO_SAVE] ?: true
        }

    suspend fun setWorkProfile(
        profile: WorkProfile
    ) {
        context.dataStore.edit {
            it[WORK_PROFILE] =
                profile.name
        }
    }

    suspend fun setDarkMode(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[DARK_MODE] = enabled
        }
    }

    suspend fun setAutoSave(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[AUTO_SAVE] = enabled
        }
    }
}