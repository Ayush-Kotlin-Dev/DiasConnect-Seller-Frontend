package com.ayush.data.repository

import com.ayush.domain.model.Product
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val networkService: NetworkService
) : ProductRepository {
    override suspend fun getProducts(): ResultWrapper<List<Product>> {
        return networkService.getProducts()
    }
}