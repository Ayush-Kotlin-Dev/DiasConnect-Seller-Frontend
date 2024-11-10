package com.ayush.domain.network

import com.ayush.domain.model.Product
import com.ayush.domain.model.ProductUploadRequest
import com.ayush.domain.model.ProductUploadResponse
import com.ayush.domain.model.User
import diasconnect.seller.com.model.myOrder

interface NetworkService {

    suspend fun getProducts(): ResultWrapper<List<Product>>

    suspend fun login(email: String, password: String): ResultWrapper<User>

    suspend fun signup(name: String, email: String, password: String): ResultWrapper<User>

    suspend fun uploadProduct(productData: ProductUploadRequest, imageFiles: List<ByteArray>): ResultWrapper<ProductUploadResponse>

    suspend fun getOrdersBySellerId(): ResultWrapper<List<myOrder>>

}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()
}