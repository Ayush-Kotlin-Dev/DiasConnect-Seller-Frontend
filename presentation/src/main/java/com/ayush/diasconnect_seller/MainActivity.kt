package com.ayush.diasconnect_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ayush.diasconnect_seller.ui.theme.DiasConnectSellerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayush.diasconnect_seller.ui.feature.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiasConnectSellerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController , startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

