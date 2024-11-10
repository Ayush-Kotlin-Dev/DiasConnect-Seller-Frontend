package com.ayush.diasconnect_seller.ui.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import diasconnect.seller.com.model.myOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderScreenUIEvents>(OrderScreenUIEvents.Loading)
    val uiState: StateFlow<OrderScreenUIEvents> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private var originalOrders: List<myOrder> = emptyList()

    init {
        getOrders()
    }

    fun getOrders() {
        viewModelScope.launch {
            _uiState.value = OrderScreenUIEvents.Loading
            when (val result = getOrdersUseCase()) {
                is ResultWrapper.Success -> {
                    originalOrders = result.value
                    sortAndUpdateOrders()
                }
                is ResultWrapper.Error -> {
                    _uiState.value = OrderScreenUIEvents.Error(result.exception.message ?: "Unknown error occurred")
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getOrders()
            _isRefreshing.value = false
        }
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        sortAndUpdateOrders()
    }

    private fun sortAndUpdateOrders() {
        val sortedOrders = when (_sortOption.value) {
            SortOption.DATE_DESC -> originalOrders.sortedByDescending { it.createdAt }
            SortOption.DATE_ASC -> originalOrders.sortedBy { it.createdAt }
            SortOption.TOTAL_DESC -> originalOrders.sortedByDescending { it.total }
            SortOption.TOTAL_ASC -> originalOrders.sortedBy { it.total }
        }
        _uiState.value = OrderScreenUIEvents.Success(sortedOrders)
    }
}

enum class SortOption(val displayName: String) {
    DATE_DESC("Newest First"),
    DATE_ASC("Oldest First"),
    TOTAL_DESC("Highest Total"),
    TOTAL_ASC("Lowest Total")
}

// ... (keep OrderScreenUIEvents as it was)
sealed class OrderScreenUIEvents {
    object Loading : OrderScreenUIEvents()
    data class Success(val data: List<myOrder>) : OrderScreenUIEvents()
    data class Error(val message: String) : OrderScreenUIEvents()
}