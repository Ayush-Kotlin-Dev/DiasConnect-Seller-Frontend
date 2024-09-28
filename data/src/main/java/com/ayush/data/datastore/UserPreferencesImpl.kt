package com.ayush.data.datastore

import androidx.datastore.core.DataStore
import com.ayush.data.datastore.UserPreferences
import com.ayush.data.datastore.UserSettings
import kotlinx.coroutines.flow.first

class UserPreferencesImpl(
    private val dataStore: DataStore<UserSettings>
): UserPreferences {
    override suspend fun getUserData(): UserSettings {
        return dataStore.data.first()
    }

    override suspend fun setUserData(userSettings: UserSettings) {
        dataStore.updateData { userSettings }
    }
}