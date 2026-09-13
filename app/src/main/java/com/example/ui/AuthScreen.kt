package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.UserProfileEntity
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MrACyan
import com.example.ui.theme.MrAViolet
import com.example.ui.theme.WarningAmber

@Composable
fun AuthScreen(
    viewModel: MrAViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()
    val maxLimit = viewModel.maxProfilesLimit

    // If no profiles exist, default to Register tab (tab index 1)
    var selectedTabIndex by remember(profiles.size) {
        mutableIntStateOf(if (profiles.isEmpty()) 1 else 0)
    }

    var selectedProfileForPin by remember { mutableStateOf<UserProfileEntity?>(null) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Brand Header
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(MrACyan, MrAViolet))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mr.A",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Local Account Access",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "အင်တာနက်မလိုဘဲ စက်ထဲတွင်သာ လုံခြုံစွာ အသုံးပြုနိုင်ပါသည် (အများဆုံး ၃ ခု)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Tab Selector: Login vs Register
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = DarkSurface,
                contentColor = MrACyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MrACyan
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.Login,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTabIndex == 0) MrACyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Login (${profiles.size})",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == 0) MrACyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTabIndex == 1) MrACyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Register (${profiles.size}/$maxLimit)",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == 1) MrACyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTabIndex == 0) {
                // LOGIN TAB
                LoginTabContent(
                    profiles = profiles,
                    maxLimit = maxLimit,
                    onSelectProfile = { profile ->
                        if (profile.pinCode.isBlank()) {
                            viewModel.login(profile.id)
                        } else {
                            selectedProfileForPin = profile
                            enteredPin = ""
                            pinError = null
                        }
                    },
                    onNavigateToRegister = { selectedTabIndex = 1 }
                )
            } else {
                // REGISTER TAB
                RegisterTabContent(
                    currentCount = profiles.size,
                    maxLimit = maxLimit,
                    onRegister = { profile ->
                        viewModel.registerNewProfile(profile)
                    },
                    onNavigateToLogin = { selectedTabIndex = 0 }
                )
            }
        }
    }

    // PIN Authentication Dialog for locked profile
    selectedProfileForPin?.let { profile ->
        AlertDialog(
            onDismissRequest = { selectedProfileForPin = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = MrACyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("လုံခြုံရေး PIN ထည့်ပါ")
                }
            },
            text = {
                Column {
                    Text(
                        text = "'${profile.username}' အကောင့်သို့ ဝင်ရောက်ရန် လျှို့ဝှက် PIN နံပါတ် ထည့်သွင်းပါ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            if (it.length <= 6) {
                                enteredPin = it
                                pinError = null
                            }
                        },
                        label = { Text("PIN Code") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError != null,
                        supportingText = pinError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredPin == profile.pinCode) {
                            selectedProfileForPin = null
                            viewModel.login(profile.id)
                        } else {
                            pinError = "PIN မှားယွင်းနေပါသည်။ ပြန်လည်စစ်ဆေးပါ"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MrACyan)
                ) {
                    Text("ဝင်ရောက်မည် (Login)", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedProfileForPin = null }) {
                    Text("မလုပ်တော့ပါ")
                }
            }
        )
    }
}

