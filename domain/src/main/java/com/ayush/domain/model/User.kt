package com.ayush.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val token: String,
    val created: String,
    val updated: String
)