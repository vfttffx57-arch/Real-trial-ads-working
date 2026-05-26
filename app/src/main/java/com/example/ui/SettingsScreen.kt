package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.Accent

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Settings", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("game_history") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Accent)
        ) {
            Text("Game History", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("withdraw_history") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Accent)
        ) {
            Text("Withdraw History", fontSize = 18.sp)
        }
    }
}
