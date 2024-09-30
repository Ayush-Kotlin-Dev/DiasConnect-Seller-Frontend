package com.ayush.diasconnect_seller.ui.feature.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ayush.diasconnect_seller.ContainerApp
import com.ayush.diasconnect_seller.ui.theme.*

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = hiltViewModel()
        var isLoginMode by remember { mutableStateOf(true) }
        val authState by viewModel.authState.collectAsState()
        val loginForm by viewModel.loginForm.collectAsState()
        val signupForm by viewModel.signupForm.collectAsState()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        LaunchedEffect(authState) {
            when (authState) {
                is AuthState.Authenticated -> {
                    val message =
                        if (isLoginMode) "Login successful" else "Registration successful"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    navigator.replaceAll(ContainerApp(user = (authState as AuthState.Authenticated).user))
                }

                is AuthState.Error -> {
                    Toast.makeText(
                        context,
                        (authState as AuthState.Error).message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo()
                Spacer(modifier = Modifier.height(32.dp))
                AuthToggle(isLoginMode) { isLoginMode = it }
                Spacer(modifier = Modifier.height(24.dp))
                AuthForm(
                    isLoginMode = isLoginMode,
                    loginForm = loginForm,
                    signupForm = signupForm,
                    onLoginFormChange = viewModel::updateLoginForm,
                    onSignupFormChange = viewModel::updateSignupForm
                )
                Spacer(modifier = Modifier.height(24.dp))
                AuthButton(
                    isLoginMode = isLoginMode,
                    loginForm = loginForm,
                    signupForm = signupForm,
                    onLoginClick = { viewModel.login(loginForm.email, loginForm.password) },
                    onSignupClick = {
                        viewModel.signup(
                            signupForm.name,
                            signupForm.email,
                            signupForm.password
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AuthStateMessage(authState)
            }
        }
    }
}


@Composable
fun Logo() {
    Text(
        text = "India Post Courier",
        color = IndiaPostRed,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun AuthToggle(isLoginMode: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(IndiaPostLightGray)
    ) {
        ToggleButton(
            text = "Login",
            isSelected = isLoginMode,
            onClick = { onToggle(true) }
        )
        ToggleButton(
            text = "Signup",
            isSelected = !isLoginMode,
            onClick = { onToggle(false) }
        )
    }
}

@Composable
fun ToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) IndiaPostRed else Color.Transparent,
            contentColor = if (isSelected) Color.White else IndiaPostGray
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text)
    }
}

@Composable
fun AuthForm(
    isLoginMode: Boolean,
    loginForm: LoginFormState,
    signupForm: SignupFormState,
    onLoginFormChange: (String?, String?) -> Unit,
    onSignupFormChange: (String?, String?, String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(visible = !isLoginMode, enter = fadeIn(), exit = fadeOut()) {
            AuthTextField(
                value = signupForm.name,
                onValueChange = { onSignupFormChange(it, null, null) },
                label = "Username",
                isError = !signupForm.isNameValid,
                errorMessage = signupForm.nameErrorMessage,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isUsername = true
            )
        }
        AuthTextField(
            value = if (isLoginMode) loginForm.email else signupForm.email,
            onValueChange = {
                if (isLoginMode) onLoginFormChange(it, null) else onSignupFormChange(
                    null,
                    it,
                    null
                )
            },
            label = "Email",
            isError = if (isLoginMode) !loginForm.isEmailValid else !signupForm.isEmailValid,
            errorMessage = if (isLoginMode) loginForm.emailErrorMessage else signupForm.emailErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        AuthTextField(
            value = if (isLoginMode) loginForm.password else signupForm.password,
            onValueChange = {
                if (isLoginMode) onLoginFormChange(null, it) else onSignupFormChange(
                    null,
                    null,
                    it
                )
            },
            label = "Password",
            isError = if (isLoginMode) !loginForm.isPasswordValid else !signupForm.isPasswordValid,
            errorMessage = if (isLoginMode) loginForm.passwordErrorMessage else signupForm.passwordErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isPassword = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions,
    isPassword: Boolean = false,
    isUsername: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (isUsername) {
                    onValueChange(it.lowercase())
                } else {
                    onValueChange(it)
                }
            },
            label = { Text(label) },
            isError = isError,
            keyboardOptions = keyboardOptions,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Check,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = IndiaPostRed,
                unfocusedIndicatorColor = IndiaPostGray,
                errorIndicatorColor = Color.Red,
                focusedTextColor = IndiaPostBlack,
                errorLabelColor = Color.Red,
                errorTrailingIconColor = Color.Red
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )

        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

//        if (isPassword && isFocused) {
//            PasswordStrengthIndicator(password = value)
//        }
    }
}

//@Composable
//fun PasswordStrengthIndicator(password: String) {
//    val hasUppercase = password.any { it.isUpperCase() }
//    val hasLowercase = password.any { it.isLowerCase() }
//    val hasSpecialChar = password.any { it in "!@#$%^&*()" }
//    val hasNumber = password.any { it.isDigit() }
//    val hasValidLength = password.length > 8
//
//    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
//        Text(
//            text = "Password strength:",
//            style = MaterialTheme.typography.bodySmall,
//            color = IndiaPostGray
//        )
//        PasswordRequirement("Uppercase letter", hasUppercase)
//        PasswordRequirement("Lowercase letter", hasLowercase)
//        PasswordRequirement("Special character", hasSpecialChar)
//        PasswordRequirement("Number", hasNumber)
//        PasswordRequirement("At least 8 characters", hasValidLength)
//    }
//}
//
//@Composable
//fun PasswordRequirement(text: String, isMet: Boolean) {
//    Row(
//        modifier = Modifier.padding(vertical = 2.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Icon(
//            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.Clear,
//            contentDescription = null,
//            tint = if (isMet) IndiaPostGreen else Color.Red
//        )
//        Text(
//            text = text,
//            style = MaterialTheme.typography.bodySmall,
//            color = if (isMet) IndiaPostGreen else Color.Red
//        )
//    }
//}

@Composable
fun AuthButton(
    isLoginMode: Boolean,
    loginForm: LoginFormState,
    signupForm: SignupFormState,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Button(
        onClick = if (isLoginMode) onLoginClick else onSignupClick,
        enabled = if (isLoginMode) {
            loginForm.isEmailValid && loginForm.isPasswordValid
        } else {
            signupForm.isNameValid && signupForm.isEmailValid && signupForm.isPasswordValid
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = IndiaPostRed,
            contentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(if (isLoginMode) "Login" else "Signup")
    }
}

@Composable
fun AuthStateMessage(authState: AuthState) {
    when (authState) {
        is AuthState.Loading -> CircularProgressIndicator(color = IndiaPostRed)
        is AuthState.Error -> Text(
            authState.message,
            color = Color.Red
        )

        else -> {}
    }
}