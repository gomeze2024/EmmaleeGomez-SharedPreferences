package com.example.emmaleegomez_sharedpreference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val storedImageId: Flow<Int> = dataStore.data.map {
        it[IMAGE_ID_KEY] ?: R.drawable.cero
    }.distinctUntilChanged()

    val storedImageTitle: Flow<String> = dataStore.data.map {
        it[IMAGE_TITLE_KEY] ?: ""
    }.distinctUntilChanged()

    suspend fun setStoredImageId(imageId: Int) {
        dataStore.edit {
            it[IMAGE_ID_KEY] = imageId
        }
    }

    suspend fun setStoredImageTitle(title: String) {
        dataStore.edit {
            it[IMAGE_TITLE_KEY] = title
        }
    }

    companion object {
        private val IMAGE_ID_KEY = intPreferencesKey("image_id")
        private val IMAGE_TITLE_KEY = stringPreferencesKey("image_title")
        private var INSTANCE: PreferencesRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("settings")
                }

                INSTANCE = PreferencesRepository(dataStore)
            }
        }

        fun get(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository must be initialized"
            )
        }
    }
}