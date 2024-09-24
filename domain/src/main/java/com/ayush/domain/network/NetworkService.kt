package com.ayush.domain.network

import com.ayush.domain.model.Product

interface NetworkService {

    suspend fun getProducts(): ResultWrapper<List<Product>>
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()
}