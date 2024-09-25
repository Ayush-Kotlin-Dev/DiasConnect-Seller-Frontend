package com.ayush.data.repository

import com.ayush.domain.model.User
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.repository.AuthRepository


class AuthRepositoryImpl(
    private val networkService: NetworkService
) : AuthRepository {
    override suspend fun login(email: String, password: String): ResultWrapper<User> {
        return networkService.login(email, password)
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): ResultWrapper<User> {
        return networkService.signup(name, email, password)
    }
}
