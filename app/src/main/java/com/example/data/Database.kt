package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val points: Int = 0,
    val isLoggedIn: Boolean = false
)

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val opponentName: String,
    val result: String, // "Win", "Lose", "Draw"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawals")
data class WithdrawEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val amountInr: Int,
    val upiId: String,
    val status: String = "Pending", // "Pending", "Success", "Denied"
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface AppDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUserSync(): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Insert
    suspend fun insertGame(game: GameEntity)

    @Query("SELECT * FROM games WHERE userId = :userId ORDER BY timestamp DESC")
    fun getGamesForUser(userId: Int): Flow<List<GameEntity>>

    @Insert
    suspend fun insertWithdrawal(withdrawal: WithdrawEntity)

    @Query("SELECT * FROM withdrawals WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWithdrawalsForUser(userId: Int): Flow<List<WithdrawEntity>>

    @Query("SELECT * FROM withdrawals ORDER BY timestamp DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawEntity>>

    @Update
    suspend fun updateWithdrawal(withdrawal: WithdrawEntity)
}

@Database(entities = [UserEntity::class, GameEntity::class, WithdrawEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
