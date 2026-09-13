package com.example.ui.components

import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AiModel
import com.example.ui.TestKeyStatus
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MrACyan
import com.example.ui.theme.SuccessGreen

@Composable
fun ApiKeyDialog(
    targetModel: AiModel,
    currentKey: String,
    testStatus: TestKeyStatus,
    onSaveKey: (AiModel, String) -> Unit,
    onTestKey: (AiModel, String) -> Unit,
    onDismiss: () -> Unit,
    onSelectLocalModel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var keyInput by remember(targetModel, currentKey) { mutableStateOf(currentKey) }
    var isMasked by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, getModelBrandColor(targetModel).copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
            color = DarkSurface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ModelAvatarBadge(
                            model = targetModel,
                            size = 38.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${targetModel.displayName} API Key",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Bring Your Own Key (BYOK)",
                                style = MaterialTheme.typography.bodySmall,
                                color = getModelBrandColor(targetModel)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_api_key_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Info Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBg)
                        .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "💡 သင့်ကိုယ်ပိုင် API Key ဖြင့် တိုက်ရိုက်ချိတ်ဆက်မည်",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "ထည့်သွင်းသော Key ကို သင့်ဖုန်းအတွင်း၌သာ လုံခြုံစွာသိမ်းဆည်းပြီး App အတွင်း မည်သည့် Key မှ ကြိုတင်ထည့်သွင်းထားခြင်း မရှိပါ။",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key Input Field
                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("${targetModel.displayName} API Key") },
                    placeholder = {
                        Text(
                            text = when (targetModel) {
                                AiModel.GEMINI -> "AIzaSy..."
                                AiModel.CHAT_GPT -> "sk-..."
                                AiModel.DEEPSEEK -> "sk-..."
                                else -> "Enter API Key"
                            }
                        )
                    },
                    visualTransformation = if (isMasked) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = getModelBrandColor(targetModel)
                        )
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { isMasked = !isMasked }) {
                                Icon(
                                    imageVector = if (isMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isMasked) "Show key" else "Hide key",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    val clip = clipboard?.primaryClip
                                    if (clip != null && clip.itemCount > 0) {
                                        val pasteText = clip.getItemAt(0).text?.toString() ?: ""
                                        if (pasteText.isNotBlank()) {
                                            keyInput = pasteText.trim()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste from clipboard",
                                    tint = getModelBrandColor(targetModel)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = getModelBrandColor(targetModel),
                        unfocusedBorderColor = DarkOutline,
                        focusedContainerColor = DarkBg,
                        unfocusedContainerColor = DarkBg,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_input_field")
                )

                // Test Status Feedback
                when (testStatus) {
                    is TestKeyStatus.Testing -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = getModelBrandColor(targetModel)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "API Key အလုပ်လုပ်မှု စစ်ဆေးနေပါသည်...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    is TestKeyStatus.Success -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "API Key မှန်ကန်ပါသည်! ချိတ်ဆက်မှု အောင်မြင်ပါသည်။",
                                style = MaterialTheme.typography.bodySmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    is TestKeyStatus.Error -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = testStatus.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = ErrorRed,
                                lineHeight = 15.sp
                            )
                        }
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onTestKey(targetModel, keyInput) },
                        enabled = keyInput.isNotBlank() && testStatus !is TestKeyStatus.Testing,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("test_api_key_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Test Key")
                    }

                    Button(
                        onClick = { onSaveKey(targetModel, keyInput) },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("save_api_key_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getModelBrandColor(targetModel)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (keyInput.isBlank()) "Clear Key" else "Save & Use",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Fallback to Mr.A Local Engine
                OutlinedButton(
                    onClick = {
                        onSelectLocalModel()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("use_mra_local_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MrACyan
                    )
                ) {
                    Text("Use Mr.A Local (Key မလို - အခမဲ့)")
                }
            }
        }
    }
}
