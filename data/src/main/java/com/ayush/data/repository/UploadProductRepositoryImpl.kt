package com.ayush.data.repository

import com.ayush.domain.model.ProductUploadRequest
import com.ayush.domain.model.ProductUploadResponse
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.UploadProductRepository
import javax.inject.Inject

class UploadProductRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
) : UploadProductRepository {
    override suspend fun uploadProduct(productData: ProductUploadRequest, imageFiles: List<ByteArray>): ResultWrapper<ProductUploadResponse> {
        return networkService.uploadProduct(productData, imageFiles)
    }
}