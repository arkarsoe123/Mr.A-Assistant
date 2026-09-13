package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CharacterAiEntity
import com.example.data.model.AiModel
import com.example.ui.components.ApiKeyDialog
import com.example.ui.components.CharacterAiDialog
import com.example.ui.components.ChatDrawerContent
import com.example.ui.components.ChatMessageItem
import com.example.ui.components.ChatStorageLimitDialog
import com.example.ui.components.ModelAvatarBadge
import com.example.ui.components.ModelSelectorChip
import com.example.ui.components.RenameChatDialog
import com.example.ui.components.getModelBrandColor
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MrACyan
import com.example.ui.theme.MrAViolet
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.launch

enum class AppScreen {
    CHAT,
    PROFILE,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MrAHomeScreen(
    viewModel: MrAViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(AppScreen.CHAT) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val activeProfile by viewModel.activeProfile.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val currentSessionId by viewModel.currentSessionId.collectAsStateWithLifecycle()
    val messages by viewModel.currentMessages.collectAsStateWithLifecycle()
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()
    val activeCharacter by viewModel.activeCharacter.collectAsStateWithLifecycle()
    val characters by viewModel.characters.collectAsStateWithLifecycle()

    val sessionToRename by viewModel.sessionToRename.collectAsStateWithLifecycle()
    val showStorageLimitDialog by viewModel.showStorageLimitDialog.collectAsStateWithLifecycle()

    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val showCharacterDialog by viewModel.showCharacterDialog.collectAsStateWithLifecycle()
    val editingCharacter by viewModel.editingCharacter.collectAsStateWithLifecycle()
    val showApiKeyDialog by viewModel.showApiKeyDialog.collectAsStateWithLifecycle()
    val modelForApiKeyDialog by viewModel.modelForApiKeyDialog.collectAsStateWithLifecycle()
    val testKeyStatus by viewModel.testKeyStatus.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Handle snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle system back gesture
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }
    BackHandler(enabled = currentScreen != AppScreen.CHAT && !drawerState.isOpen) {
        currentScreen = AppScreen.CHAT
    }

