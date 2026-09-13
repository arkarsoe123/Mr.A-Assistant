package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode(val id: String, val titleEn: String, val titleMy: String) {
    LIGHT("light", "Light Mode", "အလင်းစနစ်"),
    DARK("dark", "Dark Mode", "အမှောင်စနစ်"),
    SYSTEM("system", "Device System", "စနစ်အတိုင်း");

    companion object {
        fun fromId(id: String): AppThemeMode = entries.firstOrNull { it.id == id } ?: SYSTEM
    }
}

enum class AppLanguage(val code: String, val titleEn: String, val titleMy: String) {
    ENGLISH("en", "English", "အင်္ဂလိပ်"),
    MYANMAR("my", "Myanmar (Burmese)", "မြန်မာစာ");

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code } ?: ENGLISH
    }
}

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mra_preferences", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _appLanguage = MutableStateFlow(loadAppLanguage())
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _selectedModel = MutableStateFlow(loadSelectedModel())
    val selectedModel: StateFlow<AiModel> = _selectedModel.asStateFlow()

    private val _geminiApiKey = MutableStateFlow(loadApiKey(KEY_GEMINI_API_KEY))
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _openAiApiKey = MutableStateFlow(loadApiKey(KEY_OPENAI_API_KEY))
    val openAiApiKey: StateFlow<String> = _openAiApiKey.asStateFlow()

    private val _deepseekApiKey = MutableStateFlow(loadApiKey(KEY_DEEPSEEK_API_KEY))
    val deepseekApiKey: StateFlow<String> = _deepseekApiKey.asStateFlow()

    private val _activeCharacterId = MutableStateFlow(loadActiveCharacterId())
    val activeCharacterId: StateFlow<Long?> = _activeCharacterId.asStateFlow()

    private val _activeUserId = MutableStateFlow(loadActiveUserId())
    val activeUserId: StateFlow<Long> = _activeUserId.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(loadIsLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private fun loadActiveUserId(): Long {
        return prefs.getLong(KEY_ACTIVE_USER_ID, -1L)
    }

    private fun loadThemeMode(): AppThemeMode {
        val id = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.id) ?: AppThemeMode.SYSTEM.id
        return AppThemeMode.fromId(id)
    }

    private fun loadAppLanguage(): AppLanguage {
        // Default language is English as requested
        val code = prefs.getString(KEY_APP_LANGUAGE, AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        return AppLanguage.fromCode(code)
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.id).apply()
        _themeMode.value = mode
    }

    fun setAppLanguage(lang: AppLanguage) {
        prefs.edit().putString(KEY_APP_LANGUAGE, lang.code).apply()
        _appLanguage.value = lang
    }

    private fun loadIsLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
        _isLoggedIn.value = loggedIn
    }

    fun setActiveUserId(userId: Long) {
        prefs.edit().putLong(KEY_ACTIVE_USER_ID, userId).apply()
        _activeUserId.value = userId
    }

    private fun loadSelectedModel(): AiModel {
        // Default model is Mr.A local offline mode
        val id = prefs.getString(KEY_SELECTED_MODEL, AiModel.MR_A_LOCAL.id) ?: AiModel.MR_A_LOCAL.id
        return AiModel.fromId(id)
    }

    private fun loadApiKey(prefKey: String): String {
        return prefs.getString(prefKey, "") ?: ""
    }

    private fun loadActiveCharacterId(): Long? {
        val id = prefs.getLong(KEY_ACTIVE_CHARACTER_ID, -1L)
        return if (id >= 0) id else null
    }

    fun setSelectedModel(model: AiModel) {
        prefs.edit().putString(KEY_SELECTED_MODEL, model.id).apply()
        _selectedModel.value = model
    }

    fun setActiveCharacterId(id: Long?) {
        if (id == null) {
            prefs.edit().remove(KEY_ACTIVE_CHARACTER_ID).apply()
            _activeCharacterId.value = null
        } else {
            prefs.edit().putLong(KEY_ACTIVE_CHARACTER_ID, id).apply()
            _activeCharacterId.value = id
        }
    }

    fun getApiKey(model: AiModel): String {
        return when (model) {
            AiModel.GEMINI -> _geminiApiKey.value.trim()
            AiModel.CHAT_GPT -> _openAiApiKey.value.trim()
            AiModel.DEEPSEEK -> _deepseekApiKey.value.trim()
            AiModel.MR_A_LOCAL -> ""
        }
    }

    fun saveApiKey(model: AiModel, key: String) {
        val trimmed = key.trim()
        when (model) {
            AiModel.GEMINI -> {
                prefs.edit().putString(KEY_GEMINI_API_KEY, trimmed).apply()
                _geminiApiKey.value = trimmed
            }
            AiModel.CHAT_GPT -> {
                prefs.edit().putString(KEY_OPENAI_API_KEY, trimmed).apply()
                _openAiApiKey.value = trimmed
            }
            AiModel.DEEPSEEK -> {
                prefs.edit().putString(KEY_DEEPSEEK_API_KEY, trimmed).apply()
                _deepseekApiKey.value = trimmed
            }
            AiModel.MR_A_LOCAL -> { /* Built-in needs no key */ }
        }
    }

    fun hasApiKey(model: AiModel): Boolean {
        if (!model.isCloud) return true
        return getApiKey(model).isNotBlank()
    }

    companion object {
        private const val KEY_SELECTED_MODEL = "selected_model_id"
        private const val KEY_GEMINI_API_KEY = "user_gemini_api_key"
        private const val KEY_OPENAI_API_KEY = "user_openai_api_key"
        private const val KEY_DEEPSEEK_API_KEY = "user_deepseek_api_key"
        private const val KEY_ACTIVE_CHARACTER_ID = "active_character_id"
        private const val KEY_ACTIVE_USER_ID = "active_user_id"
        private const val KEY_IS_LOGGED_IN = "is_user_logged_in"
        private const val KEY_THEME_MODE = "app_theme_mode"
        private const val KEY_APP_LANGUAGE = "app_language"
    }
}
