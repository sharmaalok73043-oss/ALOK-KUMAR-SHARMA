package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun NeetApp(viewModel: NeetViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val progressList by viewModel.userProgress.collectAsStateWithLifecycle()
    val mockAttemptsList by viewModel.mockAttempts.collectAsStateWithLifecycle()

    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundBeige,
        bottomBar = {
            if (currentScreen is Screen.Home || 
                currentScreen is Screen.Subjects || 
                currentScreen is Screen.MockDashboard || 
                currentScreen is Screen.Stats
            ) {
                NeetBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(viewModel = viewModel, progressList = progressList)
                }
                is Screen.Subjects -> {
                    SubjectsLibraryOverview(viewModel = viewModel, progressList = progressList)
                }
                is Screen.Chapters -> {
                    ChaptersListScreen(viewModel = viewModel, subject = screen.subject, progressList = progressList)
                }
                is Screen.QuestionSolve -> {
                    PracticeSolvingScreen(viewModel = viewModel, chapterName = screen.chapterName)
                }
                is Screen.MockDashboard -> {
                    MocksDashboardScreen(viewModel = viewModel, attemptsList = mockAttemptsList)
                }
                is Screen.MockSolve -> {
                    MockTestScreen(viewModel = viewModel, questions = screen.questions)
                }
                is Screen.MockResult -> {
                    MockResultScreen(viewModel = viewModel, attempt = screen.attempt, answers = screen.answers)
                }
                is Screen.Stats -> {
                    StatsScreen(viewModel = viewModel, progressList = progressList, attemptsList = mockAttemptsList)
                }
                is Screen.AiDoubtSolver -> {
                    AiDoubtRoomScreen(viewModel = viewModel, question = screen.question)
                }
            }
        }
    }
}

