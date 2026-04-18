package com.example.midterm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                    Firebase.auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid
                            val userMap = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "createdAt" to com.google.firebase.Timestamp.now()
                            )
                            uid?.let {
                                Firebase.firestore.collection("users").document(it).set(userMap)
                                    .addOnSuccessListener { navController.navigate("home") }
                                    .addOnFailureListener { Toast.makeText(context, "Firestore Error: ${it.message}", Toast.LENGTH_SHORT).show() }
                            }
                        }
                        .addOnFailureListener { Toast.makeText(context, "Auth Error: ${it.message}", Toast.LENGTH_LONG).show() }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Sign Up")
        }
    }
}