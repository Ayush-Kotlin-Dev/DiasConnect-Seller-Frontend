package com.ayush.diasconnect_seller.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayush.diasconnect_seller.ui.feature.auth.AuthScreen
import com.ayush.diasconnect_seller.ui.feature.home.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("auth") {
            AuthScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("orders") {
            OrdersScreen(navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
    }
}

@Composable
fun OrdersScreen(
    navController: NavHostController
) {
    Text(text = "Orders Screen")
}

@Composable
fun DashboardScreen(
    navController: NavHostController
) {
    Text(text = "Dashboard Screen")
}