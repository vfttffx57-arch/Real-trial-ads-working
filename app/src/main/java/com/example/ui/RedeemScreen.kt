package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.MainViewModel
import com.example.ui.theme.Accent

@Composable
fun RedeemScreen(navController: NavController, viewModel: MainViewModel) {
    val user by viewModel.loggedInUser.collectAsState()
    var upiId by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Redeem Points", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Current Points: ${user?.points ?: 0}", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = upiId,
            onValueChange = { upiId = it },
            label = { Text("Enter UPI ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        val options = listOf(Pair(1, 10), Pair(5, 50), Pair(100, 1000))

        options.forEach { (amount, pts) ->
            Button(
                onClick = {
                    if (upiId.isBlank()) {
                        Toast.makeText(context, "Enter UPI ID first", Toast.LENGTH_SHORT).show()
                    } else if ((user?.points ?: 0) < pts) {
                        Toast.makeText(context, "Not enough points", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.submitWithdrawal(amount, upiId)
                        Toast.makeText(context, "Request Submitted", Toast.LENGTH_SHORT).show()
                        upiId = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) {
                Text("Withdraw ₹$amount ($pts points)", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
