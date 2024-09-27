package com.ayush.domain.repository

import com.ayush.domain.model.ProductUploadRequest
import com.ayush.domain.model.ProductUploadResponse
import com.ayush.domain.network.ResultWrapper

interface UploadProductRepository {
    suspend fun uploadProduct(productData: ProductUploadRequest, imageFiles: List<ByteArray>): ResultWrapper<ProductUploadResponse>

}