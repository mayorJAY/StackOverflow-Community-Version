package com.josycom.mayorjay.flowoverstack.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AppConstants.PREFERENCES_FILE_NAME
)

class PreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceRepository {

    override fun getIntPreferenceFlow(key: String): Flow<Int?> {
        val prefKey: Preferences.Key<Int> = intPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                Timber.e(exception)
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[prefKey] ?: 0
            }
    }

    override suspend fun setIntPreference(
        key: String,
        value: Int
    ) {
        val prefKey: Preferences.Key<Int> = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override suspend fun deleteAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun contains(key: String): Boolean {
        return dataStore.edit { }.contains(intPreferencesKey(key))
    }

    override suspend fun isEmpty(): Boolean {
        return dataStore.edit { }.asMap().isEmpty()
    }
}