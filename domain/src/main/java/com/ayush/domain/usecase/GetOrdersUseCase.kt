package com.ayush.domain.usecase

import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.GetOrderRepository
import diasconnect.seller.com.model.myOrder
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: GetOrderRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<myOrder>> {
        return repository.getOrdersBySellerId()
    }
}