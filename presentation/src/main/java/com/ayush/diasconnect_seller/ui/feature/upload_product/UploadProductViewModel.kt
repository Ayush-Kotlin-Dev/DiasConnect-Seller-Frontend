package com.ayush.diasconnect_seller.ui.feature.upload_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.ProductUploadRequest
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.usecase.UploadProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val uploadProductUseCase: UploadProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UploadProductUiState())
    val uiState: StateFlow<UploadProductUiState> = _uiState.asStateFlow()

    fun updateProductParams(params: ProductParams) {
        _uiState.value = _uiState.value.copy(productParams = params)
    }

    fun addImage(image: File) {
        if (_uiState.value.selectedImages.size < 7) {
            _uiState.value = _uiState.value.copy(
                selectedImages = _uiState.value.selectedImages + image
            )
        }
    }

    fun removeImage(image: File) {
        _uiState.value = _uiState.value.copy(
            selectedImages = _uiState.value.selectedImages - image
        )
    }

    fun submitProduct() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val productParams = _uiState.value.productParams
                val imageFiles = _uiState.value.selectedImages.map { it.readBytes() }
                val result = uploadProductUseCase(
                    ProductUploadRequest(
                        productParams.name,
                        productParams.price,
                        productParams.description,
                        productParams.stock,
                        productParams.categoryId,
                        productParams.sellerId
                    ),
                    imageFiles
                )
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            successMessage = "Product uploaded successfully!"
                        )
                    }
                    is ResultWrapper.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "An error occurred"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = UploadProductUiState()
    }
}

data class ProductParams(
    val name: String = "",
    val price: Float = 0f,
    val description: String = "",
    val stock: Int = 0,
    val categoryId: Long = 0L,
    val sellerId: Long = 0L
)

data class UploadProductUiState(
    val productParams: ProductParams = ProductParams(),
    val selectedImages: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)