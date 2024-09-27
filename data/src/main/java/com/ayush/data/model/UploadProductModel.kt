package com.ayush.data.model

import com.ayush.domain.model.Product
import kotlinx.serialization.Serializable


@Serializable
data class ProductUploadResponse(
    val success: Boolean,
    val product: Product? = null,
    val message: String
)
