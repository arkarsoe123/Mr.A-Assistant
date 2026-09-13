package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.AssistantAccent
import com.example.ui.theme.AssistantBubble
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MrACyan
import com.example.ui.theme.UserBubble
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier,
    userName: String = "User",
    userAvatarEmoji: String = "👤"
) {
    val isUser = message.role == ChatMessage.Role.USER
    val context = LocalContext.current
    val timeFormatted = rememberTime(message.timestamp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .testTag(if (isUser) "user_message_item" else "assistant_message_item"),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            // Assistant / Character Avatar using authentic model logo
            val detectedModel = modelFromMessage(message.modelName)
            val charEmoji = extractFirstEmoji(message.modelName)
            ModelAvatarBadge(
                model = detectedModel,
                characterEmoji = charEmoji,
                size = 36.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            // User name & timestamp for user message
            if (isUser) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 2.dp, end = 4.dp)
                ) {
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Model name badge for assistant with authentic logo
            if (!isUser && message.modelName.isNotBlank()) {
                val detectedModel = modelFromMessage(message.modelName)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 2.dp, start = 4.dp)
                ) {
                    ModelLogoIcon(
                        model = detectedModel,
                        size = 14.dp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = message.modelName,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = getModelBrandColor(detectedModel),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Message Bubble with SelectionContainer so user can select & copy any portion!
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        when {
                            message.isError -> MaterialTheme.colorScheme.errorContainer
                            isUser -> UserBubble
                            else -> AssistantBubble
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = when {
                            message.isError -> ErrorRed.copy(alpha = 0.5f)
                            isUser -> Color.Transparent
                            else -> DarkOutline
                        },
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                SelectionContainer {
                    Column {
                        if (message.isError) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = ErrorRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Request Failed",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ErrorRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        RenderFormattedText(
                            text = message.content,
                            textColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                            onCopyCode = { codeSnippet ->
                                copyToClipboard(context, codeSnippet)
                                Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // Action Row for Assistant Message (Full Copy & Speak)
            if (!isUser && !message.isError && message.content.isNotBlank()) {
                Row(
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            copyToClipboard(context, message.content)
                            Toast.makeText(context, "Copied response to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("copy_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { onSpeak(message.content) },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read aloud",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userAvatarEmoji.ifBlank { "👤" },
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun RenderFormattedText(
    text: String,
    textColor: Color,
    onCopyCode: (String) -> Unit
) {
    if (text.contains("```")) {
        val parts = text.split("```")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            parts.forEachIndexed { index, part ->
                if (index % 2 == 1) {
                    // Code Block with Header & Dedicated Copy Code Button
                    val trimmed = part.trim()
                    val firstLineBreak = trimmed.indexOf('\n')
                    val (language, actualCode) = if (firstLineBreak != -1) {
                        val firstLine = trimmed.substring(0, firstLineBreak).trim()
                        if (firstLine.isNotEmpty() && !firstLine.contains(" ")) {
                            Pair(firstLine, trimmed.substring(firstLineBreak + 1))
                        } else {
                            Pair("Code", trimmed)
                        }
                    } else {
                        Pair("Code", trimmed)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                    ) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = language,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            IconButton(
                                onClick = { onCopyCode(actualCode) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy code",
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFF334155), thickness = 0.5.dp)

                        // Code content
                        Text(
                            text = actualCode.trim(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFF38BDF8),
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                } else if (part.isNotBlank()) {
                    Text(
                        text = part.trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    } else {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            lineHeight = 22.sp
        )
    }
}

private fun extractFirstEmoji(text: String): String? {
    val regex = Regex("^[\\p{So}\\p{Sk}\\p{Cs}]+")
    val match = regex.find(text.trim())
    return match?.value
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("AI Response", text)
    clipboard.setPrimaryClip(clip)
}

@Composable
private fun rememberTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
