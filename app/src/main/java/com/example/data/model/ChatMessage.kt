package com.example.data.model

data class ChatMessage(
    val id: Long = 0,
    val sessionId: Long,
    val role: Role,
    val content: String,
    val modelName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isError: Boolean = false
) {
    enum class Role {
        USER,
        ASSISTANT,
        SYSTEM
    }
}
