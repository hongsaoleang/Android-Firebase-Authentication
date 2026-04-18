package com.example.midterm.navigation

import CartViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midterm.ui.screens.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // Initialize the Shared ViewModel here
    // This allows both MenuListScreen and Shop to use the same cart data
    val cartViewModel: CartViewModel = viewModel()

    val currentUser = Firebase.auth.currentUser
    val startDest = if (currentUser != null) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home") { HomeScreen(navController) }

        // Browsing Screen: Pass the cartViewModel
        composable("menu_list") {
            MenuListScreen(navController, cartViewModel)
        }

        // Shopping Cart Screen: Pass the same cartViewModel
        composable("shop_screen") {
            Shop(navController, cartViewModel)
        }

        // Admin features (Add/Edit)
        composable("add_menu") { AddEditMenuScreen(navController) }
        composable("edit_menu/{menuId}") { backStackEntry ->
            val menuId = backStackEntry.arguments?.getString("menuId")
            AddEditMenuScreen(navController, menuId)
        }
    }
}