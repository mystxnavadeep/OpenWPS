package com.openwps.core.filesystem.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "file_manager_prefs")

class FileManagerPreferences(private val context: Context) {
    
    companion object {
        val SORT_MODE_KEY = stringPreferencesKey("sort_mode")
        val VIEW_MODE_KEY = stringPreferencesKey("view_mode")
    }

    val sortMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SORT_MODE_KEY] ?: "NAME_ASC"
        }

    val viewMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[VIEW_MODE_KEY] ?: "LIST"
        }

    suspend fun setSortMode(sortMode: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_MODE_KEY] = sortMode
        }
    }

    suspend fun setViewMode(viewMode: String) {
        context.dataStore.edit { preferences ->
            preferences[VIEW_MODE_KEY] = viewMode
        }
    }
}