    // Scroll to latest message on change
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Scroll to bottom when soft keyboard appears so latest message is visible
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (messages.isNotEmpty() && isImeVisible) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    when (currentScreen) {
        AppScreen.PROFILE -> {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppScreen.CHAT },
                onLaunchToolPrompt = { prompt ->
                    inputText = prompt
                    currentScreen = AppScreen.CHAT
                    viewModel.sendMessage(prompt)
                }
            )
        }
        AppScreen.SETTINGS -> {
            AppSettingsScreen(
                viewModel = viewModel,
                onBack = { currentScreen = AppScreen.CHAT }
            )
        }
        AppScreen.CHAT -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ChatDrawerContent(
                        activeProfile = activeProfile,
                        sessions = sessions,
                        currentSessionId = currentSessionId,
                        hasApiKey = true,
                        maxLimit = viewModel.maxChatLimit,
                        appLanguage = appLanguage,
                        onSelectSession = { viewModel.selectSession(it) },
                        onNewChat = { viewModel.startNewChat() },
                        onRenameSession = { viewModel.openRenameSessionDialog(it) },
                        onTogglePinSession = { viewModel.togglePinSession(it) },
                        onDeleteSession = { viewModel.deleteSession(it) },
                        onCleanUpOldSessions = { viewModel.cleanupOldUnpinnedSessions() },
                        onOpenProfile = { currentScreen = AppScreen.PROFILE },
                        onOpenSettings = { currentScreen = AppScreen.SETTINGS },
                        onCloseDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            ) {
                Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(MrACyan, MrAViolet))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (activeCharacter != null) {
                                    Text(
                                        text = activeCharacter?.avatarEmoji ?: "A",
                                        fontSize = 16.sp
                                    )
                                } else {
                                    Text(
                                        text = "A",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = activeCharacter?.name ?: "Mr.A",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    },
                    actions = {
                        // Model & Character Selector Chip
                        ModelSelectorChip(
                            selectedModel = selectedModel,
                            activeCharacter = activeCharacter,
                            characters = characters,
                            hasKeyForModel = { viewModel.preferencesManager.hasApiKey(it) },
                            onModelSelected = { viewModel.selectModel(it) },
                            onConfigureKey = { viewModel.openApiKeyDialogForModel(it) },
                            onCreateCharacter = { viewModel.openCharacterCreator() },
                            onSelectCharacter = { viewModel.activateCharacter(it) },
                            onEditCharacter = { viewModel.openCharacterCreator(it) },
                            onResetToDefault = { viewModel.resetToDefaultMrA() }
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // User Profile Quick Button
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .border(1.dp, MrACyan.copy(alpha = 0.6f), CircleShape)
                                .clickable { currentScreen = AppScreen.PROFILE },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = activeProfile?.avatarEmoji ?: "👤", fontSize = 16.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("menu_drawer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open navigation menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            contentWindowInsets = WindowInsets.statusBars,
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                        )
                    )
            ) {
                // Chat Messages or Welcome Screen
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (messages.isEmpty()) {
                        EmptyChatGreeting(
                            selectedModel = selectedModel,
                            activeCharacter = activeCharacter,
                            appLanguage = appLanguage,
                            onSuggestionClick = { prompt ->
                                inputText = prompt
                                viewModel.sendMessage(prompt)
                            },
                            onCreateCharacter = { viewModel.openCharacterCreator() }
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
                        ) {
                            items(messages, key = { it.id }) { message ->
                                ChatMessageItem(
                                    message = message,
                                    onSpeak = { viewModel.speakText(it) },
                                    userName = activeProfile?.username ?: "User",
                                    userAvatarEmoji = activeProfile?.avatarEmoji ?: "👤"
                                )
                            }

                            // Thinking indicator
                            if (isGenerating) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DarkSurfaceVariant)
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MrACyan
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "${activeCharacter?.name ?: selectedModel.displayName} is answering...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Input Bar
                ChatInputBar(
                    text = inputText,
                    activeName = activeCharacter?.name ?: "Mr.A",
                    appLanguage = appLanguage,
                    onTextChange = { inputText = it },
                    isGenerating = isGenerating,
                    onSend = {
                        if (inputText.isNotBlank()) {
                            if (selectedModel.isCloud && !viewModel.preferencesManager.hasApiKey(selectedModel)) {
                                viewModel.openApiKeyDialogForModel(selectedModel)
                            } else {
                                val promptToSend = inputText
                                inputText = ""
                                viewModel.sendMessage(promptToSend)
                            }
                        }
                    }
                )
            }
        }
    }
}
}

    // API Key Dialog (BYOK)
    if (showApiKeyDialog) {
        ApiKeyDialog(
            targetModel = modelForApiKeyDialog,
            currentKey = viewModel.preferencesManager.getApiKey(modelForApiKeyDialog),
            testStatus = testKeyStatus,
            onSaveKey = { model, key -> viewModel.saveApiKey(model, key) },
            onTestKey = { model, key -> viewModel.testApiKey(model, key) },
            onDismiss = { viewModel.setApiKeyDialogOpen(false) },
            onSelectLocalModel = { viewModel.selectModel(AiModel.MR_A_LOCAL) }
        )
    }

    // Rename Chat Dialog
    sessionToRename?.let { session ->
        RenameChatDialog(
            session = session,
            onConfirmRename = { newTitle ->
                viewModel.confirmRenameSession(newTitle)
            },
            onDismiss = { viewModel.closeRenameSessionDialog() }
        )
    }

    // Chat Storage Limit Warning Dialog
    if (showStorageLimitDialog) {
        ChatStorageLimitDialog(
            currentCount = sessions.size,
            maxLimit = viewModel.maxChatLimit,
            onCleanupOldChats = { viewModel.cleanupOldUnpinnedSessions() },
            onDismiss = { viewModel.closeStorageLimitDialog() }
        )
    }

    // Character AI Studio Dialog
    if (showCharacterDialog) {
        CharacterAiDialog(
            characterToEdit = editingCharacter,
            onSaveAndActivate = { viewModel.saveAndActivateCharacter(it) },
            onDelete = { viewModel.deleteCharacter(it) },
            onDismiss = { viewModel.closeCharacterDialog() }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmptyChatGreeting(
    selectedModel: AiModel,
    activeCharacter: CharacterAiEntity?,
    appLanguage: com.example.data.local.AppLanguage = com.example.data.local.AppLanguage.ENGLISH,
    onSuggestionClick: (String) -> Unit,
    onCreateCharacter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Authentic Model / Character Avatar Logo
        ModelAvatarBadge(
            model = selectedModel,
            characterEmoji = activeCharacter?.avatarEmoji,
            size = 64.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (activeCharacter != null) activeCharacter.name else "${selectedModel.displayName} Assistant",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (activeCharacter != null) {
            Text(
                text = "🎭 Active Character: ${activeCharacter.name} • Model: ${selectedModel.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MrACyan,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "စတိုင်: ${activeCharacter.personaStyle}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            Text(
                text = if (appLanguage == com.example.data.local.AppLanguage.MYANMAR)
                    "လက်ရှိမော်ဒယ်: ${selectedModel.displayName} (Built-in)"
                else
                    "Active Model: ${selectedModel.displayName} (Built-in)",
                style = MaterialTheme.typography.bodyMedium,
                color = getModelBrandColor(selectedModel),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (appLanguage == com.example.data.local.AppLanguage.MYANMAR)
                    "လူကြီးမင်းတို့၏ နေ့စဉ်ဘဝ၊ ရုံးလုပ်ငန်း၊ စာရေးသားခြင်း၊ ဘာသာပြန်နှင့် ဗဟုသုတလိုအပ်ချက်များကို ကူညီပေးမည့် Assistant ဖြစ်ပါသည်။"
                else
                    "A versatile smart assistant to empower your daily work, communication, writing, translation, and knowledge.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (appLanguage == com.example.data.local.AppLanguage.MYANMAR) "💡 အကြံပြု ခေါင်းစဉ်များ" else "💡 SUGGESTED TOPICS",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Practical, daily-life assistant suggestions based on language!
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = if (appLanguage == com.example.data.local.AppLanguage.MYANMAR) {
                listOf(
                    "✍️ ရုံးသုံး ခွင့်တိုင်စာ ရေးပေးပါ",
                    "🌐 အင်္ဂလိပ် - မြန်မာ ဘာသာပြန်ပေးပါ",
                    "💡 နေ့စဉ် ကျန်းမာရေး အကြံပြုချက်",
                    "📝 အလုပ်လျှောက်လွှာ အီးမေးလ် ရေးနည်း",
                    "🧮 ဝင်ငွေထွက်ငွေ စာရင်းစီမံနည်း",
                    "☕ စိတ်ဖိစီးမှု လျှော့ချနည်းများ"
                )
            } else {
                listOf(
                    "✍️ Draft a formal leave email",
                    "🌐 Translate English to Myanmar",
                    "💡 Daily health & wellness tips",
                    "📝 Job application cover letter",
                    "🧮 Budget & expense planning",
                    "☕ Stress relief techniques"
                )
            }

            suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = { onSuggestionClick(suggestion) },
                    label = { Text(suggestion, fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = DarkSurfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = DarkOutline.copy(alpha = 0.7f),
                        enabled = true
                    ),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    activeName: String,
    appLanguage: com.example.data.local.AppLanguage = com.example.data.local.AppLanguage.ENGLISH,
    onTextChange: (String) -> Unit,
    isGenerating: Boolean,
    onSend: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = if (appLanguage == com.example.data.local.AppLanguage.MYANMAR)
                            "$activeName အား မေးမြန်းပါ..."
                        else
                            "Ask $activeName anything...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { onTextChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear input",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isGenerating,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (text.isNotBlank() && !isGenerating)
                            Brush.linearGradient(listOf(MrACyan, MrAViolet))
                        else
                            Brush.linearGradient(listOf(DarkSurfaceVariant, DarkSurfaceVariant))
                    )
                    .testTag("send_message_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = if (text.isNotBlank() && !isGenerating) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
