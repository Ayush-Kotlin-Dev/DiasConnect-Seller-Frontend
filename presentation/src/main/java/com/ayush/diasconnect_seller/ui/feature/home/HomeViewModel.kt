package com.ayush.diasconnect_seller.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.Product
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            getProductUseCase.execute().let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        val data = result.value
                        _uiState.value = HomeScreenUIEvents.Success(data)
                    }
                    is ResultWrapper.Error -> {
                        _uiState.value = HomeScreenUIEvents.Error(result.exception.message.toString())
                    }
                }
            }
        }
    }
}



sealed class HomeScreenUIEvents {
    object Loading : HomeScreenUIEvents() // Changed to 'object'
    data class Success(val data: List<Product>) : HomeScreenUIEvents()
    data class Error(val message: String) : HomeScreenUIEvents()
}
