package com.ayush.diasconnect_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.diasconnect_seller.ui.theme.DiasConnectSellerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiasConnectSellerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: MainActivityViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    when (uiState) {
                        is MainActivityViewModel.UiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is MainActivityViewModel.UiState.LoggedIn -> {
                            ContainerApp(user = (uiState as MainActivityViewModel.UiState.LoggedIn).user)
                        }
                        is MainActivityViewModel.UiState.LoggedOut -> {
                            ContainerApp(user = null)
                        }
                    }
                }
            }
        }
    }
}