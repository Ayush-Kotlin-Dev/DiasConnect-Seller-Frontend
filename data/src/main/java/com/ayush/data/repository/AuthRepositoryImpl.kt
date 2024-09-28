package com.ayush.data.repository

import com.ayush.data.datastore.UserPreferences
import com.ayush.data.datastore.UserSettings
import com.ayush.data.datastore.toUser
import com.ayush.data.datastore.toUserSettings
import com.ayush.data.model.AuthResponseData
import com.ayush.domain.model.User
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val networkService: NetworkService,
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun login(email: String, password: String): ResultWrapper<User> {
        val result = networkService.login(email, password)
        if (result is ResultWrapper.Success) {
            val userSettings = AuthResponseData(
                id = result.value.id,
                name = result.value.name,
                email = result.value.email,
                token = result.value.token,
                created = result.value.created,
                updated = result.value.updated
            ).toUserSettings()
            userPreferences.setUserData(userSettings)
        }
        return result
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): ResultWrapper<User> {
        val result = networkService.signup(name, email, password)
        if (result is ResultWrapper.Success) {
            val userSettings = AuthResponseData(
                id = result.value.id,
                name = result.value.name,
                email = result.value.email,
                token = result.value.token,
                created = result.value.created,
                updated = result.value.updated
            ).toUserSettings()
            userPreferences.setUserData(userSettings)
        }
        return result
    }

    override suspend fun getCurrentUser(): User? {
        val userSettings = userPreferences.getUserData()
        return if (userSettings.id != -1L) userSettings.toUser() else null
    }

    override suspend fun logout() {
        userPreferences.setUserData(UserSettings())
    }
}