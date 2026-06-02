package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiApi
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Subjects : Screen()
    data class Chapters(val subject: String) : Screen()
    data class QuestionSolve(val chapterName: String, val startIndex: Int = 0) : Screen()
    object MockDashboard : Screen()
    data class MockSolve(val questions: List<NeetQuestion>) : Screen()
    data class MockResult(val attempt: MockAttempt, val answers: Map<String, Int>) : Screen()
    object Stats : Screen()
    data class AiDoubtSolver(val question: NeetQuestion) : Screen()
}

class NeetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repo = UserProgressRepository(db.userProgressDao())

    // UI Navigation backstack history
    private val backStack = mutableListOf<Screen>()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // App Theme Customization: 0 = Classic Warm, 1 = Cosmic Night, 2 = Teal Ocean, 3 = Forest Moss
    private val _currentThemeIndex = MutableStateFlow(0)
    val currentThemeIndex: StateFlow<Int> = _currentThemeIndex.asStateFlow()

    fun selectTheme(index: Int) {
        _currentThemeIndex.value = index
        val sharedPref = getApplication<Application>().getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
        sharedPref.edit().putInt("theme_index", index).apply()
    }

    // Database observation flows
    private val _userProgress = MutableStateFlow<List<UserProgress>>(emptyList())
    val userProgress: StateFlow<List<UserProgress>> = _userProgress.asStateFlow()

    private val _mockAttemptsState = MutableStateFlow<List<MockAttempt>>(emptyList())
    val mockAttempts: StateFlow<List<MockAttempt>> = _mockAttemptsState.asStateFlow()

    // Practice Module State
    private val _activeQuestionList = MutableStateFlow<List<NeetQuestion>>(emptyList())
    val activeQuestionList: StateFlow<List<NeetQuestion>> = _activeQuestionList.asStateFlow()

    private val _currentPracticeIndex = MutableStateFlow(0)
    val currentPracticeIndex: StateFlow<Int> = _currentPracticeIndex.asStateFlow()

    // Mock Exam State
    private val _mockTimerSeconds = MutableStateFlow(300) // 5 minutes standard prototype exam
    val mockTimerSeconds: StateFlow<Int> = _mockTimerSeconds.asStateFlow()

    private val _mockSelectedOptions = MutableStateFlow<Map<String, Int>>(emptyMap()) // map of questionId -> selectedOptionIndex
    val mockSelectedOptions: StateFlow<Map<String, Int>> = _mockSelectedOptions.asStateFlow()

    private val _mockIsRunning = MutableStateFlow(false)
    val mockIsRunning: StateFlow<Boolean> = _mockIsRunning.asStateFlow()

    private var timerJob: Job? = null

    // AI explainer
    private val _aiExplanationText = MutableStateFlow<String>("")
    val aiExplanationText: StateFlow<String> = _aiExplanationText.asStateFlow()

    private val _aiExplanationLoading = MutableStateFlow(false)
    val aiExplanationLoading: StateFlow<Boolean> = _aiExplanationLoading.asStateFlow()

    // Conceptual Doubt solver state
    private val _chatDoubtResponse = MutableStateFlow<String>("")
    val chatDoubtResponse: StateFlow<String> = _chatDoubtResponse.asStateFlow()

    private val _chatDoubtLoading = MutableStateFlow(false)
    val chatDoubtLoading: StateFlow<Boolean> = _chatDoubtLoading.asStateFlow()

    fun askCustomDoubt(query: String, contextQuestion: String? = null) {
        _chatDoubtResponse.value = ""
        _chatDoubtLoading.value = true
        viewModelScope.launch {
            val response = com.example.api.GeminiApi.getCustomDoubtAnswer(query, contextQuestion)
            _chatDoubtResponse.value = response
            _chatDoubtLoading.value = false
        }
    }

    fun clearChatDoubt() {
        _chatDoubtResponse.value = ""
    }

    init {
        val sharedPref = application.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
        _currentThemeIndex.value = sharedPref.getInt("theme_index", 0)

        // Collect DB updates
        viewModelScope.launch {
            repo.allProgress.collect {
                _userProgress.value = it
            }
        }
        viewModelScope.launch {
            repo.allMockAttempts.collect {
                _mockAttemptsState.value = it
            }
        }
    }

    fun navigateTo(screen: Screen) {
        backStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
            return true
        }
        return false
    }

    // Progress calculations
    fun getChapterProgress(chapterName: String): Pair<Int, Int> {
        val totalQuestions = NeetQuestionBank.questions.count { it.chapter == chapterName }
        if (totalQuestions == 0) return 0 to 0
        
        val chapterQuestionIds = NeetQuestionBank.questions.filter { it.chapter == chapterName }.map { it.id }
        val completedCount = _userProgress.value.count { it.questionId in chapterQuestionIds }
        return completedCount to totalQuestions
    }

    fun getChapterPercentage(chapterName: String): Float {
        val (completed, total) = getChapterProgress(chapterName)
        if (total == 0) return 0f
        return completed.toFloat() / total.toFloat()
    }

    fun getSubjectProgress(subject: String): Pair<Int, Int> {
        val totalQuestions = NeetQuestionBank.questions.count { it.subject == subject }
        if (totalQuestions == 0) return 0 to 0
        
        val subjectQuestionIds = NeetQuestionBank.questions.filter { it.subject == subject }.map { it.id }
        val completedCount = _userProgress.value.count { it.questionId in subjectQuestionIds }
        return completedCount to totalQuestions
    }

    // Save answer selection
    fun savePracticeAnswer(questionId: String, selectedIndex: Int, isCorrect: Boolean) {
        viewModelScope.launch {
            val progress = UserProgress(
                questionId = questionId,
                selectedOptionIndex = selectedIndex,
                isCorrect = isCorrect,
                isBookmarked = false
            )
            repo.saveProgress(progress)
        }
    }

    fun toggleBookmark(questionId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            repo.updateBookmark(questionId, isBookmarked)
        }
    }

    // Reset Data
    fun clearAllUserData() {
        viewModelScope.launch {
            repo.clearAllData()
            _mockSelectedOptions.value = emptyMap()
        }
    }

    // Practice screen helpers
    fun startChapterPractice(chapterName: String) {
        val questions = NeetQuestionBank.questions.filter { it.chapter == chapterName }
        _activeQuestionList.value = questions
        
        // Find first unsolved or start from first index
        val solvedIds = _userProgress.value.map { it.questionId }
        val firstUnsolved = questions.indexOfFirst { it.id !in solvedIds }
        _currentPracticeIndex.value = if (firstUnsolved == -1) 0 else firstUnsolved
        
        navigateTo(Screen.QuestionSolve(chapterName, _currentPracticeIndex.value))
    }

    fun setPracticeIndex(index: Int) {
        if (index in 0 until _activeQuestionList.value.size) {
            _currentPracticeIndex.value = index
        }
    }

    // Mock Methods
    fun startNewMockTest() {
        _mockIsRunning.value = true
        _mockTimerSeconds.value = 300 // 5 minutes
        _mockSelectedOptions.value = emptyMap()
        
        // Generate a mini Mock test containing 10 randomized high-yield questions
        val biologyQs = NeetQuestionBank.questions.filter { it.subject == "Biology" }.shuffled().take(4)
        val chemistryQs = NeetQuestionBank.questions.filter { it.subject == "Chemistry" }.shuffled().take(3)
        val physicsQs = NeetQuestionBank.questions.filter { it.subject == "Physics" }.shuffled().take(3)
        
        val mockQuestions = (biologyQs + chemistryQs + physicsQs).shuffled()
        _activeQuestionList.value = mockQuestions
        _currentPracticeIndex.value = 0
        
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_mockTimerSeconds.value > 0 && _mockIsRunning.value) {
                delay(1000)
                _mockTimerSeconds.value -= 1
            }
            if (_mockIsRunning.value) {
                submitMockTest()
            }
        }
        
        navigateTo(Screen.MockSolve(mockQuestions))
    }

    fun selectMockOption(questionId: String, optionIndex: Int) {
        val nextMap = _mockSelectedOptions.value.toMutableMap()
        nextMap[questionId] = optionIndex
        _mockSelectedOptions.value = nextMap
    }

    fun submitMockTest() {
        _mockIsRunning.value = false
        timerJob?.cancel()
        
        val mockQs = _activeQuestionList.value
        val answers = _mockSelectedOptions.value
        
        var correctCount = 0
        mockQs.forEach { q ->
            val userAns = answers[q.id]
            if (userAns != null && userAns == q.correctOptionIndex) {
                correctCount++
            }
        }

        val totalTime = 300
        val timeSpent = totalTime - _mockTimerSeconds.value

        val attempt = MockAttempt(
            correctCount = correctCount,
            totalCount = mockQs.size,
            timeSpentSeconds = timeSpent,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repo.saveMockAttempt(attempt)
            // also update progress for correct answers
            mockQs.forEach { q ->
                val selectedIdx = answers[q.id]
                if (selectedIdx != null) {
                    val progress = UserProgress(
                        questionId = q.id,
                        selectedOptionIndex = selectedIdx,
                        isCorrect = selectedIdx == q.correctOptionIndex,
                        isBookmarked = false
                    )
                    repo.saveProgress(progress)
                }
            }
        }

        navigateTo(Screen.MockResult(attempt, answers))
    }

    // AI Guru Explainer
    fun requestAiGuruExplanation(question: NeetQuestion) {
        _aiExplanationText.value = ""
        _aiExplanationLoading.value = true
        navigateTo(Screen.AiDoubtSolver(question))
        viewModelScope.launch {
            val expl = GeminiApi.getAiExplanation(question)
            _aiExplanationText.value = expl
            _aiExplanationLoading.value = false
        }
    }
}
