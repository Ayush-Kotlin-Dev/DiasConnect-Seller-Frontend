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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase
) : ViewModel() {

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
        _loginForm.value = _loginForm.value.copy(
            email = email ?: _loginForm.value.email,
            password = password ?: _loginForm.value.password,
            isEmailValid = email?.isValidEmail() ?: _loginForm.value.isEmailValid,
            isPasswordValid = password?.isValidPassword() ?: _loginForm.value.isPasswordValid
        )
    }

    fun updateSignupForm(name: String? = null, email: String? = null, password: String? = null) {
        _signupForm.value = _signupForm.value.copy(
            name = name ?: _signupForm.value.name,
            email = email ?: _signupForm.value.email,
            password = password ?: _signupForm.value.password,
            isNameValid = name?.isValidName() ?: _signupForm.value.isNameValid,
            isEmailValid = email?.isValidEmail() ?: _signupForm.value.isEmailValid,
            isPasswordValid = password?.isValidPassword() ?: _signupForm.value.isPasswordValid
        )
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        val isEmailValid = email.isValidEmail()
        val isPasswordValid = password.isValidPassword()
        _loginForm.value = _loginForm.value.copy(
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid
        )
        return isEmailValid && isPasswordValid
    }

    private fun validateSignupInput(name: String, email: String, password: String): Boolean {
        val isNameValid = name.isValidName()
        val isEmailValid = email.isValidEmail()
        val isPasswordValid = password.isValidPassword()
        _signupForm.value = _signupForm.value.copy(
            isNameValid = isNameValid,
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid
        )
        return isNameValid && isEmailValid && isPasswordValid
    }

    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPassword(): Boolean =
        length >= 8 && contains(Regex("[A-Z]")) && contains(Regex("[a-z]")) && contains(Regex("[0-9]")) && contains(Regex("[^A-Za-z0-9]"))

    private fun String.isValidName(): Boolean = length >= 2

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
    val isPasswordValid: Boolean = true
)

data class SignupFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true
)