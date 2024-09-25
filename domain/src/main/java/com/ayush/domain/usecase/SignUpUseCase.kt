package com.ayush.domain.usecase

import com.ayush.domain.repository.AuthRepository

class SignupUseCase(private val repository: AuthRepository) {
    suspend fun execute(name: String, email: String, password: String) = repository.signup(name, email, password)
}