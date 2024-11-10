package com.ayush.domain.repository

import com.ayush.domain.network.ResultWrapper
import diasconnect.seller.com.model.myOrder

interface GetOrderRepository {
    suspend fun getOrdersBySellerId(): ResultWrapper<List<myOrder>>

}