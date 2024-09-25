package com.ayush.domain.usecase

import com.ayush.domain.repository.ProductRepository

class GetProductUseCase(private val repository: ProductRepository) {
    suspend fun execute() = repository.getProducts()
}