package com.ayush.domain.usecase

import com.ayush.domain.repository.ProductRepository

class GetProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke() = productRepository.getProducts()

}