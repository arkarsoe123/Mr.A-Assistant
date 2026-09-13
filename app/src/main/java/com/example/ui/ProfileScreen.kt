package com.example.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.UserProfileEntity
import com.example.data.local.entity.UserTaskEntity
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.SwitchAccountDialog
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MrACyan
import com.example.ui.theme.MrAViolet
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: MrAViewModel,
    onBack: () -> Unit,
    onLaunchToolPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeProfile by viewModel.activeProfile.collectAsStateWithLifecycle()
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()
    val tasks by viewModel.userTasks.collectAsStateWithLifecycle()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showSwitchAccountDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isRegisteringNew by remember { mutableStateOf(false) }

    var newTaskTitle by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }

    val categories = listOf("General", "Work", "Family", "Health")

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Profile & Tools",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = { showSwitchAccountDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwitchAccount,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MrACyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ပြောင်းရန် (${profiles.size}/3)",
                            fontSize = 11.sp,
                            color = MrACyan
                        )
                    }

                    IconButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.testTag("profile_logout_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: User Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MrACyan.copy(alpha = 0.5f), MrAViolet.copy(alpha = 0.5f))))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(MrACyan.copy(alpha = 0.3f), MrAViolet.copy(alpha = 0.1f))
                                    )
                                )
                                .border(2.dp, MrACyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = activeProfile?.avatarEmoji ?: "👤", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = activeProfile?.username ?: "User",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (!activeProfile?.bio.isNullOrBlank()) activeProfile?.bio!! else "Mr.A AI အသုံးပြုသူ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MrACyan.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = activeProfile?.role ?: "Primary",
                                    fontSize = 11.sp,
                                    color = MrACyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (!activeProfile?.pinCode.isNullOrBlank()) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF334155))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("🔒 PIN Protected", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    isRegisteringNew = false
                                    showEditProfileDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("edit_profile_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit Profile", fontSize = 13.sp)
                            }

                            val canAddProfile = profiles.size < 3
                            Button(
                                onClick = {
                                    if (canAddProfile) {
                                        isRegisteringNew = true
                                        showEditProfileDialog = true
                                    }
                                },
                                enabled = canAddProfile,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("register_profile_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (canAddProfile) MrACyan else DarkSurfaceVariant,
                                    disabledContainerColor = DarkSurfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = if (canAddProfile) Color.Black else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (canAddProfile) "Add Profile" else "Limit (3/3)",
                                    fontSize = 13.sp,
                                    color = if (canAddProfile) Color.Black else Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Logout button inside User Account settings
                        OutlinedButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("logout_profile_card_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "အကောင့်မှ ထွက်မည် (Logout)",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Section 2: Daily Life Quick Tools (နေ့စဉ်လူမှုဘဝ အထောက်အကူပြု Tools များ)
            item {
                Text(
                    text = "🛠️ DAILY LIFE ASSISTANT TOOLS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val quickTools = listOf(
                        DailyTool(
                            title = "ခွင့်တိုင်စာ ရေးဆွဲရန် (Leave Letter)",
                            description = "ရုံးသုံး၊ ကျောင်းသုံး ခွင့်တိုင်စာ ပုံစံအပြည့်အစုံ ရေးသားပေးရန်",
                            icon = Icons.Default.Email,
                            prompt = "ရုံးသုံး ခွင့်တိုင်စာ (ဆေးခွင့် / အရေးပေါ်ခွင့် / ရှောင်တခင်ခွင့်) ပုံစံတစ်ခု အပြည့်အစုံနှင့် လိုအပ်သည့် အချက်အလက်များ ဖြည့်စွက်နိုင်ရန် ရေးဆွဲပေးပါ"
                        ),
                        DailyTool(
                            title = "အင်္ဂလိပ် - မြန်မာ ဘာသာပြန်စနစ် (Translator)",
                            description = "နေ့စဉ်သုံး စကားပြောများနှင့် စာသားများကို တိကျစွာ ဘာသာပြန်ရန်",
                            icon = Icons.Default.Translate,
                            prompt = "ကျွန်တော် ပေးပို့မည့် အင်္ဂလိပ် သို့မဟုတ် မြန်မာ စာသားများကို သဘာဝကျကျ အဓိပ္ပာယ်ပြည့်စုံစွာ ဘာသာပြန်ပေးရန် အဆင်သင့်ဖြစ်ပါပြီ"
                        ),
                        DailyTool(
                            title = "နေ့စဉ် သုံးစွဲငွေ စာရင်းစီမံနည်း (Budget Planner)",
                            description = "မိသားစုဝင်ငွေ/ထွက်ငွေ ချိန်ညှိတွက်ချက်နည်းနှင့် ငွေစုနည်း",
                            icon = Icons.Default.Payments,
                            prompt = "နေ့စဉ် မိသားစု သုံးစွဲငွေ (Budget) စီမံခန့်ခွဲနည်းနှင့် မလိုအပ်သော ကုန်ကျစရိတ် လျှော့ချနည်း အကြံပြုချက် ဇယားဆွဲပေးပါ"
                        ),
                        DailyTool(
                            title = "ကျန်းမာရေးနှင့် စိတ်ဖိစီးမှု လျှော့ချခြင်း (Wellness)",
                            description = "နေ့စဉ် ကျန်းမာရေးအတွက် လေ့ကျင့်ခန်း၊ အိပ်စက်မှုနှင့် စိတ်ဖိစီးမှု ဖြေဖျောက်နည်း",
                            icon = Icons.Default.HealthAndSafety,
                            prompt = "နေ့စဉ် ကျန်းမာရေးနှင့် စိတ်လက်အပန်းပြေစေမည့် အလွယ်တကူ လုပ်ဆောင်နိုင်သော အကြံပြုချက် ၅ ချက် ပေးပါ"
                        )
                    )

                    quickTools.forEach { tool ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface)
                                .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
                                .clickable { onLaunchToolPrompt(tool.prompt) }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MrACyan.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tool.icon,
                                    contentDescription = null,
                                    tint = MrACyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tool.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = tool.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "သုံးမည် ❯",
                                fontSize = 12.sp,
                                color = MrACyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Section 3: User Custom Tasks & Routines (စိတ်ကြိုက် လုပ်ငန်းဆောင်တာ ကိစ္စများ)
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 MY DAILY TASKS & ROUTINES",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (tasks.any { it.isCompleted }) {
                        Text(
                            text = "ပြီးစီးသည်များ ဖျက်မည်",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable { viewModel.clearCompletedTasks() }
                        )
                    }
                }
            }

            // Task Progress
            item {
                val completedCount = tasks.count { it.isCompleted }
                val totalCount = tasks.size
                val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "လုပ်ဆောင်ပြီးစီးမှု: $completedCount / $totalCount",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (progress == 1f && totalCount > 0) SuccessGreen else MrACyan
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (progress == 1f && totalCount > 0) SuccessGreen else MrACyan,
                            trackColor = Color(0xFF1E293B)
                        )
                    }
                }
            }

            // Add New Task Input
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            placeholder = { Text("လုပ်ငန်းဆောင်တာ အသစ် ထည့်သွင်းပါ...") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("new_task_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newTaskTitle.isNotBlank()) {
                                    viewModel.addTask(newTaskTitle.trim(), selectedCategory)
                                    newTaskTitle = ""
                                }
                            },
                            enabled = newTaskTitle.isNotBlank(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                            modifier = Modifier.testTag("add_task_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        }
                    }

                    // Category selection
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { cat ->
                            val isSel = cat == selectedCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) MrACyan.copy(alpha = 0.2f) else DarkSurfaceVariant)
                                    .border(1.dp, if (isSel) MrACyan else DarkOutline, RoundedCornerShape(12.dp))
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    color = if (isSel) MrACyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Task List
            if (tasks.isEmpty()) {
                item {
                    Text(
                        text = "လုပ်ငန်းဆောင်တာ မရှိသေးပါ။ အထက်တွင် အသစ်ထည့်သွင်းနိုင်ပါသည်။",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp))
                            .clickable { viewModel.toggleTask(task.id, task.isCompleted) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = if (task.isCompleted) "Completed" else "Pending",
                                tint = if (task.isCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                    ),
                                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = task.category,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MrACyan
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.deleteTask(task.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit or Register Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            profileToEdit = if (isRegisteringNew) null else activeProfile,
            isRegisteringNew = isRegisteringNew,
            onSaveProfile = { newProfile ->
                viewModel.saveProfile(newProfile)
                showEditProfileDialog = false
            },
            onDismiss = { showEditProfileDialog = false }
        )
    }

    // Switch Account Dialog
    if (showSwitchAccountDialog) {
        SwitchAccountDialog(
            profiles = profiles,
            activeProfileId = activeProfile?.id ?: 1L,
            onSelectProfile = { selectedId ->
                viewModel.switchProfile(selectedId)
            },
            onAddNewProfile = {
                isRegisteringNew = true
                showEditProfileDialog = true
            },
            onDeleteProfile = { id ->
                viewModel.deleteProfile(id)
            },
            onDismiss = { showSwitchAccountDialog = false }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("အကောင့်မှ ထွက်မည်လား?")
                }
            },
            text = {
                Text(
                    text = "လက်ရှိ '${activeProfile?.username}' အကောင့်မှ ထွက်မည်မှာ သေချာပါသလား? နောက်တစ်ကြိမ် အသုံးပြုလိုပါက ပြန်လည် Login ပြုလုပ်ရပါမည်။",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ထွက်မည် (Logout)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("မထွက်တော့ပါ")
                }
            }
        )
    }
}

private data class DailyTool(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val prompt: String
)