@Composable
fun NeetBottomNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(width = 1.dp, color = SoftCreamSurface, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        val tabs = listOf(
            Triple(Screen.Home, "Home", "🏠"),
            Triple(Screen.Subjects, "Chapters", "📜"),
            Triple(Screen.MockDashboard, "Mocks", "🏆"),
            Triple(Screen.Stats, "Stats", "📊")
        )

        tabs.forEach { (screenTarget, label, emoji) ->
            val isSelected = when (screenTarget) {
                is Screen.Home -> currentScreen is Screen.Home
                is Screen.Subjects -> currentScreen is Screen.Subjects || currentScreen is Screen.Chapters || currentScreen is Screen.QuestionSolve
                is Screen.MockDashboard -> currentScreen is Screen.MockDashboard || currentScreen is Screen.MockSolve || currentScreen is Screen.MockResult
                is Screen.Stats -> currentScreen is Screen.Stats
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screenTarget) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PastelPeach else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextDeepPrivate else TextMutedGray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_${label.lowercase()}")
            )
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: NeetViewModel,
    progressList: List<UserProgress>
) {
    val totalAvailableQs = NeetQuestionBank.questions.size
    val solvedQsCount = progressList.filter { it.selectedOptionIndex >= 0 }.size
    val progressPercent = if (totalAvailableQs > 0) (solvedQsCount * 100 / totalAvailableQs) else 0

    var fastDoubtText by remember { mutableStateOf("") }
    val chatResponse by viewModel.chatDoubtResponse.collectAsStateWithLifecycle()
    val chatLoading by viewModel.chatDoubtLoading.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PREPPING FOR AIIMS & NEET",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp,
                        color = TextMutedGray
                    )
                    Text(
                        text = "Hello, Aspirant",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDeepPrivate
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(LavenderAccent)
                        .border(2.dp, BorderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DR",
                        color = DeepViolet,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        item {
            val currentThemeIndex by viewModel.currentThemeIndex.collectAsStateWithLifecycle()
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Aesthetic Study Vibe",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDeepPrivate,
                    letterSpacing = 0.5.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val themes = listOf(
                        Triple(0, "Ivory", "🍦"),
                        Triple(1, "Cosmic", "🌌"),
                        Triple(2, "Teal", "🌊"),
                        Triple(3, "Moss", "🌿")
                    )
                    
                    themes.forEach { (index, label, emoji) ->
                        val isSelected = currentThemeIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryBrown else SoftCreamSurface)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) PrimaryBrown else PastelPeach,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { viewModel.selectTheme(index) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = emoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else TextDeepPrivate
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(PrimaryBrown)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Daily PYQ Goal",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$solvedQsCount / $totalAvailableQs solved",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction = (progressPercent.toFloat() / 100f).coerceIn(0f, 1f))
                                    .clip(CircleShape)
                                    .background(PastelPeach)
                            )
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    ) {
                        CircularProgressIndicator(
                            progress = { (progressPercent.toFloat() / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.size(64.dp),
                            color = PastelPeach,
                            strokeWidth = 5.dp,
                            trackColor = Color.White.copy(0.15f)
                        )
                        Text(
                            text = "$progressPercent%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subject Library",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDeepPrivate
                )
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBrown,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(SoftCreamSurface)
                        .clickable { viewModel.navigateTo(Screen.Subjects) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val bioAll = NeetQuestionBank.questions.filter { it.subject == "Biology" }
                    val bioSolved = progressList.filter { p -> bioAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(PastelPeach)
                            .clickable {
                                viewModel.navigateTo(Screen.Chapters("Biology"))
                            }
                            .padding(16.dp)
                            .testTag("subject_card_biology"),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧬", fontSize = 18.sp)
                            }
                            Column {
                                Text("Biology", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDeepPrivate)
                                Text("$bioSolved / ${bioAll.size} PYQs Done", fontSize = 11.sp, color = TextMutedGray)
                            }
                        }
                    }

                    val chemAll = NeetQuestionBank.questions.filter { it.subject == "Chemistry" }
                    val chemSolved = progressList.filter { p -> chemAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(SymmetricalBlue)
                            .clickable {
                                viewModel.navigateTo(Screen.Chapters("Chemistry"))
                            }
                            .padding(16.dp)
                            .testTag("subject_card_chemistry"),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧪", fontSize = 18.sp)
                            }
                            Column {
                                Text("Chemistry", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDeepPrivate)
                                Text("$chemSolved / ${chemAll.size} PYQs Done", fontSize = 11.sp, color = TextMutedGray)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val physAll = NeetQuestionBank.questions.filter { it.subject == "Physics" }
                    val physSolved = progressList.filter { p -> physAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(SoftCreamSurface)
                            .border(2.dp, PastelPeach, RoundedCornerShape(24.dp))
                            .clickable {
                                viewModel.navigateTo(Screen.Chapters("Physics"))
                            }
                            .padding(16.dp)
                            .testTag("subject_card_physics"),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧲", fontSize = 18.sp)
                            }
                            Column {
                                Text("Physics", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDeepPrivate)
                                Text("$physSolved / ${physAll.size} PYQs Done", fontSize = 11.sp, color = TextMutedGray)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(PrimaryBrown)
                            .clickable {
                                viewModel.navigateTo(Screen.MockDashboard)
                            }
                            .padding(16.dp)
                            .testTag("subject_card_mocks"),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⏱️", fontSize = 18.sp)
                            }
                            Column {
                                Text("Mock Test", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                                Text("10 Live Exams", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
        }

        item {
            val lastSolved = progressList.lastOrNull()
            val qResume = NeetQuestionBank.questions.find { it.id == lastSolved?.questionId } ?: NeetQuestionBank.questions.first()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(1.dp, PastelPeach, RoundedCornerShape(24.dp))
                    .clickable {
                        viewModel.startChapterPractice(qResume.chapter)
                    }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PastelPeach, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "P",
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBrown
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Resume Last Solve", fontSize = 11.sp, color = TextMutedGray)
                        Text(
                            text = "${qResume.chapter} • Q. ${qResume.id.substringAfter('_')}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDeepPrivate
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(PrimaryBrown, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Resume",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SoftCreamSurface)
                    .border(1.dp, PastelPeach, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🤖", fontSize = 20.sp)
                        Column {
                            Text("NEET Doubt Room", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDeepPrivate)
                            Text("Ask concepts in simple Hinglish!", fontSize = 11.sp, color = TextMutedGray)
                        }
                    }

                    TextField(
                        value = fastDoubtText,
                        onValueChange = { fastDoubtText = it },
                        placeholder = { Text("E.g: C4 plant pathway samjhao...", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .testTag("fast_doubt_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (fastDoubtText.isNotBlank()) {
                                viewModel.askCustomDoubt(fastDoubtText, null)
                                keyboardController?.hide()
                            }
                        })
                    )

                    Button(
                        onClick = {
                            if (fastDoubtText.isNotBlank()) {
                                viewModel.askCustomDoubt(fastDoubtText, null)
                                keyboardController?.hide()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ask_doubt_button")
                    ) {
                        Text("Ask AI Guru", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    if (chatLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryBrown, modifier = Modifier.size(24.dp))
                        }
                    } else if (chatResponse.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "AI GURU SOLUTION:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBrown
                                    )
                                    IconButton(
                                        onClick = { viewModel.clearChatDoubt() },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = chatResponse,
                                    fontSize = 13.sp,
                                    color = TextBodyDark,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SubjectsLibraryOverview(
    viewModel: NeetViewModel,
    progressList: List<UserProgress>
) {
    val subjects = listOf("Biology", "Chemistry", "Physics")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Subject Library",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDeepPrivate
        )
        Text(
            text = "Chapter-wise sorted NEET past year papers practice.",
            fontSize = 12.sp,
            color = TextMutedGray
        )
        Spacer(modifier = Modifier.height(24.dp))

        subjects.forEach { subject ->
            val allQs = NeetQuestionBank.questions.filter { it.subject == subject }
            val solved = progressList.filter { p -> allQs.any { it.id == p.questionId } && p.selectedOptionIndex >= 0 }.size
            val ratio = if (allQs.isNotEmpty()) (solved.toFloat() / allQs.size.toFloat()) else 0f

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { viewModel.navigateTo(Screen.Chapters(subject)) }
                    .testTag("subject_row_$subject"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = borderStrokeHelper(subject)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(subjectThemeColor(subject), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (subject) {
                                "Biology" -> "🧬"
                                "Chemistry" -> "🧪"
                                else -> "🧲"
                            },
                            fontSize = 24.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = subject,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextDeepPrivate
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$solved / ${allQs.size} Solved",
                                fontSize = 12.sp,
                                color = TextMutedGray
                            )
                            Text(
                                text = "${(ratio * 100).toInt()}% Done",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBrown
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { ratio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape),
                            color = if (subject == "Biology") PrimaryBrown else SymmetricalBlue,
                            trackColor = SoftCreamSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Chapters",
                        tint = TextMutedGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChaptersListScreen(
    viewModel: NeetViewModel,
    subject: String,
    progressList: List<UserProgress>
) {
    val allQuestions = NeetQuestionBank.questions.filter { it.subject == subject }
    val chapters = allQuestions.map { it.chapter }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, SoftCreamSurface, CircleShape)
                    .testTag("chapter_list_back")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDeepPrivate
                )
            }
            Column {
                Text(
                    text = subject,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDeepPrivate
                )
                Text(
                    text = "Pick a chapter to start PYQs",
                    fontSize = 12.sp,
                    color = TextMutedGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (chapters.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No chapters found.", color = TextMutedGray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(chapters) { chapter ->
                    val qInChap = allQuestions.filter { it.chapter == chapter }
                    val solvedInChap = progressList.filter { p -> qInChap.any { it.id == p.questionId } && p.selectedOptionIndex >= 0 }.size

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { viewModel.startChapterPractice(chapter) }
                            .testTag("chapter_card_$chapter"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, SoftCreamSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = chapter,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextDeepPrivate
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$solvedInChap Solved / ${qInChap.size} Total",
                                    fontSize = 12.sp,
                                    color = TextMutedGray
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SoftCreamSurface)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (solvedInChap == qInChap.size) "Complete" else "Solve",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (solvedInChap == qInChap.size) Color(0xFF2E7D32) else PrimaryBrown
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PracticeSolvingScreen(viewModel: NeetViewModel, chapterName: String) {
    val activeQuestionList by viewModel.activeQuestionList.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentPracticeIndex.collectAsStateWithLifecycle()
    val totalQs = activeQuestionList.size

    val activeQuestion = activeQuestionList.getOrNull(currentIndex)

    val progressList by viewModel.userProgress.collectAsStateWithLifecycle()
    val isBookmarked = activeQuestion?.let { q ->
         progressList.find { it.questionId == q.id }?.isBookmarked == true
    } ?: false

    val isSolvedInDb = activeQuestion?.let { q ->
         progressList.find { it.questionId == q.id }
    }

    var selectedOptionIndex by remember(activeQuestion?.id) { 
        mutableStateOf(isSolvedInDb?.selectedOptionIndex) 
    }
    var showExplanation by remember(activeQuestion?.id) { 
        mutableStateOf(isSolvedInDb != null) 
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, SoftCreamSurface, CircleShape)
                        .testTag("practice_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Column {
                    Text(text = chapterName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDeepPrivate)
                    Text(text = "Question ${currentIndex + 1} of $totalQs", fontSize = 11.sp, color = TextMutedGray)
                }
            }

            IconButton(
                onClick = { activeQuestion?.let { viewModel.toggleBookmark(it.id, !isBookmarked) } },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(
                        1.dp,
                        if (isBookmarked) PastelPeach else SoftCreamSurface,
                        CircleShape
                    )
                    .testTag("bookmark_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) PrimaryBrown else TextMutedGray.copy(alpha = 0.4f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeQuestion == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No questions found.", color = TextMutedGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(1.dp, SoftCreamSurface, RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NEET ${activeQuestion.year}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBrown,
                                modifier = Modifier
                                    .background(PastelPeach.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                            Text(
                                text = activeQuestion.subject,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMutedGray
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = activeQuestion.questionText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDeepPrivate,
                            lineHeight = 22.sp
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        activeQuestion.options.forEachIndexed { optIndex, optionText ->
                            val prefix = ('A' + optIndex).toString()
                            
                            val isChosen = (selectedOptionIndex == optIndex)
                            val isCorrectSol = (optIndex == activeQuestion.correctOptionIndex)

                            val borderCol = when {
                                showExplanation && isCorrectSol -> Color(0xFF2E7D32)
                                showExplanation && isChosen && !isCorrectSol -> Color(0xFFC62828)
                                isChosen -> PrimaryBrown
                                else -> SoftCreamSurface
                            }

                            val bgCol = when {
                                showExplanation && isCorrectSol -> Color(0xFFE8F5E9)
                                showExplanation && isChosen && !isCorrectSol -> Color(0xFFFFEBEE)
                                isChosen -> PastelPeach.copy(alpha = 0.3f)
                                else -> Color.White
                            }

                            val textCol = when {
                                showExplanation && isCorrectSol -> Color(0xFF1B5E20)
                                showExplanation && isChosen && !isCorrectSol -> Color(0xFFB71C1C)
                                else -> TextDeepPrivate
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(bgCol)
                                    .border(1.dp, borderCol, RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (!showExplanation) {
                                            selectedOptionIndex = optIndex
                                        }
                                    }
                                    .padding(16.dp)
                                    .testTag("option_${prefix.lowercase()}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            if (isChosen) PrimaryBrown else SoftCreamSurface,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = prefix,
                                        color = if (isChosen) Color.White else TextDeepPrivate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = optionText,
                                    fontSize = 14.sp,
                                    color = textCol,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                if (!showExplanation) {
                    item {
                        Button(
                            onClick = { 
                                val chosen = selectedOptionIndex
                                if (chosen != null) {
                                    showExplanation = true
                                    val isCorrect = chosen == activeQuestion.correctOptionIndex
                                    viewModel.savePracticeAnswer(activeQuestion.id, chosen, isCorrect)
                                }
                            },
                            enabled = (selectedOptionIndex != null),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("submit_answer_button"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(text = "Check Answer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }

                if (showExplanation) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFF1F8E9))
                                .border(1.dp, Color(0xFFDCEDC8), RoundedCornerShape(24.dp))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "CORRECT ANSWER: ${('A' + activeQuestion.correctOptionIndex)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = activeQuestion.explanation,
                                fontSize = 13.sp,
                                color = Color(0xFF33691E),
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { viewModel.requestAiGuruExplanation(activeQuestion) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("explanation_ai_button")
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🤖", fontSize = 16.sp)
                                    Text("Get AI Guru Deep Explanation", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.setPracticeIndex(currentIndex - 1) },
                    enabled = (currentIndex > 0),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelPeach,
                        disabledContainerColor = SoftCreamSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("previous_question_button")
                ) {
                    Text(text = "Prev", color = TextDeepPrivate, fontWeight = FontWeight.Bold)
                }

                if (showExplanation) {
                    Button(
                        onClick = { 
                            showExplanation = false
                            selectedOptionIndex = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftCreamSurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("retry_button")
                    ) {
                        Text(text = "Retry Q", color = TextDeepPrivate, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = { viewModel.setPracticeIndex(currentIndex + 1) },
                    enabled = (currentIndex + 1 < totalQs),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showExplanation) PrimaryBrown else PastelPeach,
                        disabledContainerColor = SoftCreamSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("next_question_button")
                ) {
                    Text(
                        text = "Next",
                        color = if (showExplanation) Color.White else TextDeepPrivate,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MocksDashboardScreen(
    viewModel: NeetViewModel,
    attemptsList: List<MockAttempt>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "NEET Mock Arena",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDeepPrivate
        )
        Text(
            text = "Time-bound, simulated full-syllabus mock sets standard for NEET.",
            fontSize = 12.sp,
            color = TextMutedGray
        )
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(PrimaryBrown)
                .border(2.dp, PastelPeach, RoundedCornerShape(32.dp))
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⏱️", fontSize = 22.sp)
                }
                Text(
                    text = "NEET Mini Past-Year Mock",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "A composite test comprising 10 high-impact questions sorted directly from Biology, Chemistry, and Physics. Timer is set to 5 minutes.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.startNewMockTest() },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("start_mock_button")
                ) {
                    Text(
                        text = "Start Mock Exam",
                        color = TextDeepPrivate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recent Simulated Scores",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDeepPrivate
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (attemptsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("📝", fontSize = 24.sp)
                    Text("No simulator test history yet.", fontSize = 13.sp, color = TextMutedGray)
                    Text("Take a mock above to track your level!", fontSize = 11.sp, color = TextMutedGray)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(attemptsList) { attempt ->
                    val accuracy = if (attempt.totalCount > 0) (attempt.correctCount * 100 / attempt.totalCount) else 0
                    val min = attempt.timeSpentSeconds / 60
                    val sec = attempt.timeSpentSeconds % 60
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, SoftCreamSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Practice Simulator Attempt",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextDeepPrivate
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Timer: ${min}m ${sec}s | Score: ${attempt.correctCount}/${attempt.totalCount}",
                                    fontSize = 12.sp,
                                    color = TextMutedGray
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (accuracy >= 70) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$accuracy%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (accuracy >= 70) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MockTestScreen(viewModel: NeetViewModel, questions: List<NeetQuestion>) {
    val activeIndex by viewModel.currentPracticeIndex.collectAsStateWithLifecycle()
    val mockAnswers by viewModel.mockSelectedOptions.collectAsStateWithLifecycle()
    val timeRemaining by viewModel.mockTimerSeconds.collectAsStateWithLifecycle()

    val currentQuestion = questions.getOrNull(activeIndex)

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "NEET SIMULATION EXAM",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBrown
                )
                Text(
                    text = "Aspirant Mode Live",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDeepPrivate
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(if (timeRemaining < 60) Color(0xFFD32F2F) else PrimaryBrown)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formattedTime,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (questions.isNotEmpty()) {
            val progressFraction = (activeIndex + 1).toFloat() / questions.size.toFloat()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${activeIndex + 1} of ${questions.size}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMutedGray
                )
                Text(
                    text = "${(progressFraction * 100).toInt()}% Done",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBrown
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = PrimaryBrown,
                trackColor = SoftCreamSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentQuestion == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading mock details...", color = TextMutedGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(1.dp, SoftCreamSurface, RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentQuestion.subject,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBrown,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Topic: ${currentQuestion.chapter}",
                                fontSize = 11.sp,
                                color = TextMutedGray
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentQuestion.questionText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDeepPrivate,
                            lineHeight = 22.sp
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        currentQuestion.options.forEachIndexed { optIndex, optionText ->
                            val prefix = ('A' + optIndex).toString()
                            val isChosen = mockAnswers[currentQuestion.id] == optIndex

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isChosen) PastelPeach.copy(alpha = 0.3f) else Color.White)
                                    .border(
                                        1.dp,
                                        if (isChosen) PrimaryBrown else SoftCreamSurface,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { viewModel.selectMockOption(currentQuestion.id, optIndex) }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            if (isChosen) PrimaryBrown else SoftCreamSurface,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = prefix,
                                        color = if (isChosen) Color.White else TextDeepPrivate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = optionText,
                                    fontSize = 14.sp,
                                    color = TextDeepPrivate,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.navigateBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = SoftCreamSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Quit", color = Color(0xFFC62828))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.setPracticeIndex(activeIndex - 1) },
                        enabled = (activeIndex > 0),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PastelPeach,
                            disabledContainerColor = SoftCreamSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Back", color = TextDeepPrivate)
                    }

                    if (activeIndex == questions.size - 1) {
                        Button(
                            onClick = { viewModel.submitMockTest() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("submit_mock_done")
                        ) {
                            Text(text = "Submit Test", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.setPracticeIndex(activeIndex + 1) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Next", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MockResultScreen(viewModel: NeetViewModel, attempt: MockAttempt, answers: Map<String, Int>) {
    val correct = attempt.correctCount
    val total = attempt.totalCount
    val accuracy = if (total > 0) (correct * 100 / total) else 0

    val timeSpent = attempt.timeSpentSeconds
    val min = timeSpent / 60
    val sec = timeSpent % 60

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(PastelPeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏆", fontSize = 44.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Mock Submitted!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDeepPrivate
            )
            Text(
                text = "Simulated test completed. Check your score metrics:",
                fontSize = 12.sp,
                color = TextMutedGray,
                textAlign = TextAlign.Center
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(PrimaryBrown)
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$correct / $total", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Score", fontSize = 12.sp, color = Color.White.copy(0.8f))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$accuracy%", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = PastelPeach)
                            Text(text = "Accuracy", fontSize = 12.sp, color = Color.White.copy(0.8f))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = String.format("%02d:%02d", min, sec), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Time Spent", fontSize = 12.sp, color = Color.White.copy(0.8f))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (accuracy >= 80) "Exceptional accuracy score! Ready for AIIMS."
                               else if (accuracy >= 50) "Fair prep. Kripya AI explainer se concepts review kijiye!"
                               else "Practice chapterwise questions to solidify bases.",
                        color = Color.White,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.navigateTo(Screen.Stats) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("dismiss_result_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Check Full Analytics", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatsScreen(
    viewModel: NeetViewModel,
    progressList: List<UserProgress>,
    attemptsList: List<MockAttempt>
) {
    var showResetDialog by remember { mutableStateOf(false) }

    val totalQuestions = NeetQuestionBank.questions.size
    val totalSolved = progressList.filter { it.selectedOptionIndex >= 0 }.size
    val correctCount = progressList.filter { it.isCorrect }.size

    val accuracy = if (totalSolved > 0) (correctCount * 100 / totalSolved) else 0

    val bioAll = NeetQuestionBank.questions.filter { it.subject == "Biology" }
    val bioSolved = progressList.filter { p -> bioAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
    val bioRatio = if (bioAll.isNotEmpty()) bioSolved.toFloat() / bioAll.size.toFloat() else 0f

    val chemAll = NeetQuestionBank.questions.filter { it.subject == "Chemistry" }
    val chemSolved = progressList.filter { p -> chemAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
    val chemRatio = if (chemAll.isNotEmpty()) chemSolved.toFloat() / chemAll.size.toFloat() else 0f

    val physAll = NeetQuestionBank.questions.filter { it.subject == "Physics" }
    val physSolved = progressList.filter { p -> physAll.any { it.id == p.questionId } && p.selectedOptionIndex >=0 }.size
    val physRatio = if (physAll.isNotEmpty()) physSolved.toFloat() / physAll.size.toFloat() else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Performance Tracker",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDeepPrivate
            )
            Text(
                text = "Real-time records synced directly from local SQLite Room storage.",
                fontSize = 12.sp,
                color = TextMutedGray
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(PrimaryBrown)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$totalSolved / $totalQuestions", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Total Solved", fontSize = 11.sp, color = Color.White.copy(0.8f))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$accuracy%", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PastelPeach)
                        Text(text = "Avg Accuracy", fontSize = 11.sp, color = Color.White.copy(0.8f))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${attemptsList.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Mocks Done", fontSize = 11.sp, color = Color.White.copy(0.8f))
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SoftCreamSurface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Subject Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDeepPrivate)

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "🧬 Biology", fontSize = 13.sp, color = TextDeepPrivate)
                            Text(text = "$bioSolved / ${bioAll.size} PYQs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { bioRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = PrimaryBrown,
                            trackColor = SoftCreamSurface
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "🧪 Chemistry", fontSize = 13.sp, color = TextDeepPrivate)
                            Text(text = "$chemSolved / ${chemAll.size} PYQs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { chemRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = SymmetricalBlue,
                            trackColor = SoftCreamSurface
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "🧲 Physics", fontSize = 13.sp, color = TextDeepPrivate)
                            Text(text = "$physSolved / ${physAll.size} PYQs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { physRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = PastelPeach,
                            trackColor = SoftCreamSurface
                        )
                    }
                }
            }
        }

        item {
            val bookmarkedIds = progressList.filter { it.isBookmarked }.map { it.questionId }
            val bookmarkedQs = NeetQuestionBank.questions.filter { bookmarkedIds.contains(it.id) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SoftCreamSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⭐ Bookmarked Questions (${bookmarkedQs.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDeepPrivate
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (bookmarkedQs.isEmpty()) {
                        Text(
                            text = "Aapne koi question star nahi kiya hai. Star icons in practice screen help collect difficult PYQs.",
                            fontSize = 12.sp,
                            color = TextMutedGray
                        )
                    } else {
                        bookmarkedQs.forEach { q ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        viewModel.startChapterPractice(q.chapter)
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = q.questionText, fontSize = 13.sp, maxLines = 1, color = TextDeepPrivate)
                                    Text(text = "Chapter: ${q.chapter}", fontSize = 11.sp, color = TextMutedGray)
                                }
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Starred",
                                    tint = PrimaryBrown,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            HorizontalDivider(color = SoftCreamSurface)
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { showResetDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_progress_button")
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Reset", tint = Color.White)
                    Text(text = "Reset All Progress", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(text = "Reset Progress?", color = TextDeepPrivate, fontWeight = FontWeight.Bold) },
            text = { Text(text = "Kya aap apna pura local mock history and chapter solving percentage reset karna chahte hain? Yeh choice revert nahi ki jaa sakti.", color = TextBodyDark) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllUserData()
                        showResetDialog = false
                    },
                    modifier = Modifier.testTag("reset_confirm")
                ) {
                    Text(text = "Confirm Reset", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false }
                ) {
                    Text(text = "Cancel", color = TextDeepPrivate)
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
fun AiDoubtRoomScreen(viewModel: NeetViewModel, question: NeetQuestion) {
    val aiExplanationText by viewModel.aiExplanationText.collectAsStateWithLifecycle()
    val aiExplanationLoading by viewModel.aiExplanationLoading.collectAsStateWithLifecycle()

    var customDoubtText by remember { mutableStateOf("") }
    val chatResponse by viewModel.chatDoubtResponse.collectAsStateWithLifecycle()
    val chatLoading by viewModel.chatDoubtLoading.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { 
                    viewModel.clearChatDoubt()
                    viewModel.navigateBack() 
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, SoftCreamSurface, CircleShape)
                    .testTag("ai_back_button")
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDeepPrivate)
            }
            Column {
                Text(text = "AI Guru Doubt Room", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDeepPrivate)
                Text(text = "Solving: Question ${question.id.substringAfter('_')}", fontSize = 11.sp, color = TextMutedGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(1.dp, SoftCreamSurface, RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "TARGET PYQ",
                        color = PrimaryBrown,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.questionText,
                        fontSize = 13.sp,
                        color = TextBodyDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Correct Option is: Option ${('A' + question.correctOptionIndex)}",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(SoftCreamSurface)
                        .border(1.dp, PastelPeach, RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "DEEP TUTOR EXPLANATION",
                        color = PrimaryBrown,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (aiExplanationLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryBrown)
                        }
                    } else {
                        Text(
                            text = aiExplanationText.ifEmpty { "Loading response from Guru..." },
                            fontSize = 13.sp,
                            color = TextBodyDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            if (!aiExplanationLoading) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(1.dp, SoftCreamSurface, RoundedCornerShape(24.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Aapka Specific Concept Doubt:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDeepPrivate
                        )

                        TextField(
                            value = customDoubtText,
                            onValueChange = { customDoubtText = it },
                            placeholder = { Text("E.g: Option C organic mechanism explain kijiye", fontSize = 12.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = SoftCreamSurface,
                                unfocusedContainerColor = SoftCreamSurface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .testTag("specific_doubt_input"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                if (customDoubtText.isNotBlank()) {
                                    viewModel.askCustomDoubt(customDoubtText, question.questionText)
                                    keyboardController?.hide()
                                }
                            })
                        )

                        Button(
                            onClick = {
                                if (customDoubtText.isNotBlank()) {
                                    viewModel.askCustomDoubt(customDoubtText, question.questionText)
                                    keyboardController?.hide()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("specific_doubt_submit_button")
                        ) {
                            Text("Ask Guru", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        if (chatLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = PrimaryBrown, modifier = Modifier.size(24.dp))
                            }
                        } else if (chatResponse.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SoftCreamSurface, RoundedCornerShape(14.dp))
                                    .border(1.dp, PastelPeach, RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = chatResponse,
                                    fontSize = 13.sp,
                                    color = TextBodyDark,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

object NEETUtils {
    fun getChapterQuestions(subject: String, chapter: String): List<NeetQuestion> {
        return NeetQuestionBank.questions.filter { it.subject == subject && it.chapter == chapter }
    }
}

@Composable
fun subjectThemeColor(subject: String): Color {
    return when (subject) {
        "Biology" -> PastelPeach
        "Chemistry" -> SymmetricalBlue
        else -> SoftCreamSurface
    }
}

@Composable
fun borderStrokeHelper(subject: String): BorderStroke {
    val color = when (subject) {
        "Biology" -> PrimaryBrown
        "Chemistry" -> SymmetricalBlue
        else -> PastelPeach
    }
    return BorderStroke(2.dp, color)
}
