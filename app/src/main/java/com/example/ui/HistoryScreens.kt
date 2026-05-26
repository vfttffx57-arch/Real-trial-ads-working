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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val user by viewModel.loggedInUser.collectAsState()
    val games by viewModel.getGameHistory(user?.id ?: 0).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(games) { game ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("vs ${game.opponentName}", style = MaterialTheme.typography.titleMedium)
                        Text("Result: ${game.result}", color = if (game.result == "Win") Color.Green else Color.Red)
                        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(game.timestamp))
                        Text(date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawHistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val user by viewModel.loggedInUser.collectAsState()
    val withdrawals by viewModel.getWithdrawalHistory(user?.id ?: 0).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Withdraw History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(withdrawals) { withdrawal ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("₹${withdrawal.amountInr} to ${withdrawal.upiId}", style = MaterialTheme.typography.titleMedium)
                        Text("Status: ${withdrawal.status}", color = when(withdrawal.status) {
                            "Success" -> Color.Green
                            "Denied" -> Color.Red
                            else -> Color.Yellow
                        })
                        if (withdrawal.reason.isNotEmpty()) {
                            Text("Reason: ${withdrawal.reason}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        }
                        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(withdrawal.timestamp))
                        Text(date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}
