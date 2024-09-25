package com.ayush.domain.usecase


import com.ayush.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend fun execute(email: String, password: String) = repository.login(email, password)
}