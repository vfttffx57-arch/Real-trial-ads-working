package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatchingScreen(navController: NavController) {
    val opponents = listOf("Alex_99", "ProGamer", "Ninja2000", "Shadow", "KingT", "Ghost", "NoobSlayer")
    var currentIndex by remember { mutableStateOf(0) }
    var isMatched by remember { mutableStateOf(false) }
    var opponentName by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        val totalSpins = 20
        var delayTime = 50L
        for (i in 0 until totalSpins) {
            currentIndex = (currentIndex + 1) % opponents.size
            delay(delayTime)
            delayTime += 10
        }
        opponentName = opponents[currentIndex]
        isMatched = true
        delay(1500)
        navController.navigate("game/$opponentName") {
            popUpTo("matching") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Finding Opponent...", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(100.dp)
                .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            val displayMode = if (isMatched) opponentName else opponents[currentIndex]
            Text(
                text = displayMode, 
                fontSize = 28.sp, 
                color = if (isMatched) Color.Green else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        if (isMatched) {
            Text("Matched! Starting Game...", color = Color.Gray)
        }
    }
}
