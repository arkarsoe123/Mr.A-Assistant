package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MrADatabase
import com.example.data.local.UserPreferencesManager
import com.example.data.local.entity.CharacterAiEntity
import com.example.data.local.entity.ChatSessionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.local.entity.UserTaskEntity
import com.example.data.model.AiModel
import com.example.data.model.ChatMessage
import com.example.data.repository.MrARepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

sealed interface TestKeyStatus {
    object Idle : TestKeyStatus
    object Testing : TestKeyStatus
    object Success : TestKeyStatus
    data class Error(val message: String) : TestKeyStatus
}

@OptIn(ExperimentalCoroutinesApi::class)
class MrAViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MrADatabase.getDatabase(application)
    val preferencesManager = UserPreferencesManager(application)
    val repository = MrARepository(
        chatDao = db.chatDao(),
        characterDao = db.characterDao(),
        userDao = db.userDao(),
        taskDao = db.taskDao(),
        preferencesManager = preferencesManager
    )

    val activeUserId: StateFlow<Long> = preferencesManager.activeUserId
    val isLoggedIn: StateFlow<Boolean> = preferencesManager.isLoggedIn
    val themeMode: StateFlow<com.example.data.local.AppThemeMode> = preferencesManager.themeMode
    val appLanguage: StateFlow<com.example.data.local.AppLanguage> = preferencesManager.appLanguage
    val maxProfilesLimit: Int = MrARepository.MAX_PROFILES_LIMIT

    // All Profiles on this device
    val profiles: StateFlow<List<UserProfileEntity>> = repository.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Profile
    val activeProfile: StateFlow<UserProfileEntity?> = activeUserId
        .flatMapLatest { uid -> repository.observeActiveProfile(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedModel: StateFlow<AiModel> = preferencesManager.selectedModel

    // Sessions for active user
    val sessions: StateFlow<List<ChatSessionEntity>> = activeUserId
        .flatMapLatest { uid -> repository.getSessionsForUser(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tasks for active user
    val userTasks: StateFlow<List<UserTaskEntity>> = activeUserId
        .flatMapLatest { uid -> repository.getTasksForUser(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSessionId = MutableStateFlow<Long?>(null)
    val currentSessionId: StateFlow<Long?> = _currentSessionId.asStateFlow()

    val currentMessages: StateFlow<List<ChatMessage>> = _currentSessionId
        .flatMapLatest { sessionId ->
            if (sessionId == null) flowOf(emptyList())
            else repository.getMessagesForSession(sessionId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Chat Storage Limit Warning
    val maxChatLimit = 20
    private val _showStorageLimitDialog = MutableStateFlow(false)
    val showStorageLimitDialog: StateFlow<Boolean> = _showStorageLimitDialog.asStateFlow()

    // Rename Chat Dialog State
    private val _sessionToRename = MutableStateFlow<ChatSessionEntity?>(null)
    val sessionToRename: StateFlow<ChatSessionEntity?> = _sessionToRename.asStateFlow()

    // Character AI States
    val characters: StateFlow<List<CharacterAiEntity>> = repository.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeCharacter: StateFlow<CharacterAiEntity?> = repository.getActiveCharacterFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _showCharacterDialog = MutableStateFlow(false)
    val showCharacterDialog: StateFlow<Boolean> = _showCharacterDialog.asStateFlow()

    private val _editingCharacter = MutableStateFlow<CharacterAiEntity?>(null)
    val editingCharacter: StateFlow<CharacterAiEntity?> = _editingCharacter.asStateFlow()

    // API Key Dialog States
    private val _showApiKeyDialog = MutableStateFlow(false)
    val showApiKeyDialog: StateFlow<Boolean> = _showApiKeyDialog.asStateFlow()

    private val _modelForApiKeyDialog = MutableStateFlow(AiModel.GEMINI)
    val modelForApiKeyDialog: StateFlow<AiModel> = _modelForApiKeyDialog.asStateFlow()

    private val _testKeyStatus = MutableStateFlow<TestKeyStatus>(TestKeyStatus.Idle)
    val testKeyStatus: StateFlow<TestKeyStatus> = _testKeyStatus.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        // Initialize TTS
        try {
            tts = TextToSpeech(application) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.ENGLISH
                }
            }
        } catch (_: Exception) {}

        // Ensure default profile exists
        viewModelScope.launch {
            repository.ensureDefaultProfile()
        }

        // Keep current session in sync with active user's session list
        viewModelScope.launch {
            sessions.collect { list ->
                if (list.isNotEmpty()) {
                    if (_currentSessionId.value == null || list.none { it.id == _currentSessionId.value }) {
                        _currentSessionId.value = list.first().id
                    }
                } else {
                    _currentSessionId.value = null
                }
            }
        }
    }

    // --- Profile & Account Management ---
    fun login(userId: Long) {
        viewModelScope.launch {
            repository.login(userId)
            _currentSessionId.value = null
            _snackbarMessage.value = "Login အောင်မြင်ပါသည်"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _snackbarMessage.value = "အကောင့်မှ ထွက်လိုက်ပါပြီ (Logged out)"
        }
    }

    fun registerNewProfile(
        profile: UserProfileEntity,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.registerOrUpdateProfile(profile)
                _snackbarMessage.value = "Account '${profile.username}' ဖွင့်လှစ်ပြီးပါပြီ"
                onSuccess()
            } catch (e: Exception) {
                val err = e.message ?: "Account ဖွင့်လှစ်ခြင်း မအောင်မြင်ပါ"
                _snackbarMessage.value = err
                onError(err)
            }
        }
    }

    fun saveProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            val id = repository.registerOrUpdateProfile(profile)
            _snackbarMessage.value = "Account '${profile.username}' သိမ်းဆည်းပြီးပါပြီ"
        }
    }

    fun switchProfile(userId: Long) {
        viewModelScope.launch {
            repository.switchProfile(userId)
            _currentSessionId.value = null // reset session to active profile's latest
            _snackbarMessage.value = "Account ပြောင်းလဲလိုက်ပါပြီ"
        }
    }

    fun deleteProfile(userId: Long) {
        viewModelScope.launch {
            repository.deleteProfile(userId)
            _snackbarMessage.value = "Account ဖျက်ပစ်လိုက်ပါပြီ"
        }
    }

    // --- User Custom Tasks & Routines ---
    fun addTask(title: String, category: String) {
        viewModelScope.launch {
            repository.addTask(activeUserId.value, title, category)
            _snackbarMessage.value = "လုပ်ငန်းဆောင်တာ ထည့်သွင်းပြီးပါပြီ"
        }
    }

    fun toggleTask(taskId: Long, currentCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(taskId, currentCompleted)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.clearCompletedTasks(activeUserId.value)
            _snackbarMessage.value = "ပြီးစီးသည်များကို ဖျက်ပြီးပါပြီ"
        }
    }

    // --- Chat Session Operations ---
    fun selectSession(sessionId: Long) {
        _currentSessionId.value = sessionId
    }

    fun startNewChat() {
        viewModelScope.launch {
            val currentList = sessions.value
            // Check storage limit warning
            if (currentList.size >= maxChatLimit) {
                _showStorageLimitDialog.value = true
                return@launch
            }
            val newId = repository.createNewSession(activeUserId.value, "စကားဝိုင်း အသစ်")
            _currentSessionId.value = newId
        }
    }

    fun openRenameSessionDialog(session: ChatSessionEntity) {
        _sessionToRename.value = session
    }

    fun closeRenameSessionDialog() {
        _sessionToRename.value = null
    }

    fun confirmRenameSession(newTitle: String) {
        val target = _sessionToRename.value ?: return
        viewModelScope.launch {
            repository.renameSession(target.id, newTitle)
            _sessionToRename.value = null
            _snackbarMessage.value = "ခေါင်းစဉ် ပြောင်းလဲပြီးပါပြီ"
        }
    }

    fun togglePinSession(session: ChatSessionEntity) {
        viewModelScope.launch {
            repository.togglePinSession(session.id, session.isPinned)
            val msg = if (!session.isPinned) "Pin ထိုးထားပါပြီ" else "Pin ဖြုတ်လိုက်ပါပြီ"
            _snackbarMessage.value = msg
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            if (_currentSessionId.value == sessionId) {
                _currentSessionId.value = null
            }
            _snackbarMessage.value = "စကားဝိုင်း ဖျက်ပြီးပါပြီ"
        }
    }

    fun openStorageLimitDialog() {
        _showStorageLimitDialog.value = true
    }

    fun closeStorageLimitDialog() {
        _showStorageLimitDialog.value = false
    }

    fun cleanupOldUnpinnedSessions() {
        viewModelScope.launch {
            repository.cleanupOldUnpinnedSessions(activeUserId.value, keepCount = 10)
            _showStorageLimitDialog.value = false
            _snackbarMessage.value = "အသုံးမပြုသော စကားဝိုင်းဟောင်းများကို ရှင်းလင်းပြီးပါပြီ"
        }
    }

    // --- Model Selection ---
    fun selectModel(model: AiModel) {
        preferencesManager.setSelectedModel(model)
    }

    fun openApiKeyDialogForModel(model: AiModel) {
        _modelForApiKeyDialog.value = model
        _testKeyStatus.value = TestKeyStatus.Idle
        _showApiKeyDialog.value = true
    }

    fun setApiKeyDialogOpen(open: Boolean) {
        _showApiKeyDialog.value = open
        if (!open) {
            _testKeyStatus.value = TestKeyStatus.Idle
        }
    }

    fun saveApiKey(model: AiModel, newKey: String) {
        preferencesManager.saveApiKey(model, newKey)
        _showApiKeyDialog.value = false
        if (newKey.isNotBlank()) {
            preferencesManager.setSelectedModel(model)
            _snackbarMessage.value = "${model.displayName} API Key saved"
        } else {
            _snackbarMessage.value = "${model.displayName} API Key cleared"
        }
    }

    fun testApiKey(model: AiModel, testKey: String) {
        if (testKey.isBlank()) {
            _testKeyStatus.value = TestKeyStatus.Error("Please enter an API Key first")
            return
        }
        viewModelScope.launch {
            _testKeyStatus.value = TestKeyStatus.Testing
            val result = repository.testApiKey(model, testKey)
            if (result.isSuccess) {
                _testKeyStatus.value = TestKeyStatus.Success
            } else {
                val err = result.exceptionOrNull()?.message ?: "Validation failed"
                _testKeyStatus.value = TestKeyStatus.Error(err)
            }
        }
    }

    // Character AI Operations
    fun openCharacterCreator(character: CharacterAiEntity? = null) {
        _editingCharacter.value = character
        _showCharacterDialog.value = true
    }

    fun closeCharacterDialog() {
        _showCharacterDialog.value = false
        _editingCharacter.value = null
    }

    fun saveAndActivateCharacter(character: CharacterAiEntity) {
        viewModelScope.launch {
            val charId = repository.saveCharacter(character)
            repository.activateCharacter(charId)
            closeCharacterDialog()
            _snackbarMessage.value = "Character '${character.name}' activated!"
        }
    }

    fun activateCharacter(id: Long) {
        viewModelScope.launch {
            repository.activateCharacter(id)
            _snackbarMessage.value = "Character activated"
        }
    }

    fun resetToDefaultMrA() {
        viewModelScope.launch {
            repository.deactivateAllCharacters()
            _snackbarMessage.value = "Switched to Default Mr.A"
        }
    }

    fun deleteCharacter(id: Long) {
        viewModelScope.launch {
            repository.deleteCharacter(id)
            _snackbarMessage.value = "Character deleted"
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || _isGenerating.value) return

        val activeId = _currentSessionId.value
        val currentModel = selectedModel.value
        val userId = activeUserId.value

        viewModelScope.launch {
            val sessionId = activeId ?: repository.createNewSession(userId, "စကားဝိုင်း အသစ်").also {
                _currentSessionId.value = it
            }

            _isGenerating.value = true
            try {
                repository.sendMessage(sessionId, userId, trimmed, currentModel)
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun speakText(text: String) {
        val cleanText = text.replace(Regex("""[#\*`_~>]"""), "")
        tts?.stop()
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "mra_tts_${System.currentTimeMillis()}")
        _isSpeaking.value = true
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun setThemeMode(mode: com.example.data.local.AppThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    fun setAppLanguage(lang: com.example.data.local.AppLanguage) {
        preferencesManager.setAppLanguage(lang)
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
