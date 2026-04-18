package com.example.midterm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun AddEditMenuScreen(navController: NavController, menuId: String? = null) {
    val db = Firebase.firestore
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // If editing, load the current data
    LaunchedEffect(menuId) {
        if (menuId != null) {
            db.collection("menu").document(menuId).get().addOnSuccessListener { doc ->
                name = doc.getString("name") ?: ""
                price = doc.getDouble("price")?.toString() ?: ""
                qty = doc.getLong("qty")?.toString() ?: ""
                description = doc.getString("description") ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (menuId == null) "Add New Item" else "Edit Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Respect the navbar height
                .padding(16.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Food Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    val data = hashMapOf(
                        "name" to name,
                        "price" to (price.toDoubleOrNull() ?: 0.0),
                        "qty" to (qty.toIntOrNull() ?: 0),
                        "description" to description
                    )

                    if (menuId == null) {
                        db.collection("menu").add(data).addOnSuccessListener { navController.popBackStack() }
                    } else {
                        db.collection("menu").document(menuId).set(data).addOnSuccessListener { navController.popBackStack() }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(if (menuId == null) "Add to Menu" else "Update Item")
            }
        }
    }
}