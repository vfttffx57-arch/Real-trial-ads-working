package com.example.game

object TicTacToeAI {

    fun getBestMove(board: Array<String>, aiPlayer: String, humanPlayer: String): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (i in board.indices) {
            if (board[i] == "") {
                board[i] = aiPlayer
                val score = minimax(board, 0, false, aiPlayer, humanPlayer)
                board[i] = ""
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: Array<String>, depth: Int, isMaximizing: Boolean, aiPlayer: String, humanPlayer: String): Int {
        val result = checkWinner(board)
        if (result == aiPlayer) return 10 - depth
        if (result == humanPlayer) return depth - 10
        if (isBoardFull(board)) return 0

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i] == "") {
                    board[i] = aiPlayer
                    val score = minimax(board, depth + 1, false, aiPlayer, humanPlayer)
                    board[i] = ""
                    bestScore = maxOf(score, bestScore)
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i] == "") {
                    board[i] = humanPlayer
                    val score = minimax(board, depth + 1, true, aiPlayer, humanPlayer)
                    board[i] = ""
                    bestScore = minOf(score, bestScore)
                }
            }
            return bestScore
        }
    }

    fun checkWinner(board: Array<String>): String? {
        val winPatterns = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Cols
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)                       // Diagonals
        )
        for (pattern in winPatterns) {
            val a = board[pattern[0]]
            val b = board[pattern[1]]
            val c = board[pattern[2]]
            if (a.isNotEmpty() && a == b && b == c) {
                return a
            }
        }
        return null
    }

    fun isBoardFull(board: Array<String>): Boolean {
        return board.none { it == "" }
    }
}
