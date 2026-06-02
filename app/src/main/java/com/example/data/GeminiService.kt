package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val api: GeminiApi = retrofit.create(GeminiApi::class.java)

    suspend fun getExplanation(questionText: String, optionsText: String, subject: String, chapter: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "Aapka Gemini API Key empty ya default placeholder hai. Please setup actual API key in Secrets Panel to talk with NEET AI Guru!"
        }

        val prompt = """
            Subject: $subject
            Chapter: $chapter
            Question: $questionText
            Options: $optionsText
            
            Is question ka full concept and safe steps explain kijiye aur samjhaiye ki correct option kaise aaya. 
            Keep it super simple and explain in friendly NEET Hinglish style (English + Hindi mix).
        """.trimIndent()

        val systemInstruction = """
            You are 'NEET Guru AI', an enthusiastic, high-scoring medical exam mentor.
            Your job is to explain tough mock topics simply in Hinglish (Hindi + English blend).
            Follow these:
            1. Use bullet points and clear headings with emojis.
            2. Break down the concept clearly.
            3. Point out common pitfalls/mistakes that NEET aspirants commit in this specific topic.
            4. Suggest a memory trick or formula tip if applicable.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemInstruction)))
        )

        return try {
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "AI Guru response create nahi kar paaye. Kripya badme prayas karein."
        } catch (e: Exception) {
            "Error standard: ${e.localizedMessage ?: "Network error occurs during resolving."}. Check internet and API key in secrets panel."
        }
    }

    suspend fun askCustomDoubt(userQuery: String, contextQuestion: String? = null): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "API Key configuration missing. Please add GEMINI_API_KEY in the Secrets panel."
        }

        val prompt = if (contextQuestion != null) {
            "Doubt about question: \"$contextQuestion\"\nMy Question/Doubt: $userQuery\nSamjhaiye details me."
        } else {
            "Query: $userQuery\nExplain this NEET topic concept in Hinglish."
        }

        val systemInstruction = "You are NEET Guru AI. Answer candidate doubts in friendly Hinglish, clarify their concept with clear formulas/diagrams and tips."

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemInstruction)))
        )

        return try {
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "No response from AI."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}. Please retry later."
        }
    }
}
