package com.ayush.domain.repository

import com.ayush.domain.model.User
import com.ayush.domain.network.ResultWrapper

interface AuthRepository {
    suspend fun login(email: String, password: String): ResultWrapper<User>
    suspend fun signup(name: String, email: String, password: String): ResultWrapper<User>
    suspend fun getCurrentUser(): User?
    suspend fun logout()
}