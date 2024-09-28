package com.ayush.diasconnect_seller.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.User
import com.ayush.domain.network.ResultWrapper
import com.ayush.domain.usecase.LoginUseCase
import com.ayush.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase
) : ViewModel() {
    //todo show a welcome message to user after login or signup
    //todo show a option at login page to login with a previous account you have created by calling getCurrentUser() method in AuthRepository
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser


    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _loginForm = MutableStateFlow(LoginFormState())
    val loginForm: StateFlow<LoginFormState> = _loginForm

    private val _signupForm = MutableStateFlow(SignupFormState())
    val signupForm: StateFlow<SignupFormState> = _signupForm

    fun login(email: String, password: String) {
        if (!validateLoginInput(email, password)) return

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = loginUseCase.execute(email, password)) {
                is ResultWrapper.Success -> _authState.value = AuthState.Authenticated(result.value)
                is ResultWrapper.Error -> _authState.value = AuthState.Error(result.exception.message ?: "Unknown error occurred")
            }
        }
    }

    fun signup(name: String, email: String, password: String) {
        if (!validateSignupInput(name, email, password)) return

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = signupUseCase.execute(name, email, password)) {
                is ResultWrapper.Success -> _authState.value = AuthState.Authenticated(result.value)
                is ResultWrapper.Error -> _authState.value = AuthState.Error(result.exception.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateLoginForm(email: String? = null, password: String? = null) {
        val updatedEmail = email ?: _loginForm.value.email
        val updatedPassword = password ?: _loginForm.value.password

        _loginForm.value = _loginForm.value.copy(
            email = updatedEmail,
            password = updatedPassword,
            isEmailValid = validateEmail(updatedEmail) == null,
            isPasswordValid = validatePassword(updatedPassword) == null,
            emailErrorMessage = validateEmail(updatedEmail),
            passwordErrorMessage = validatePassword(updatedPassword)
        )
    }

    fun updateSignupForm(name: String? = null, email: String? = null, password: String? = null) {
        val updatedName = name ?: _signupForm.value.name
        val updatedEmail = email ?: _signupForm.value.email
        val updatedPassword = password ?: _signupForm.value.password

        _signupForm.value = _signupForm.value.copy(
            name = updatedName,
            email = updatedEmail,
            password = updatedPassword,
            isNameValid = validateUsername(updatedName) == null,
            isEmailValid = validateEmail(updatedEmail) == null,
            isPasswordValid = validatePassword(updatedPassword) == null,
            nameErrorMessage = validateUsername(updatedName),
            emailErrorMessage = validateEmail(updatedEmail),
            passwordErrorMessage = validatePassword(updatedPassword)
        )
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        _loginForm.update { it.copy(
            emailErrorMessage = emailError,
            passwordErrorMessage = passwordError
        ) }
        return emailError == null && passwordError == null
    }

    private fun validateSignupInput(name: String, email: String, password: String): Boolean {
        val nameError = validateUsername(name)
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        _signupForm.update { it.copy(
            nameErrorMessage = nameError,
            emailErrorMessage = emailError,
            passwordErrorMessage = passwordError
        ) }
        return nameError == null && emailError == null && passwordError == null
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isEmpty() -> "Username cannot be empty"
            username != username.lowercase() -> "Username must be in lowercase"
            !username.all { it.isLetter() || it == '_' || it == '.' } ->
                "Username must only contain letters, underscores, or dots"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.length < 8 -> "Password must be at least 8 characters long"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            !password.any { it in "!@#$%^&*()" } -> "Password must contain at least one special character"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email address"
        } else null
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
)

data class SignupFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val nameErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
)