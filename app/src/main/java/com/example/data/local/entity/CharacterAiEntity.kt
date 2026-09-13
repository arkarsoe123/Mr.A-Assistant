package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_ais")
data class CharacterAiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarEmoji: String = "🤖",
    val personaStyle: String,
    val memoryData: String = "",
    val strictnessRules: String = "",
    val languageStyle: String = "မြန်မာစာ ဦးစားပေး",
    val greetingMessage: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
