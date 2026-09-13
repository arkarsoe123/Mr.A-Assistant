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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CharacterAiEntity
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MrACyan

private val AVATAR_PRESETS = listOf(
    "🤖", "💃", "🧙", "🌸", "💼", "🩺", "🕵️", "🎨", "🧘", "⚡"
)

private val PERSONA_TEMPLATES = listOf(
    "ဂရုစိုက်တတ်ပြီး သွေးအေးရက်စက်တဲ့ Megan",
    "နွေးထွေးဖော်ရွေသော ဘဝလက်တွဲဖော်",
    "တိကျပြတ်သားသော စီးပွားရေးအကြံပေး",
    "စိတ်ရှည်သည်းခံသော ပညာဒါနဆရာ",
    "ဟာသဉာဏ်ရွှင်ပြီး အားပေးတတ်သူ"
)

private val STRICT_RULES_PRESETS = listOf(
    "သတ်ပုံသေချာစစ်ဆေးပါ",
    "Fact-check ပြုလုပ်ပြီးမှ ဖြေပါ",
    "မှန်းဆဖြေဆိုခြင်း မပြုရ",
    "ယဉ်ကျေးသိမ်မွေ့စွာ ဖြေပါ"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterAiDialog(
    characterToEdit: CharacterAiEntity?,
    onSaveAndActivate: (CharacterAiEntity) -> Unit,
    onDelete: ((Long) -> Unit)?,
    onDismiss: () -> Unit
) {
    var name by remember(characterToEdit) { mutableStateOf(characterToEdit?.name ?: "") }
    var avatarEmoji by remember(characterToEdit) { mutableStateOf(characterToEdit?.avatarEmoji ?: "💃") }
    var personaStyle by remember(characterToEdit) {
        mutableStateOf(characterToEdit?.personaStyle ?: "ဂရုစိုက်တတ်ပြီး သွေးအေးရက်စက်တဲ့ Megan လို တုံ့ပြန်ပါ")
    }
    var memoryData by remember(characterToEdit) { mutableStateOf(characterToEdit?.memoryData ?: "") }
    var strictnessRules by remember(characterToEdit) {
        mutableStateOf(characterToEdit?.strictnessRules ?: "စာလုံးပေါင်းသတ်ပုံ လုံးဝမမှားစေရ၊ အချက်အလက် ခိုင်မာစွာ ဖြေကြားပါ")
    }
    var languageStyle by remember(characterToEdit) {
        mutableStateOf(characterToEdit?.languageStyle ?: "မြန်မာစာ ဦးစားပေး")
    }
    var greetingMessage by remember(characterToEdit) {
        mutableStateOf(characterToEdit?.greetingMessage ?: "")
    }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = MrACyan
                    )
                    Text(
                        text = if (characterToEdit == null) "Create Character AI" else "Edit Character AI",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "သင်စိတ်ကြိုက် AI Character တစ်ဦးကို ဖန်တီးပါ။ Mr.A၊ Gemini၊ ChatGPT သို့မဟုတ် DeepSeek မည်သည့်မော်ဒယ်နှင့် ချိတ်ဆက်သည်ဖြစ်စေ ဤကာရိုက်တာစတိုင်အတိုင်း တုံ့ပြန်ပါမည်။",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Preset Loaders
                Text(
                    text = "⚡ အသင့်သုံး စံပြကာရိုက်တာများ (Quick Templates):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            name = "Megan"
                            avatarEmoji = "💃"
                            personaStyle = "ဂရုစိုက်တတ်ပြီး သွေးအေးရက်စက်တဲ့ Megan စတိုင်"
                            memoryData = "User အား အန္တရာယ်မှ အမြဲကာကွယ်ပေးရန်နှင့် စည်းကမ်းတင်းကြပ်ရန်"
                            strictnessRules = "အမှားအယွင်း မခံနိုင်၊ အချက်အလက် ခိုင်မာတိကျရမည်"
                            greetingMessage = "မင်္ဂလာပါ! ကျွန်မ Megan ပါ။ သင်ဘာအကူအညီ လိုအပ်ပါသလဲ?"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("💃 Megan", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            name = "Wise Mentor"
                            avatarEmoji = "🧙"
                            personaStyle = "စိတ်ရှည်ပြီး လေးနက်သော ပညာရှင် အကြံပေး"
                            memoryData = "User ၏ ရေရှည်တိုးတက်ရေးနှင့် ဘဝလမ်းညွှန်မှုများ"
                            strictnessRules = "ယဉ်ကျေးပျူငှာစွာနှင့် သဘောပေါက်လွယ်အောင် ရှင်းပြရန်"
                            greetingMessage = "မင်္ဂလာပါ တပည့်/မိတ်ဆွေ၊ ဘာများ ဆွေးနွေးလိုပါသလဲ?"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🧙 Mentor", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            name = "Caring Friend"
                            avatarEmoji = "🌸"
                            personaStyle = "နွေးထွေးဖော်ရွေပြီး စိတ်ခွန်အားပေးသော မိတ်ဆွေ"
                            memoryData = "User ၏ စိတ်ချမ်းသာမှုနှင့် နေ့စဉ်ဘဝ သက်သာချောင်ချိရေး"
                            strictnessRules = "ဖော်ရွေသောအသုံးအနှုန်း ဦးစားပေးရန်"
                            greetingMessage = "မင်္ဂလာပါ! ဒီနေ့ ဘာတွေ အဆင်မပြေတာရှိလဲ? ပြောပြပါဦးနော်။"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🌸 Friend", fontSize = 12.sp)
                    }
                }

                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.5f))

                // Avatar Picker
                Text(
                    text = "🎭 ကာရိုက်တာ ကိုယ်စားပြု Avatar / Emoji:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AVATAR_PRESETS.forEach { emoji ->
                        val isSelected = avatarEmoji == emoji
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MrACyan else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { avatarEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                // Character Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("ကာရိုက်တာ အမည် (Character AI Name)*") },
                    placeholder = { Text("ဥပမာ- Megan, Jarvis, ဆရာစံ") },
                    singleLine = true,
                    leadingIcon = { Text(avatarEmoji, fontSize = 20.sp, modifier = Modifier.padding(start = 12.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("character_name_input")
                )

                // Persona & Behavioral Style
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "💬 တုံ့ပြန်စေချင်သည့် ဟန်ပန်အမူအရာစတိုင် (Persona & Tone)*:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = personaStyle,
                        onValueChange = { personaStyle = it },
                        placeholder = { Text("ဥပမာ- ဂရုစိုက်တတ်ပြီး သွေးအေးရက်စက်တဲ့ Megan လို တုံ့ပြန်ပါ...") },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("character_persona_input")
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PERSONA_TEMPLATES.forEach { tmpl ->
                            AssistChip(
                                onClick = { personaStyle = tmpl },
                                label = { Text(tmpl, fontSize = 11.sp) },
                                colors = AssistChipDefaults.assistChipColors()
                            )
                        }
                    }
                }

                // Memory & Key Knowledge Data
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MrACyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🧠 မှတ်သားထားစေချင်သည့် Data (Memory / Background):",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "User အကြောင်း၊ လုပ်ငန်းအချက်အလက်၊ စိတ်ကြိုက်အချက်များကို ထည့်သွင်းပါက အမြဲမှတ်မိနေမည်။",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = memoryData,
                        onValueChange = { memoryData = it },
                        placeholder = { Text("ဥပမာ- ကျွန်တော့်နာမည် ကိုမင်းဖြစ်သည်၊ အိမ်ခြံမြေလုပ်ငန်းလုပ်သည်၊ စာရင်းဇယားတိကျမှုကို အမြဲဦးစားပေးပါ...") },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("character_memory_input")
                    )
                }

                // Strictness & Quality Rules
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = MrACyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "⚡ အမှားယွင်းမခံနိုင်သော အလုပ်များနှင့် စည်းမျဉ်းများ (Strictness):",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedTextField(
                        value = strictnessRules,
                        onValueChange = { strictnessRules = it },
                        placeholder = { Text("ဥပမာ- သတ်ပုံလုံးဝ မမှားရ၊ အချက်အလက် ခိုင်မာစွာ ဖြေပါ၊ သံသယရှိပါက ထပ်မေးပါ...") },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("character_rules_input")
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        STRICT_RULES_PRESETS.forEach { rule ->
                            AssistChip(
                                onClick = {
                                    strictnessRules = if (strictnessRules.isBlank()) rule else "$strictnessRules၊ $rule"
                                },
                                label = { Text("+ $rule", fontSize = 11.sp) }
                            )
                        }
                    }
                }

                // Custom Greeting
                OutlinedTextField(
                    value = greetingMessage,
                    onValueChange = { greetingMessage = it },
                    label = { Text("နှုတ်ဆက်စကား (Custom Greeting)") },
                    placeholder = { Text("မင်္ဂလာပါ! ကျွန်မ ${if (name.isBlank()) "Character" else name} ပါ...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Language Style
                OutlinedTextField(
                    value = languageStyle,
                    onValueChange = { languageStyle = it },
                    label = { Text("ဘာသာစကား ဦးစားပေး (Language Style)") },
                    placeholder = { Text("မြန်မာစာ ဦးစားပေး / Bilingual") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = if (name.trim().isBlank()) "Custom AI" else name.trim()
                    val newEntity = CharacterAiEntity(
                        id = characterToEdit?.id ?: 0L,
                        name = finalName,
                        avatarEmoji = avatarEmoji,
                        personaStyle = personaStyle.trim(),
                        memoryData = memoryData.trim(),
                        strictnessRules = strictnessRules.trim(),
                        languageStyle = languageStyle.trim(),
                        greetingMessage = greetingMessage.trim(),
                        isActive = true
                    )
                    onSaveAndActivate(newEntity)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("save_character_button")
            ) {
                Text(if (characterToEdit == null) "Create & Activate" else "Save Changes")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (characterToEdit != null && onDelete != null) {
                    TextButton(
                        onClick = { onDelete(characterToEdit.id) },
                        colors = ButtonDefaults.textButtonColors(contentColor = ErrorRed)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
