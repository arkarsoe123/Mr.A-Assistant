package com.example.data.model

enum class AiModel(
    val id: String,
    val displayName: String,
    val description: String,
    val isCloud: Boolean,
    val providerName: String,
    val iconEmoji: String
) {
    MR_A_LOCAL(
        id = "mra_local",
        displayName = "Mr.A",
        description = "Mr.A Smart Brain (Built-in Offline - Math, Code, Writing & Reasoning)",
        isCloud = false,
        providerName = "Mr.A",
        iconEmoji = "🤖"
    ),
    GEMINI(
        id = "gemini",
        displayName = "Gemini",
        description = "Google Gemini Intelligence (User API Key)",
        isCloud = true,
        providerName = "Google",
        iconEmoji = "✨"
    ),
    CHAT_GPT(
        id = "chatgpt",
        displayName = "ChatGPT",
        description = "OpenAI ChatGPT Assistant (User API Key)",
        isCloud = true,
        providerName = "OpenAI",
        iconEmoji = "🟢"
    ),
    DEEPSEEK(
        id = "deepseek",
        displayName = "DeepSeek",
        description = "DeepSeek Reasoning Engine (User API Key)",
        isCloud = true,
        providerName = "DeepSeek",
        iconEmoji = "🐋"
    );

    companion object {
        fun fromId(id: String): AiModel =
            entries.firstOrNull { it.id == id } ?: MR_A_LOCAL
    }
}

