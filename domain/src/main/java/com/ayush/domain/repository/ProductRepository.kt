package com.ayush.domain.repository

import com.ayush.domain.model.Product
import com.ayush.domain.network.ResultWrapper

interface ProductRepository {

    suspend fun getProducts(): ResultWrapper<List<Product>>

//    suspend fun getProductById(id: Long): ResultWrapper<Product>
//
//    suspend fun addProduct(product: Product): ResultWrapper<Unit>
//
//    suspend fun updateProduct(product: Product): ResultWrapper<Unit>
//
//    suspend fun deleteProduct(id: Long): ResultWrapper<Unit>




}