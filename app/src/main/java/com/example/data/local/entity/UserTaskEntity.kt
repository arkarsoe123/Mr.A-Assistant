package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tasks")
data class UserTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val category: String = "General", // "General", "Work", "Family", "Health"
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
