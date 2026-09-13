package com.example.data.repository

import com.example.data.local.UserPreferencesManager
import com.example.data.local.dao.CharacterDao
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.TaskDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.CharacterAiEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.ChatSessionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.local.entity.UserTaskEntity
import com.example.data.model.AiModel
import com.example.data.model.ChatMessage
import com.example.data.remote.MrALocalAiEngine
import com.example.data.remote.MultiModelCloudClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class MrARepository(
    private val chatDao: ChatDao,
    private val characterDao: CharacterDao,
    private val userDao: UserDao,
    private val taskDao: TaskDao,
    val preferencesManager: UserPreferencesManager,
    private val localEngine: MrALocalAiEngine = MrALocalAiEngine(),
    private val cloudClient: MultiModelCloudClient = MultiModelCloudClient()
) {

    // --- User Profiles ---
    companion object {
        const val MAX_PROFILES_LIMIT = 3
    }

    fun getAllProfiles(): Flow<List<UserProfileEntity>> = userDao.getAllProfiles()

    suspend fun getProfileCount(): Int = userDao.getProfileCount()

    fun observeActiveProfile(userId: Long): Flow<UserProfileEntity?> = userDao.observeProfileById(userId)

    suspend fun getProfileById(id: Long): UserProfileEntity? = userDao.getProfileById(id)

    suspend fun ensureDefaultProfile(): Long {
        val count = userDao.getProfileCount()
        if (count == 0) {
            val defaultUser = UserProfileEntity(
                username = "User (အဓိက)",
                bio = "Mr.A AI အသုံးပြုသူ",
                avatarEmoji = "👨‍💼",
                role = "Primary Account"
            )
            val id = userDao.insertProfile(defaultUser)
            preferencesManager.setActiveUserId(id)
            return id
        }
        return preferencesManager.activeUserId.value
    }

    suspend fun registerOrUpdateProfile(profile: UserProfileEntity): Long {
        return if (profile.id == 0L) {
            val count = userDao.getProfileCount()
            if (count >= MAX_PROFILES_LIMIT) {
                throw IllegalStateException("အကောင့် အများဆုံး ၃ ခုသာ ဖွင့်လှစ်ခွင့်ရှိပါသည်")
            }
            val newId = userDao.insertProfile(profile)
            preferencesManager.setActiveUserId(newId)
            preferencesManager.setLoggedIn(true)
            newId
        } else {
            userDao.updateProfile(profile)
            profile.id
        }
    }

    suspend fun login(userId: Long) {
        preferencesManager.setActiveUserId(userId)
        preferencesManager.setLoggedIn(true)
    }

    fun logout() {
        preferencesManager.setLoggedIn(false)
    }

    suspend fun switchProfile(userId: Long) {
        preferencesManager.setActiveUserId(userId)
        preferencesManager.setLoggedIn(true)
    }

    suspend fun deleteProfile(userId: Long) {
        userDao.deleteProfile(userId)
        val remaining = userDao.getAllProfiles().firstOrNull() ?: emptyList()
        if (remaining.isNotEmpty()) {
            preferencesManager.setActiveUserId(remaining.first().id)
        } else {
            preferencesManager.setLoggedIn(false)
        }
    }

    // --- Chat Sessions & Messages ---
    fun getSessionsForUser(userId: Long): Flow<List<ChatSessionEntity>> =
        chatDao.getAllSessionsForUser(userId)

    fun observeSessions(userId: Long): Flow<List<ChatSessionEntity>> =
        chatDao.getAllSessionsForUser(userId)

    fun getMessagesForSession(sessionId: Long): Flow<List<ChatMessage>> =
        chatDao.getMessagesForSession(sessionId).map { list ->
            list.map { it.toDomain() }
        }

    fun observeMessages(sessionId: Long): Flow<List<ChatMessage>> =
        getMessagesForSession(sessionId)

    suspend fun getSessionById(sessionId: Long): ChatSessionEntity? =
        chatDao.getSessionById(sessionId)

    suspend fun createNewSession(userId: Long, title: String = "စကားဝိုင်းသစ်"): Long {
        val session = ChatSessionEntity(
            userId = userId,
            title = title,
            createdAt = System.currentTimeMillis(),
            lastModifiedAt = System.currentTimeMillis()
        )
        return chatDao.insertSession(session)
    }

    suspend fun deleteSession(sessionId: Long) {
        chatDao.deleteSession(sessionId)
    }

    suspend fun renameSession(sessionId: Long, newTitle: String) {
        chatDao.renameSession(sessionId, newTitle)
    }

    suspend fun togglePinSession(sessionId: Long, currentPinned: Boolean) {
        chatDao.setSessionPinned(sessionId, !currentPinned)
    }

    suspend fun cleanupOldUnpinnedSessions(userId: Long, keepCount: Int = 10) {
        chatDao.cleanupOldUnpinnedSessions(userId, keepCount)
    }

    suspend fun getOrCreateLastSession(userId: Long): Long {
        val sessions = chatDao.getAllSessionsForUser(userId).firstOrNull() ?: emptyList()
        return if (sessions.isNotEmpty()) {
            sessions.first().id
        } else {
            createNewSession(userId)
        }
    }

    // --- Tasks Management ---
    fun getTasksForUser(userId: Long): Flow<List<UserTaskEntity>> =
        taskDao.getTasksForUser(userId)

    fun observeTasks(userId: Long): Flow<List<UserTaskEntity>> =
        taskDao.getTasksForUser(userId)

    suspend fun addTask(userId: Long, title: String, category: String): Long =
        taskDao.insertTask(UserTaskEntity(userId = userId, title = title, category = category))

    suspend fun addTask(task: UserTaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: UserTaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(id: Long) = taskDao.deleteTask(id)

    suspend fun toggleTaskCompleted(id: Long, completed: Boolean) =
        taskDao.setTaskCompleted(id, completed)

    suspend fun clearCompletedTasks(userId: Long) =
        taskDao.clearCompletedTasks(userId)

    // --- Character AI Management ---
    fun getAllCharacters(): Flow<List<CharacterAiEntity>> =
        characterDao.getAllCharacters()

    fun observeCharacters(): Flow<List<CharacterAiEntity>> =
        characterDao.getAllCharacters()

    fun getActiveCharacterFlow(): Flow<CharacterAiEntity?> =
        characterDao.getActiveCharacterFlow()

    suspend fun getActiveCharacter(): CharacterAiEntity? =
        characterDao.getActiveCharacter()

    suspend fun saveCharacter(character: CharacterAiEntity): Long {
        return if (character.id == 0L) {
            val count = characterDao.getCharacterCount()
            if (count >= 10) {
                throw IllegalStateException("Character အများဆုံး ၁၀ ခုသာ ဖန်တီးခွင့်ရှိပါသည်")
            }
            characterDao.insertCharacter(character)
        } else {
            characterDao.updateCharacter(character)
            character.id
        }
    }

    suspend fun createOrUpdateCharacter(character: CharacterAiEntity): Long =
        saveCharacter(character)

    suspend fun activateCharacter(id: Long) {
        characterDao.deactivateAll()
        characterDao.activateCharacter(id)
        preferencesManager.setActiveCharacterId(id)
    }

    suspend fun deactivateAllCharacters() {
        characterDao.deactivateAll()
        preferencesManager.setActiveCharacterId(null)
    }

    suspend fun setActiveCharacter(id: Long?) {
        if (id != null) {
            activateCharacter(id)
        } else {
            deactivateAllCharacters()
        }
    }

    suspend fun deleteCharacter(id: Long) {
        val active = characterDao.getActiveCharacter()
        if (active?.id == id) {
            preferencesManager.setActiveCharacterId(null)
        }
        characterDao.deleteCharacter(id)
    }

    // --- Sending Messages ---
    suspend fun sendMessage(
        sessionId: Long,
        userId: Long,
        userPrompt: String,
        targetModel: AiModel
    ): Result<ChatMessage> {
        val activeChar = characterDao.getActiveCharacter()

        // 1. Save User Message
        val userEntity = ChatMessageEntity(
            sessionId = sessionId,
            role = ChatMessage.Role.USER.name,
            content = userPrompt,
            modelName = targetModel.displayName,
            timestamp = System.currentTimeMillis(),
            isError = false
        )
        chatDao.insertMessage(userEntity)

        // Update session title if default
        updateSessionTitleIfNeeded(sessionId, userId, userPrompt)

        // 2. Generate response: If model is Cloud, use user's configured API key with MultiModelCloudClient; otherwise local engine
        val assistantResult: Result<String> = try {
            if (targetModel.isCloud) {
                val apiKey = preferencesManager.getApiKey(targetModel)
                if (apiKey.isBlank()) {
                    throw IllegalArgumentException("${targetModel.displayName} ကို အသုံးပြုရန် သင်၏ API Key ထည့်သွင်းပေးရန် လိုအပ်ပါသည် (API Key Required)။ Settings တွင် Key ထည့်နိုင်ပါသည်။")
                }
                val pastEntities = chatDao.getMessagesForSession(sessionId).firstOrNull() ?: emptyList()
                val pastHistory = pastEntities.map { it.toDomain() }
                val response = cloudClient.generateResponse(
                    model = targetModel,
                    apiKey = apiKey,
                    prompt = userPrompt,
                    history = pastHistory,
                    character = activeChar
                )
                Result.success(response)
            } else {
                val pastEntities = chatDao.getMessagesForSession(sessionId).firstOrNull() ?: emptyList()
                val pastHistory = pastEntities.map { it.toDomain() }
                val response = localEngine.generateResponse(
                    model = targetModel,
                    prompt = userPrompt,
                    history = pastHistory,
                    activeCharacter = activeChar
                )
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

        // Display name
        val displayModelName = if (activeChar != null) {
            "${activeChar.avatarEmoji} ${activeChar.name} (${targetModel.displayName})"
        } else {
            targetModel.displayName
        }

        return if (assistantResult.isSuccess) {
            val replyText = assistantResult.getOrNull() ?: "No response generated."
            val replyEntity = ChatMessageEntity(
                sessionId = sessionId,
                role = ChatMessage.Role.ASSISTANT.name,
                content = replyText,
                modelName = displayModelName,
                timestamp = System.currentTimeMillis(),
                isError = false
            )
            val msgId = chatDao.insertMessage(replyEntity)
            Result.success(replyEntity.copy(id = msgId).toDomain())
        } else {
            val errorMsg = assistantResult.exceptionOrNull()?.message ?: "An unexpected error occurred."
            val errorEntity = ChatMessageEntity(
                sessionId = sessionId,
                role = ChatMessage.Role.ASSISTANT.name,
                content = "⚠️ $errorMsg",
                modelName = displayModelName,
                timestamp = System.currentTimeMillis(),
                isError = true
            )
            val msgId = chatDao.insertMessage(errorEntity)
            Result.failure(assistantResult.exceptionOrNull() ?: Exception(errorMsg))
        }
    }

    private suspend fun updateSessionTitleIfNeeded(sessionId: Long, userId: Long, prompt: String) {
        val titleCandidate = if (prompt.length > 30) prompt.take(28) + "…" else prompt
        chatDao.renameSession(sessionId, titleCandidate)
    }

    suspend fun testApiKey(model: AiModel, apiKey: String): Result<Boolean> {
        return if (!model.isCloud) {
            Result.success(true)
        } else {
            cloudClient.testApiKey(model, apiKey).map { true }
        }
    }

    private fun ChatMessageEntity.toDomain(): ChatMessage {
        return ChatMessage(
            id = id,
            sessionId = sessionId,
            role = when (role) {
                ChatMessage.Role.USER.name -> ChatMessage.Role.USER
                ChatMessage.Role.ASSISTANT.name -> ChatMessage.Role.ASSISTANT
                else -> ChatMessage.Role.SYSTEM
            },
            content = content,
            modelName = modelName,
            timestamp = timestamp,
            isError = isError
        )
    }
}
