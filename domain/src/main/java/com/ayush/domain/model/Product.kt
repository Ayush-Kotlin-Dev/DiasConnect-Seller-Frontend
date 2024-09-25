package com.ayush.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long? = null,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long,
    val images: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String ? = null,
)