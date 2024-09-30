package com.ayush.diasconnect_seller.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.model.Product
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()



    init {
        getProductsDummy()
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

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getProducts() // Or use getProducts() if you want to fetch real data
            _isRefreshing.value = false
        }
    }

    private fun getProductsDummy() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            val data = listOf(
                Product(
                    id = 1,
                    name = "Product 1",
                    price = 10.00.toFloat() ,
                    images = listOf("https://picsum.photos/200/300"),
                    stock = 10,
                    categoryId = 1,
                    sellerId = 1,
                    description = "Product 1 description"
                ),
                Product(
                    id = 2,
                    name = "Product 2",
                    price = 20.00.toFloat() ,
                    images = listOf("https://picsum.photos/200/300"),
                    stock = 20,
                    categoryId = 2,
                    sellerId = 2,
                    description = "Product 2 description"
                ),
                Product(
                    id = 3,
                    name = "Product 3",
                    price = 30.00.toFloat() ,
                    images = listOf("https://picsum.photos/200/300"),
                    stock = 30,
                    categoryId = 3,
                    sellerId = 3,
                    description = "Product 3 description"
                ),
                Product(
                    id = 4,
                    name = "Product 4",
                    price = 40.00.toFloat() ,
                    images = listOf("https://picsum.photos/200/300"),
                    stock = 40,
                    categoryId = 4,
                    sellerId = 4,
                    description = "Product 4 description"
                ),
            )
            delay(2000)
            _uiState.value = HomeScreenUIEvents.Success(data)
        }
    }
}



sealed class HomeScreenUIEvents {
    object Loading : HomeScreenUIEvents() // Changed to 'object'
    data class Success(val data: List<Product>) : HomeScreenUIEvents()
    data class Error(val message: String) : HomeScreenUIEvents()
}
