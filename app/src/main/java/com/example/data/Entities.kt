package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val questionId: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val isBookmarked: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mock_attempts")
data class MockAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correctCount: Int,
    val totalCount: Int,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgress>>

    @Query("SELECT * FROM user_progress WHERE questionId = :questionId LIMIT 1")
    suspend fun getProgressForQuestion(questionId: String): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgress)

    @Query("UPDATE user_progress SET isBookmarked = :isBookmarked WHERE questionId = :questionId")
    suspend fun updateBookmark(questionId: String, isBookmarked: Boolean)

    @Query("DELETE FROM user_progress WHERE questionId = :questionId")
    suspend fun deleteProgress(questionId: String)

    @Query("SELECT * FROM mock_attempts ORDER BY timestamp DESC")
    fun getAllMockAttempts(): Flow<List<MockAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockAttempt(attempt: MockAttempt)

    @Query("DELETE FROM user_progress")
    suspend fun clearAllProgress()

    @Query("DELETE FROM mock_attempts")
    suspend fun clearAllMockAttempts()
}
