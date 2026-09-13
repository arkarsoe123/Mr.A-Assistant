package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val bio: String = "",
    val avatarEmoji: String = "👤",
    val pinCode: String = "", // Optional PIN for family privacy
    val role: String = "Member", // e.g. "Primary", "Family Member", "Kid"
    val createdAt: Long = System.currentTimeMillis()
)
