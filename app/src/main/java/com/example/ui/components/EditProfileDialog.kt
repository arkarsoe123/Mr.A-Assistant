package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserProfileEntity
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MrACyan

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditProfileDialog(
    profileToEdit: UserProfileEntity?,
    isRegisteringNew: Boolean = false,
    onSaveProfile: (UserProfileEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var username by remember { mutableStateOf(profileToEdit?.username ?: "") }
    var bio by remember { mutableStateOf(profileToEdit?.bio ?: "") }
    var avatarEmoji by remember { mutableStateOf(profileToEdit?.avatarEmoji ?: "👨‍💼") }
    var pinCode by remember { mutableStateOf(profileToEdit?.pinCode ?: "") }
    var role by remember { mutableStateOf(profileToEdit?.role ?: "Family Member") }
    var showPin by remember { mutableStateOf(false) }

    val emojis = listOf("👨‍💼", "👩‍💼", "👨‍💻", "👩‍⚕️", "🧑‍🎓", "👨‍🏫", "👩‍🍳", "👨‍🎨", "🧕", "🧔", "🧒", "👶", "🐱", "🚀")
    val roles = listOf("Primary", "Work", "Personal", "Family Member", "Kid")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MrACyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MrACyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isRegisteringNew) "Account အသစ် မှတ်ပုံတင်ရန်" else "Profile အချက်အလက် ပြင်ဆင်ရန်",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Avatar Selection
                Text(
                    text = "Avatar Emoji ရွေးချယ်ပါ:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojis.forEach { emoji ->
                        val isSelected = emoji == avatarEmoji
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MrACyan.copy(alpha = 0.25f) else Color(0xFF1E293B))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MrACyan else DarkOutline,
                                    shape = CircleShape
                                )
                                .clickable { avatarEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("အမည် (Username) *") },
                    placeholder = { Text("ဥပမာ - ကိုအောင်၊ မနှင်း၊ သားသား") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_username_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Bio
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("ကိုယ်ရေးအကျဉ်း (Bio)") },
                    placeholder = { Text("ဥပမာ - ရုံးဝန်ထမ်း၊ ကျောင်းသား၊ မိခင်") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_bio_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Role/Tag
                Text(
                    text = "Account အမျိုးအစား:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    roles.forEach { r ->
                        val isSelected = r == role
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) MrACyan.copy(alpha = 0.2f) else Color(0xFF1E293B))
                                .border(
                                    1.dp,
                                    if (isSelected) MrACyan else DarkOutline,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { role = r }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = r,
                                fontSize = 11.sp,
                                color = if (isSelected) MrACyan else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Optional PIN Code
                OutlinedTextField(
                    value = pinCode,
                    onValueChange = { if (it.length <= 6) pinCode = it },
                    label = { Text("လုံခြုံရေး PIN (Optional - မထည့်လည်းရ)") },
                    placeholder = { Text("ဂဏန်း ၄ လုံး သို့မဟုတ် ၆ လုံး") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPin = !showPin }) {
                            Icon(
                                imageVector = if (showPin) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle PIN",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotBlank()) {
                        val entity = UserProfileEntity(
                            id = profileToEdit?.id ?: 0L,
                            username = username.trim(),
                            bio = bio.trim(),
                            avatarEmoji = avatarEmoji,
                            pinCode = pinCode.trim(),
                            role = role,
                            createdAt = profileToEdit?.createdAt ?: System.currentTimeMillis()
                        )
                        onSaveProfile(entity)
                    }
                },
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text(
                    text = if (isRegisteringNew) "Register & Login" else "Save Changes",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ပယ်ဖျက်မည်", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
