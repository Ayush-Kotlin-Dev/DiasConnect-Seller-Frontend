package com.ayush.domain.usecase

import com.ayush.domain.model.ProductUploadRequest
import com.ayush.domain.model.ProductUploadResponse
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.UploadProductRepository
import javax.inject.Inject

class UploadProductUseCase @Inject constructor(
    private val repository: UploadProductRepository
) {
    suspend operator fun invoke(productData: ProductUploadRequest, imageFiles: List<ByteArray>): ResultWrapper<ProductUploadResponse> {
        // Ensure we don't upload more than 7 images
        val limitedImageFiles = imageFiles.take(7)
        return repository.uploadProduct(productData, limitedImageFiles)
    }
}