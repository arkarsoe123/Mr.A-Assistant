package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entity.ChatSessionEntity
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MrACyan

@Composable
fun RenameChatDialog(
    session: ChatSessionEntity,
    onConfirmRename: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var titleText by remember { mutableStateOf(session.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = "စကားဝိုင်း ခေါင်းစဉ် ပြောင်းလဲရန်",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "စကားဝိုင်းအား အလွယ်တကူ ရှာဖွေနိုင်စေရန် ခေါင်းစဉ်အသစ် သတ်မှတ်ပေးပါ:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("ခေါင်းစဉ် (Title)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rename_chat_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleText.isNotBlank()) {
                        onConfirmRename(titleText.trim())
                    }
                },
                enabled = titleText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MrACyan),
                modifier = Modifier.testTag("confirm_rename_button")
            ) {
                Text("ပြောင်းမည်", color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("မလုပ်တော့ပါ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
