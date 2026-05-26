package com.example

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {
    private val dao = db.appDao()

    val loggedInUser: StateFlow<UserEntity?> = dao.getLoggedInUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()
    
    private val _signupError = MutableStateFlow<String?>(null)
    val signupError = _signupError.asStateFlow()

    fun clearErrors() {
        _loginError.value = null
        _signupError.value = null
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            val user = dao.getUserByEmail(email)
            if (user != null && user.passwordHash == pass) {
                dao.logoutAll()
                dao.updateUser(user.copy(isLoggedIn = true))
            } else {
                _loginError.value = "Invalid email or password"
            }
        }
    }

    fun signup(email: String, pass: String) {
        viewModelScope.launch {
            val existing = dao.getUserByEmail(email)
            if (existing != null) {
                _signupError.value = "Email already exists"
                return@launch
            }
            dao.logoutAll()
            dao.insertUser(UserEntity(email = email, passwordHash = pass, isLoggedIn = true))
        }
    }

    fun logout() {
        viewModelScope.launch {
            dao.logoutAll()
        }
    }

    fun addPoints(amount: Int) {
        viewModelScope.launch {
            loggedInUser.value?.let { user ->
                dao.updateUser(user.copy(points = user.points + amount))
            }
        }
    }

    fun insertGameMatch(opponent: String, result: String) {
        viewModelScope.launch {
            loggedInUser.value?.let { user ->
                dao.insertGame(GameEntity(userId = user.id, opponentName = opponent, result = result))
                if (result == "Win") {
                    addPoints(10)
                }
            }
        }
    }

    fun submitWithdrawal(amountInr: Int, upi: String) {
        viewModelScope.launch {
            loggedInUser.value?.let { user ->
                val pointsNeeded = amountInr * 10
                if (user.points >= pointsNeeded) {
                    dao.updateUser(user.copy(points = user.points - pointsNeeded))
                    dao.insertWithdrawal(WithdrawEntity(userId = user.id, amountInr = amountInr, upiId = upi))
                }
            }
        }
    }
    
    fun getGameHistory(userId: Int) = dao.getGamesForUser(userId)
    fun getWithdrawalHistory(userId: Int) = dao.getWithdrawalsForUser(userId)
    fun getAllWithdrawals() = dao.getAllWithdrawals()
    
    fun processWithdrawal(withdrawal: WithdrawEntity, isApproved: Boolean) {
        viewModelScope.launch {
            if (isApproved) {
                dao.updateWithdrawal(withdrawal.copy(status = "Success"))
            } else {
                dao.updateWithdrawal(withdrawal.copy(status = "Denied", reason = "Declined by Admin"))
                // Refund points
                val user = dao.getUserById(withdrawal.userId)
                if (user != null) {
                    dao.updateUser(user.copy(points = user.points + (withdrawal.amountInr * 10)))
                }
            }
        }
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "fun-earn-db"
        ).fallbackToDestructiveMigration().build()
        return MainViewModel(db) as T
    }
}
