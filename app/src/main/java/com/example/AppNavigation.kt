package com.example

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import com.example.ui.*

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val startDest = "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(navController, viewModel) }
        composable("signup") { SignupScreen(navController, viewModel) }
        composable("main") { MainContainer(viewModel) } 
    }
}

@Composable
fun MainContainer(viewModel: MainViewModel) {
    val bottomNavController = rememberNavController()
    
    val items = listOf(
        Pair("hm", "Home" to Icons.Default.Home),
        Pair("redeem", "Redeem" to Icons.Default.MonetizationOn),
        Pair("settings", "Settings" to Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { (route, data) ->
                    val (title, icon) = data
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            if (currentRoute != route) {
                                bottomNavController.navigate(route) {
                                    popUpTo("hm") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "hm",
            modifier = Modifier.padding(padding)
        ) {
            composable("hm") { HomeScreen(bottomNavController, viewModel) }
            composable("redeem") { RedeemScreen(bottomNavController, viewModel) }
            composable("settings") { SettingsScreen(bottomNavController) }
            
            composable("profile") { ProfileScreen(bottomNavController, viewModel) }
            composable("admin") { AdminScreen(bottomNavController, viewModel) }
            
            composable("matching") { MatchingScreen(bottomNavController) }
            composable("game/{opponentName}") { backStackEntry ->
                val opponent = backStackEntry.arguments?.getString("opponentName") ?: "Unknown"
                GameScreen(bottomNavController, opponent, viewModel)
            }
            composable("result/{res}") { backStackEntry ->
                val res = backStackEntry.arguments?.getString("res") ?: "Draw"
                ResultScreen(bottomNavController, res, viewModel)
            }
            
            // Nested history screens accessible from Settings
            composable("game_history") { GameHistoryScreen(bottomNavController, viewModel) }
            composable("withdraw_history") { WithdrawHistoryScreen(bottomNavController, viewModel) }
        }
    }
}
