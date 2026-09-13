package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.ChatSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_sessions WHERE userId = :userId ORDER BY isPinned DESC, lastModifiedAt DESC")
    fun getAllSessionsForUser(userId: Long): Flow<List<ChatSessionEntity>>

    @Query("SELECT * FROM chat_sessions ORDER BY isPinned DESC, lastModifiedAt DESC")
    fun getAllSessions(): Flow<List<ChatSessionEntity>>

    @Query("SELECT COUNT(*) FROM chat_sessions WHERE userId = :userId")
    fun getSessionCountForUser(userId: Long): Flow<Int>

    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Long): ChatSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChatSessionEntity): Long

    @Update
    suspend fun updateSession(session: ChatSessionEntity)

    @Query("UPDATE chat_sessions SET title = :newTitle, lastModifiedAt = :modifiedAt WHERE id = :sessionId")
    suspend fun renameSession(sessionId: Long, newTitle: String, modifiedAt: Long = System.currentTimeMillis())

    @Query("UPDATE chat_sessions SET isPinned = :isPinned WHERE id = :sessionId")
    suspend fun setSessionPinned(sessionId: Long, isPinned: Boolean)

    @Query("DELETE FROM chat_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: Long)

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: Long)

    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: Long): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearAllMessages()

    // Delete unpinned old sessions for a user, keeping the latest N
    @Query("DELETE FROM chat_sessions WHERE userId = :userId AND isPinned = 0 AND id NOT IN (SELECT id FROM chat_sessions WHERE userId = :userId AND isPinned = 0 ORDER BY lastModifiedAt DESC LIMIT :keepCount)")
    suspend fun cleanupOldUnpinnedSessions(userId: Long, keepCount: Int)
}
