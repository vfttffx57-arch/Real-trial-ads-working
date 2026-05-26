package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController, viewModel: MainViewModel) {
    val withdrawals by viewModel.getAllWithdrawals().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(withdrawals) { request ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("User ID: ${request.userId} | ₹${request.amountInr}")
                        Text("UPI: ${request.upiId}")
                        Text("Status: ${request.status}", color = if (request.status == "Pending") Color.Yellow else Color.Gray)
                        
                        if (request.status == "Pending") {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { viewModel.processWithdrawal(request, true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                ) {
                                    Text("Approve")
                                }
                                Button(
                                    onClick = { viewModel.processWithdrawal(request, false) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                                ) {
                                    Text("Deny")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
