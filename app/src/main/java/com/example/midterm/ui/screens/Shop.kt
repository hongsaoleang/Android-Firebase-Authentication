package com.example.midterm.ui.screens

import CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.midterm.ui.model.MenuItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems = cartViewModel.cartItems
    val totalPrice = cartViewModel.getTotalPrice()

    // State to control the visibility of the receipt dialog
    var showReceipt by remember { mutableStateOf(false) }

    // --- RECEIPT DIALOG ---
    if (showReceipt) {
        AlertDialog(
            onDismissRequest = { showReceipt = false },
            title = { Text("Order Receipt", style = MaterialTheme.typography.headlineMedium) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // List every item in the receipt
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name)
                            Text("$${String.format("%.2f", item.price)}")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // Final Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount:", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text(
                            "$${String.format("%.2f", totalPrice)}",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReceipt = false
                        cartViewModel.clearCart() // Clear the cart after purchase
                        navController.popBackStack() // Go back to menu
                    }
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReceipt = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $${String.format("%.2f", totalPrice)}",
                            style = MaterialTheme.typography.headlineSmall)

                        // Click this to show the receipt
                        Button(onClick = { showReceipt = true }) {
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Your cart is empty")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(cartItems) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text("$${item.price}") },
                        trailingContent = {
                            IconButton(onClick = { cartViewModel.removeFromCart(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}