package com.ayush.domain.network

import com.ayush.domain.model.Product
import com.ayush.domain.model.User

interface NetworkService {

    suspend fun getProducts(): ResultWrapper<List<Product>>

    suspend fun login(email: String, password: String): ResultWrapper<User>

    suspend fun signup(name: String, email: String, password: String): ResultWrapper<User>
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()
}