package com.ayush.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductUploadRequest(
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long
)

@Serializable
data class ProductUploadResponse(
    val success: Boolean,
    val product: Product? = null,
    val message: String
)