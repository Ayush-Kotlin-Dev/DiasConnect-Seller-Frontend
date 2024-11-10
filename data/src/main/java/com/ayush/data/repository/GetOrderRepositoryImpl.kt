package com.ayush.data.repository

import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.GetOrderRepository
import diasconnect.seller.com.model.myOrder

class GetOrderRepositoryImpl  (
    private val networkService: NetworkService,
) : GetOrderRepository {
    override suspend fun getOrdersBySellerId(): ResultWrapper<List<myOrder>> {
        return networkService.getOrdersBySellerId()
    }
}