package com.example.data

import kotlinx.coroutines.flow.Flow

class UserProgressRepository(private val userProgressDao: UserProgressDao) {

    val allProgress: Flow<List<UserProgress>> = userProgressDao.getAllProgress()
    val allMockAttempts: Flow<List<MockAttempt>> = userProgressDao.getAllMockAttempts()

    suspend fun getProgressForQuestion(questionId: String): UserProgress? {
        return userProgressDao.getProgressForQuestion(questionId)
    }

    suspend fun saveProgress(progress: UserProgress) {
        userProgressDao.saveProgress(progress)
    }

    suspend fun updateBookmark(questionId: String, isBookmarked: Boolean) {
        val existing = userProgressDao.getProgressForQuestion(questionId)
        if (existing != null) {
            userProgressDao.saveProgress(existing.copy(isBookmarked = isBookmarked))
        } else {
            userProgressDao.saveProgress(
                UserProgress(
                    questionId = questionId,
                    selectedOptionIndex = -1,
                    isCorrect = false,
                    isBookmarked = isBookmarked
                )
            )
        }
    }

    suspend fun clearAllData() {
        userProgressDao.clearAllProgress()
        userProgressDao.clearAllMockAttempts()
    }

    suspend fun saveMockAttempt(attempt: MockAttempt) {
        userProgressDao.insertMockAttempt(attempt)
    }
}
