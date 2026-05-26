package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.MainViewModel
import com.example.game.TicTacToeAI
import com.example.ui.theme.Accent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavController, opponentName: String, viewModel: MainViewModel) {
    var board by remember { mutableStateOf(Array(9) { "" }) }
    var isPlayerTurn by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }
    var showResultDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val playerMark = "X"
    val aiMark = "O"

    fun checkResult() {
        val w = TicTacToeAI.checkWinner(board)
        if (w != null) {
            winner = w
            showResultDialog = true
        } else if (TicTacToeAI.isBoardFull(board)) {
            winner = "Draw"
            showResultDialog = true
        } else {
            isPlayerTurn = false
            coroutineScope.launch {
                delay(600) // AI thinking delay
                val aiMove = TicTacToeAI.getBestMove(board, aiMark, playerMark)
                if (aiMove != -1) {
                    val newBoard = board.copyOf()
                    newBoard[aiMove] = aiMark
                    board = newBoard
                    val w2 = TicTacToeAI.checkWinner(board)
                    if (w2 != null) {
                        winner = w2
                        showResultDialog = true
                    } else if (TicTacToeAI.isBoardFull(board)) {
                        winner = "Draw"
                        showResultDialog = true
                    } else {
                        isPlayerTurn = true
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playing vs $opponentName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            Text(if (isPlayerTurn) "Your Turn (X)" else "Opponent's Turn (O)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(30.dp))

            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .background(Accent)
                                    .clickable {
                                        if (isPlayerTurn && board[index] == "" && winner == null) {
                                            val newBoard = board.copyOf()
                                            newBoard[index] = playerMark
                                            board = newBoard
                                            checkResult()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(board[index], fontSize = 48.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showResultDialog) {
        val resultText = when (winner) {
            playerMark -> "Win"
            aiMark -> "Lose"
            else -> "Draw"
        }
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text(if (resultText == "Win") "You won! +10 Points" else if (resultText == "Lose") "You lose!" else "It's a draw!") },
            confirmButton = {
                Button(onClick = {
                    viewModel.insertGameMatch(opponentName, resultText)
                    showResultDialog = false
                    navController.navigate("result/${resultText}") {
                        popUpTo("hm")
                    }
                }) {
                    Text("Confirm")
                }
            }
        )
    }
}
