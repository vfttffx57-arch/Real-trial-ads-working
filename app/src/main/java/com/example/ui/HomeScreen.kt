package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.MainViewModel
import com.example.ui.theme.Accent

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val user by viewModel.loggedInUser.collectAsState()
    var adminClickCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Icon
            IconButton(
                onClick = { navController.navigate("profile") },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(32.dp))
            }

            // Points
            Surface(
                color = Accent,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.clickable {
                    adminClickCount++
                    if (adminClickCount >= 9) {
                        adminClickCount = 0
                        // Navigate to Admin
                        navController.navigate("admin")
                    }
                }
            ) {
                Text(
                    text = "💰 ${user?.points ?: 0} PTS",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Button(
                onClick = { navController.navigate("matching") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) {
                Text("Play Tic Tac Toe", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "🎮 Beat the AI, earn 10 Points!\n⚡ Fast Paced Action\n💸 Redeem points for real money",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
