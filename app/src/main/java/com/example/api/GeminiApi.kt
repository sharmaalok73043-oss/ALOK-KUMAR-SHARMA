package com.example.api

import com.example.BuildConfig
import com.example.data.NeetQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApi {
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getAiExplanation(question: NeetQuestion): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key not configured correctly in Secrets Panel. Please set GEMINI_API_KEY."
        }

        val prompt = """
            You are 'NEET PYQ Guru', an expert science educator in India.
            Explain the following NEET question to a student in an easy-to-understand way using a combination of English and simple Hindi (Hinglish), which is the standard style for Indian students:
            
            Subject: ${question.subject}
            Chapter: ${question.chapter}
            Year: ${question.year}
            Question: ${question.questionText}
            Options:
            1. ${question.options.getOrNull(0) ?: ""}
            2. ${question.options.getOrNull(1) ?: ""}
            3. ${question.options.getOrNull(2) ?: ""}
            4. ${question.options.getOrNull(3) ?: ""}
            
            Correct Answer Option: Option ${question.correctOptionIndex + 1} (${question.options.getOrNull(question.correctOptionIndex) ?: ""})
            
            Provide:
            1. Why the selected option is correct.
            2. Why the other options are wrong.
            3. A simple memorization trick or mnemonic (yaad rakhne ka tareeqa).
            
            Keep the tone encouraging, fun, and extremely clear.
        """.trimIndent()

        try {
            // Construct request JSON manually to avoid additional compilation dependencies
            val partsObj = JSONObject().put("text", prompt)
            val partsArr = JSONArray().put(partsObj)
            val contentObj = JSONObject().put("parts", partsArr)
            val contentsArr = JSONArray().put(contentObj)
            val jsonBody = JSONObject().put("contents", contentsArr)

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            val url = "$BASE_URL?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext "Network Error: Server returned code ${response.code}. Please verify your API Key."
            }

            val bodyString = response.body?.string() ?: return@withContext "Empty response received from AI Guru."
            val responseJson = JSONObject(bodyString)
            val candidates = responseJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val part = parts?.optJSONObject(0)
            
            val textResult = part?.optString("text")
            if (!textResult.isNullOrBlank()) {
                textResult
            } else {
                "Guru was unable to generate explanation. Please try again."
            }
        } catch (e: Exception) {
            "An error occurred: ${e.message}"
        }
    }

    suspend fun getCustomDoubtAnswer(query: String, contextQuestion: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key not configured correctly in Secrets Panel. Please set GEMINI_API_KEY in Secrets."
        }

        val prompt = """
            You are 'NEET PYQ Guru', an inspiring, friendly science teacher.
            A student preparing for NEET the medical entrance exam has this question/doubt for you:
            
            DOUBT: "$query"
            ${if (contextQuestion != null) "CONTEXT_QUESTION: $contextQuestion" else ""}
            
            Please resolve their doubt in an easy, precise, high-yield way using simple Hinglish (Hindi + English mix).
            Incorporate:
            - A direct, easy explanation.
            - Real-life medical/biology links or standard formulas if applicable to Physics/Chemistry.
            - A quick mnemonics/trick (याद रखने की ट्रिक) to memorize the key facts.
            Keep the tone exciting and encouraging!
        """.trimIndent()

        try {
            val partsObj = JSONObject().put("text", prompt)
            val partsArr = JSONArray().put(partsObj)
            val contentObj = JSONObject().put("parts", partsArr)
            val contentsArr = JSONArray().put(contentObj)
            val jsonBody = JSONObject().put("contents", contentsArr)

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val url = "$BASE_URL?key=$apiKey"
            val request = Request.Builder().url(url).post(requestBody).build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext "Server returned helper code ${response.code}. Please ensure your API Key is verified."
            }

            val bodyString = response.body?.string() ?: return@withContext "Could not fetch content."
            val responseJson = JSONObject(bodyString)
            val textResult = responseJson.optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text")

            if (!textResult.isNullOrBlank()) {
                textResult
            } else {
                "Sorry! Guru could not generate an answer. Please rephrase."
            }
        } catch (e: Exception) {
            "An error occurred: ${e.message}"
        }
    }
}