@Composable
private fun LoginTabContent(
    profiles: List<UserProfileEntity>,
    maxLimit: Int,
    onSelectProfile: (UserProfileEntity) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (profiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("👤", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "အကောင့် မရှိသေးပါ",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "စတင်အသုံးပြုရန် အကောင့်အသစ်တစ်ခု ဖွင့်လှစ်ပေးပါ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToRegister,
                        colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("အကောင့်အသစ် ဖွင့်မည်", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Text(
                text = "မိမိ၏ အကောင့်ကို ရွေးချယ်၍ ဝင်ရောက်ပါ:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            profiles.forEach { profile ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkOutline, RoundedCornerShape(14.dp))
                        .clickable { onSelectProfile(profile) }
                        .padding(14.dp)
                        .testTag("profile_item_${profile.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MrACyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = profile.avatarEmoji, fontSize = 26.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.username,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (profile.pinCode.isNotBlank()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "PIN Protected",
                                    tint = WarningAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Text(
                            text = profile.bio.ifBlank { profile.role },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = profile.role,
                                fontSize = 10.sp,
                                color = MrACyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = { onSelectProfile(profile) },
                        colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Login", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (profiles.size < maxLimit) {
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = MrACyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+ အကောင့်အသစ် ထပ်မံဖွင့်မည် (ကျန်ရှိ: ${maxLimit - profiles.size} ခု)",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarningAmber.copy(alpha = 0.15f))
                        .border(1.dp, WarningAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "အကောင့် ၃ ခု ကန့်သတ်ချက် ပြည့်ပါပြီ (မိသားစုဝင် ၃ ဦး အထိ)",
                        fontSize = 12.sp,
                        color = WarningAmber
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterTabContent(
    currentCount: Int,
    maxLimit: Int,
    onRegister: (UserProfileEntity) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    if (currentCount >= maxLimit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "အကောင့် ကန့်သတ်ချက် ပြည့်ပါပြီ",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarningAmber
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "ဤ Device တွင် အများဆုံး အကောင့် ၃ ခုသာ ဖွင့်လှစ်ခွင့်ရှိပါသည်။ လက်ရှိအကောင့်များမှ Login ဝင်ရောက်အသုံးပြုပါရန်။",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Login စာရင်းသို့ သွားမည်", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    val availableEmojis = listOf("👨‍💼", "👩‍💼", "🧑‍💻", "👨‍👩‍👧", "🎓", "👵", "🧕", "👦", "👧", "🎨")
    val roles = listOf("Primary Account", "Family Member", "Office Work", "Personal", "Kid")

    var selectedEmoji by remember { mutableStateOf("👨‍💼") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Primary Account") }
    var pinCode by remember { mutableStateOf("") }
    var showPin by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "အကောင့် အသစ် ဖွင့်လှစ်ရန် (${currentCount + 1}/$maxLimit)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MrACyan
        )

        // Avatar Emoji Selector
        Column {
            Text(
                text = "Profile Avatar ရွေးပါ:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableEmojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedEmoji == emoji) MrACyan.copy(alpha = 0.3f)
                                else DarkSurfaceVariant
                            )
                            .border(
                                width = if (selectedEmoji == emoji) 2.dp else 1.dp,
                                color = if (selectedEmoji == emoji) MrACyan else DarkOutline,
                                shape = CircleShape
                            )
                            .clickable { selectedEmoji = emoji },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 22.sp)
                    }
                }
            }
        }

        // Username Field (Required)
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorText = null
            },
            label = { Text("Username (အမည်) *") },
            placeholder = { Text("ဥပမာ - ဦးအောင်၊ ဒေါ်မြ၊ သားသား") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_username_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MrACyan,
                unfocusedBorderColor = DarkOutline
            )
        )

        // Bio Field
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio / အကြောင်းအရာ (Optional)") },
            placeholder = { Text("ဥပမာ - ရုံးသုံး၊ နေ့စဉ်ဘဝမှတ်တမ်း") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MrACyan,
                unfocusedBorderColor = DarkOutline
            )
        )

        // Role Selector Chips
        Column {
            Text(
                text = "အကောင့် အမျိုးအစား (Role):",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(roles) { r ->
                    val isSelected = selectedRole == r
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MrACyan.copy(alpha = 0.2f) else DarkSurfaceVariant)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) MrACyan else DarkOutline,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedRole = r }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = r,
                            fontSize = 11.sp,
                            color = if (isSelected) MrACyan else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Optional PIN
        OutlinedTextField(
            value = pinCode,
            onValueChange = {
                if (it.length <= 6) pinCode = it
            },
            label = { Text("လုံခြုံရေး PIN (ရွေးချယ်ရန်)") },
            placeholder = { Text("၄ လုံး ဂဏန်း (မထည့်လဲရပါသည်)") },
            singleLine = true,
            visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            trailingIcon = {
                IconButton(onClick = { showPin = !showPin }) {
                    Icon(
                        imageVector = if (showPin) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MrACyan,
                unfocusedBorderColor = DarkOutline
            )
        )

        AnimatedVisibility(visible = errorText != null) {
            Text(
                text = errorText ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (username.isBlank()) {
                    errorText = "Username အမည် ထည့်သွင်းပေးပါ"
                    return@Button
                }
                val newProfile = UserProfileEntity(
                    username = username.trim(),
                    bio = bio.trim(),
                    avatarEmoji = selectedEmoji,
                    role = selectedRole,
                    pinCode = pinCode.trim()
                )
                onRegister(newProfile)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("submit_register_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Register & Login (အကောင့်ဖွင့်ပြီး ဝင်ရောက်မည်)", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
