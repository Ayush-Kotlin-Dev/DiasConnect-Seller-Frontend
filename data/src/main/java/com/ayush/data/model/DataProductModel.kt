package com.ayush.data.model


import com.ayush.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
class DataProductModel(
    val id: Long,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long,    val images: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String ? = null,

) {

    fun toProduct() = Product(
        id = id,
        name = name,
        price = price,
        description = description,
        stock = stock,
        categoryId = categoryId,
        sellerId = sellerId,
        images = images,
        createdAt = createdAt,
        updatedAt = updatedAt


    )
}
@Serializable
data class ProductsResponse(
    val success: Boolean,
    val products: List<DataProductModel>,
    val message: String
)

