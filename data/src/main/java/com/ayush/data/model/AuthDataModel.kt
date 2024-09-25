package com.ayush.data.model


import com.ayush.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null
)

@Serializable
data class AuthResponseData(
    val id: Long,
    val name: String,
    val email: String,
    val token: String,
    val created: String,
    val updated: String
)

@Serializable
data class AuthResponse(
    val data: AuthResponseData?,
    val errorMessage: String?
)
fun AuthResponseData.toUser() = User(
    id = id,
    name = name,
    email = email,
    token = token,
    created = created,
    updated = updated
)