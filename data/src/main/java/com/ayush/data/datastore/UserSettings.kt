package com.ayush.data.datastore

import com.ayush.data.model.AuthResponseData
import com.ayush.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val id: Long = -1,
    val name: String = "",
    val email: String = "",
    val token: String = "",
    val created: String = "",
    val updated: String = ""
)

fun UserSettings.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email,
        token = token,
        created = created,
        updated = updated
    )
}

fun AuthResponseData.toUserSettings(): UserSettings {
    return UserSettings(
        id = id,
        name = name,
        email = email,
        token = token,
        created = created,
        updated = updated
    )
}