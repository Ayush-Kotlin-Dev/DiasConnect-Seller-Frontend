package com.ayush.diasconnect_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayush.diasconnect_seller.ui.feature.auth.AuthScreen
import com.ayush.diasconnect_seller.ui.feature.home.HomeScreen
import com.ayush.diasconnect_seller.ui.theme.DiasConnectSellerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DiasConnectSellerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "auth") {
                    composable("home") {
                        HomeScreen(
                            navController = navController
                        )
                    }
                    composable("auth") {
                         AuthScreen(navController = navController)
                    }
                }
            }
        }
    }
}

