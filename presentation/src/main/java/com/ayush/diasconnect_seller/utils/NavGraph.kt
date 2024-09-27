package com.ayush.diasconnect_seller.utils

import UploadProductScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayush.diasconnect_seller.ui.feature.auth.AuthScreen
import com.ayush.diasconnect_seller.ui.feature.home.HomeScreen

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    object Orders : Screen("orders")
    object Dashboard : Screen("dashboard")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Orders.route) {
             OrdersScreen(navController)
        }
        composable(Screen.Dashboard.route) {
            UploadProductScreen(
                navController = navController,
                modifier = modifier
            )
        }
    }
}

@Composable
fun OrdersScreen(navController: NavHostController) {
        Text(text = "Orders Screen")
}
