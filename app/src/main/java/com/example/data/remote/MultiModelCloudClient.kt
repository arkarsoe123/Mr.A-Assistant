package com.example.data.remote

import com.example.data.local.entity.CharacterAiEntity
import com.example.data.model.AiModel
import com.example.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MultiModelCloudClient(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
) {

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateResponse(
        model: AiModel,
        apiKey: String,
        prompt: String,
        history: List<ChatMessage>,
        character: CharacterAiEntity? = null
    ): String = withContext(Dispatchers.IO) {
        val trimmedKey = apiKey.trim()
        if (trimmedKey.isBlank()) {
            throw IllegalArgumentException("${model.displayName} အတွက် API Key မထည့်ရသေးပါ။ Settings တွင် API Key ထည့်သွင်းပေးပါ။")
        }

        when (model) {
            AiModel.GEMINI -> callGemini(trimmedKey, prompt, history, character)
            AiModel.CHAT_GPT -> callOpenAi(trimmedKey, prompt, history, character)
            AiModel.DEEPSEEK -> callDeepSeek(trimmedKey, prompt, history, character)
            AiModel.MR_A_LOCAL -> throw IllegalArgumentException("Mr.A is a local built-in engine")
        }
    }

    suspend fun testApiKey(model: AiModel, apiKey: String): Result<String> = withContext(Dispatchers.IO) {
        val trimmedKey = apiKey.trim()
        if (trimmedKey.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("API Key ကွက်လပ်ဖြစ်နေပါသည်"))
        }

        try {
            when (model) {
                AiModel.GEMINI -> {
                    val result = callGemini(trimmedKey, "Hi, test connection", emptyList(), null)
                    Result.success("Gemini API ချိတ်ဆက်မှု အောင်မြင်ပါသည်!")
                }
                AiModel.CHAT_GPT -> {
                    val result = callOpenAi(trimmedKey, "Hi, test connection", emptyList(), null)
                    Result.success("OpenAI ChatGPT ချိတ်ဆက်မှု အောင်မြင်ပါသည်!")
                }
                AiModel.DEEPSEEK -> {
                    val result = callDeepSeek(trimmedKey, "Hi, test connection", emptyList(), null)
                    Result.success("DeepSeek API ချိတ်ဆက်မှု အောင်မြင်ပါသည်!")
                }
                AiModel.MR_A_LOCAL -> Result.success("Mr.A Local အသင့်ရှိပါသည်")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun callGemini(
        apiKey: String,
        prompt: String,
        history: List<ChatMessage>,
        character: CharacterAiEntity?
    ): String {
        // Using Google Gemini standard generateContent REST endpoint
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val root = JSONObject()
        val contentsArray = JSONArray()

        // System Instruction if Character AI is active
        if (character != null) {
            val systemInstruction = JSONObject().apply {
                val parts = JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", "You are ${character.name}, ${character.personaStyle}. ${character.memoryData} ${character.strictnessRules} Always reply in ${character.languageStyle}.")
                    })
                }
                put("parts", parts)
            }
            root.put("systemInstruction", systemInstruction)
        }

        // Add recent conversation history (up to last 10 messages)
        val recentHistory = history.takeLast(10)
        for (msg in recentHistory) {
            val role = if (msg.role == ChatMessage.Role.USER) "user" else "model"
            val item = JSONObject().apply {
                put("role", role)
                val parts = JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", msg.content)
                    })
                }
                put("parts", parts)
            }
            contentsArray.put(item)
        }

        // Current message
        val current = JSONObject().apply {
            put("role", "user")
            val parts = JSONArray().apply {
                put(JSONObject().apply {
                    put("text", prompt)
                })
            }
            put("parts", parts)
        }
        contentsArray.put(current)

        root.put("contents", contentsArray)

        val request = Request.Builder()
            .url(url)
            .post(root.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                handleHttpError("Gemini", response.code, responseBody)
            }

            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return parts.getJSONObject(0).optString("text", "")
                }
            }
            throw IOException("Gemini မှ အဖြေထုတ်ပြန်ခြင်း မရှိပါ (Empty response from Gemini)")
        }
    }

    private fun callOpenAi(
        apiKey: String,
        prompt: String,
        history: List<ChatMessage>,
        character: CharacterAiEntity?
    ): String {
        val url = "https://api.openai.com/v1/chat/completions"

        val root = JSONObject()
        root.put("model", "gpt-4o-mini")

        val messagesArray = JSONArray()

        val systemPrompt = if (character != null) {
            "You are ${character.name}, ${character.personaStyle}. ${character.memoryData} ${character.strictnessRules} Always reply in ${character.languageStyle}."
        } else {
            "You are ChatGPT, a helpful AI assistant. Always communicate clearly, formatted with Markdown."
        }

        messagesArray.put(JSONObject().apply {
            put("role", "system")
            put("content", systemPrompt)
        })

        // Add recent conversation history (up to last 10 messages)
        val recentHistory = history.takeLast(10)
        for (msg in recentHistory) {
            val role = if (msg.role == ChatMessage.Role.USER) "user" else "assistant"
            messagesArray.put(JSONObject().apply {
                put("role", role)
                put("content", msg.content)
            })
        }

        // Current message
        messagesArray.put(JSONObject().apply {
            put("role", "user")
            put("content", prompt)
        })

        root.put("messages", messagesArray)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(root.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                handleHttpError("OpenAI ChatGPT", response.code, responseBody)
            }

            val json = JSONObject(responseBody)
            val choices = json.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val firstChoice = choices.getJSONObject(0)
                val msg = firstChoice.optJSONObject("message")
                return msg?.optString("content", "") ?: ""
            }
            throw IOException("ChatGPT မှ အဖြေထုတ်ပြန်ခြင်း မရှိပါ (Empty response from ChatGPT)")
        }
    }

    private fun callDeepSeek(
        apiKey: String,
        prompt: String,
        history: List<ChatMessage>,
        character: CharacterAiEntity?
    ): String {
        val url = "https://api.deepseek.com/chat/completions"

        val root = JSONObject()
        root.put("model", "deepseek-chat")

        val messagesArray = JSONArray()

        val systemPrompt = if (character != null) {
            "You are ${character.name}, ${character.personaStyle}. ${character.memoryData} ${character.strictnessRules} Always reply in ${character.languageStyle}."
        } else {
            "You are DeepSeek AI, an advanced reasoning and coding assistant. Always respond with clear structure, Markdown formatting, and precise explanations."
        }

        messagesArray.put(JSONObject().apply {
            put("role", "system")
            put("content", systemPrompt)
        })

        val recentHistory = history.takeLast(10)
        for (msg in recentHistory) {
            val role = if (msg.role == ChatMessage.Role.USER) "user" else "assistant"
            messagesArray.put(JSONObject().apply {
                put("role", role)
                put("content", msg.content)
            })
        }

        messagesArray.put(JSONObject().apply {
            put("role", "user")
            put("content", prompt)
        })

        root.put("messages", messagesArray)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(root.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                handleHttpError("DeepSeek", response.code, responseBody)
            }

            val json = JSONObject(responseBody)
            val choices = json.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val firstChoice = choices.getJSONObject(0)
                val msg = firstChoice.optJSONObject("message")
                return msg?.optString("content", "") ?: ""
            }
            throw IOException("DeepSeek မှ အဖြေထုတ်ပြန်ခြင်း မရှိပါ (Empty response from DeepSeek)")
        }
    }

    private fun handleHttpError(provider: String, code: Int, errorBody: String) {
        val parsedMsg = try {
            val json = JSONObject(errorBody)
            val errObj = json.optJSONObject("error")
            errObj?.optString("message", "") ?: ""
        } catch (_: Exception) {
            ""
        }

        when (code) {
            400, 401, 403 -> {
                throw IOException(
                    "$provider API Key မမှန်ကန်ပါ (HTTP $code: Invalid API Key)။\n" +
                    "ကျေးဇူးပြု၍ Settings သို့မဟုတ် Key ထည့်သွင်းသည့်နေရာတွင် သင်၏ $provider Key ကို ပြန်လည်စစ်ဆေးပါ။" +
                    if (parsedMsg.isNotBlank()) "\nအသေးစိတ်: $parsedMsg" else ""
                )
            }
            429 -> {
                throw IOException(
                    "$provider Quota သို့မဟုတ် Rate Limit ပြည့်သွားပါပြီ (HTTP 429)။\n" +
                    "သင်၏ အကောင့် limit ကို စစ်ဆေးပါ သို့မဟုတ် API Key မလိုသော Mr.A Local Engine ကို အသုံးပြုနိုင်ပါသည်။"
                )
            }
            500, 502, 503 -> {
                throw IOException(
                    "$provider Server ခေတ္တမအားလပ်ပါ (HTTP $code)။ ခဏအကြာမှ ထပ်မံကြိုးစားပါ သို့မဟုတ် Mr.A Local သို့ ပြောင်းသုံးပါ။"
                )
            }
            else -> {
                throw IOException(
                    "$provider တောင်းဆိုမှု မအောင်မြင်ပါ (HTTP $code)။ " +
                    if (parsedMsg.isNotBlank()) parsedMsg else errorBody
                )
            }
        }
    }
}
