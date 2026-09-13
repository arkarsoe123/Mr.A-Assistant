package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CharacterDao
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.TaskDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.CharacterAiEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.ChatSessionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.local.entity.UserTaskEntity

@Database(
    entities = [
        ChatMessageEntity::class,
        ChatSessionEntity::class,
        CharacterAiEntity::class,
        UserProfileEntity::class,
        UserTaskEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class MrADatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun characterDao(): CharacterDao
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: MrADatabase? = null

        fun getDatabase(context: Context): MrADatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MrADatabase::class.java,
                    "mra_assistant_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
