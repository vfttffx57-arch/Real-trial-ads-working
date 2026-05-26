package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.MainViewModel
import com.example.ads.AdsManager
import com.example.ui.theme.Accent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, result: String, viewModel: MainViewModel) {
    val user by viewModel.loggedInUser.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("hm") { popUpTo(0) } }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                when (result) {
                    "Win" -> "🎉 You Won! 🎉"
                    "Lose" -> "💀 You Lost! 💀"
                    else -> "🤝 Draw! 🤝"
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (result == "Win") {
                Text("+10 Points", color = MaterialTheme.colorScheme.primary, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Current Points: ${user?.points ?: 0}", fontSize = 20.sp)
            
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    AdsManager.showAd(context, AdsManager.INTERSTITIAL_AD_UNIT) {
                        navController.navigate("hm") { popUpTo(0) }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) {
                Text("Back to Home")
            }
        }
    }
}
