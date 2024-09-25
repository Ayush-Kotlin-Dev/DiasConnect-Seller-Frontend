package com.ayush.diasconnect_seller.ui.feature.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayush.diasconnect_seller.ui.feature.home.HomeViewModel
import com.ayush.diasconnect_seller.ui.theme.*

@Composable
fun AuthScreen(
    navController: NavController,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    var isLoginMode by remember { mutableStateOf(true) }
    val authState by viewModel.authState.collectAsState()
    val loginForm by viewModel.loginForm.collectAsState()
    val signupForm by viewModel.signupForm.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                val message = if (isLoginMode) "Login successful" else "Registration successful"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IndiaPostBlack)
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
                onSignupClick = { viewModel.signup(signupForm.name, signupForm.email, signupForm.password) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthStateMessage(authState)
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
            .background(IndiaPostGray)
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
            contentColor = if (isSelected) Color.White else IndiaPostLightGray
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
        AnimatedVisibility(visible = !isLoginMode) {
            AuthTextField(
                value = signupForm.name,
                onValueChange = { onSignupFormChange(it, null, null) },
                label = "Name",
                isError = !signupForm.isNameValid,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }
        AuthTextField(
            value = if (isLoginMode) loginForm.email else signupForm.email,
            onValueChange = { if (isLoginMode) onLoginFormChange(it, null) else onSignupFormChange(null, it, null) },
            label = "Email",
            isError = if (isLoginMode) !loginForm.isEmailValid else !signupForm.isEmailValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        AuthTextField(
            value = if (isLoginMode) loginForm.password else signupForm.password,
            onValueChange = { if (isLoginMode) onLoginFormChange(null, it) else onSignupFormChange(null, null, it) },
            label = "Password",
            isError = if (isLoginMode) !loginForm.isPasswordValid else !signupForm.isPasswordValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation()
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
    keyboardOptions: KeyboardOptions,
    visualTransformation: PasswordVisualTransformation = PasswordVisualTransformation()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            focusedContainerColor = IndiaPostGray,
            unfocusedContainerColor = IndiaPostGray,
            focusedLabelColor = IndiaPostRed,
            unfocusedLabelColor = IndiaPostGray,
            errorContainerColor = IndiaPostGray,
            errorLabelColor = Color.Red
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

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