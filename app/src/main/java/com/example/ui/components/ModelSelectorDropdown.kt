package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CharacterAiEntity
import com.example.data.model.AiModel
import com.example.ui.theme.MrACyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import androidx.compose.material.icons.filled.Key

@Composable
fun ModelSelectorChip(
    selectedModel: AiModel,
    activeCharacter: CharacterAiEntity?,
    characters: List<CharacterAiEntity>,
    hasKeyForModel: (AiModel) -> Boolean = { true },
    onModelSelected: (AiModel) -> Unit,
    onConfigureKey: (AiModel) -> Unit = {},
    onCreateCharacter: () -> Unit,
    onSelectCharacter: (Long) -> Unit,
    onEditCharacter: (CharacterAiEntity) -> Unit,
    onResetToDefault: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("model_selector_chip")
        ) {
            if (activeCharacter != null) {
                Text(text = activeCharacter.avatarEmoji, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = activeCharacter.name,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MrACyan
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${selectedModel.displayName})",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                ModelLogoIcon(
                    model = selectedModel,
                    size = 16.dp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedModel.displayName,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .widthIn(min = 280.dp, max = 340.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            // Section 1: AI Models
            Text(
                text = "AI INTELLIGENCE MODELS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )

            AiModel.entries.forEach { model ->
                val isSelected = model == selectedModel
                val hasKey = hasKeyForModel(model)

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                ModelLogoIcon(
                                    model = model,
                                    size = 20.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = model.displayName,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        if (!model.isCloud) {
                                            Text(
                                                text = "Built-in Offline",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                color = SuccessGreen
                                            )
                                        } else if (hasKey) {
                                            Text(
                                                text = "Key Active ✓",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                color = SuccessGreen
                                            )
                                        } else {
                                            Text(
                                                text = "Needs Key",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                color = WarningAmber
                                            )
                                        }
                                    }
                                    Text(
                                        text = model.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (model.isCloud) {
                                    IconButton(
                                        onClick = {
                                            expanded = false
                                            onConfigureKey(model)
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Key,
                                            contentDescription = "Configure Key",
                                            tint = if (hasKey) SuccessGreen else WarningAmber,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MrACyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    },
                    onClick = {
                        onModelSelected(model)
                        expanded = false
                    },
                    modifier = Modifier.testTag("model_option_${model.id}")
                )
            }

            HorizontalDivider(
                color = DividerDefaults.color.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Section 2: Character AI (Custom Persona)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "CUSTOM CHARACTER AI",
                    style = MaterialTheme.typography.labelSmall,
                    color = MrACyan,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+ New",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MrACyan,
                    modifier = Modifier
                        .clickable {
                            expanded = false
                            onCreateCharacter()
                        }
                        .padding(4.dp)
                )
            }

            if (characters.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create first Character AI...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onCreateCharacter()
                    }
                )
            } else {
                characters.forEach { char ->
                    val isCharActive = activeCharacter?.id == char.id
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = char.avatarEmoji, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = char.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isCharActive) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = if (isCharActive) MrACyan else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = char.personaStyle,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            expanded = false
                                            onEditCharacter(char)
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Character",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    if (isCharActive) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Active",
                                            tint = MrACyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        },
                        onClick = {
                            onSelectCharacter(char.id)
                            expanded = false
                        }
                    )
                }
            }

            if (activeCharacter != null) {
                HorizontalDivider(
                    color = DividerDefaults.color.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Default Mr.A (No Character)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        onResetToDefault()
                        expanded = false
                    }
                )
            }
        }
    }
}
