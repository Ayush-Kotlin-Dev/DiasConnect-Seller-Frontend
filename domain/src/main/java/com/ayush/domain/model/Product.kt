package com.ayush.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val image: String
)