package com.ayush.diasconnect_seller

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ayush.diasconnect_seller.utils.NavGraph
import com.ayush.diasconnect_seller.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerApp() {
    val navController = rememberNavController()
    val isUserLoggedIn by remember { mutableStateOf(true) } //TODO implement DataStore to store user login state

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Home",
            icon = Icons.Filled.Home,
            screen = Screen.Home
        ),
        BottomNavItem(
            title = "Orders",
            icon = Icons.Filled.ShoppingCart,
            screen = Screen.Orders
        ),
        BottomNavItem(
            title = "Dashboard",
            icon = Icons.Filled.Menu,
            screen = Screen.Dashboard
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != Screen.Auth.route) {
                NavigationBar(
                    containerColor = Color.White,
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = Color.Red.copy(alpha = 0.7f),
//                                unselectedIconColor = Color.Black.copy(alpha = 0.7f),
//                                indicatorColor = Color.Transparent,
//                            ),
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)