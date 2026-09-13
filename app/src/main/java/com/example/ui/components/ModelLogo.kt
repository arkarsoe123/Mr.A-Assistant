package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AiModel
import com.example.ui.theme.MrACyan

fun getModelLogoResId(model: AiModel): Int {
    return when (model) {
        AiModel.MR_A_LOCAL -> R.drawable.ic_model_mra
        AiModel.GEMINI -> R.drawable.ic_model_gemini
        AiModel.CHAT_GPT -> R.drawable.ic_model_chatgpt
        AiModel.DEEPSEEK -> R.drawable.ic_model_deepseek
    }
}

fun getModelBrandColor(model: AiModel): Color {
    return when (model) {
        AiModel.MR_A_LOCAL -> Color(0xFF00E5FF)
        AiModel.GEMINI -> Color(0xFF4E82EE)
        AiModel.CHAT_GPT -> Color(0xFF10A37F)
        AiModel.DEEPSEEK -> Color(0xFF1D72B8)
    }
}

fun modelFromMessage(modelName: String): AiModel {
    val lower = modelName.lowercase()
    return when {
        lower.contains("gemini") -> AiModel.GEMINI
        lower.contains("chatgpt") || lower.contains("openai") || lower.contains("gpt") -> AiModel.CHAT_GPT
        lower.contains("deepseek") -> AiModel.DEEPSEEK
        else -> AiModel.MR_A_LOCAL
    }
}

@Composable
fun ModelLogoIcon(
    model: AiModel,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color? = null
) {
    val brandColor = getModelBrandColor(model)
    val logoRes = getModelLogoResId(model)

    Image(
        painter = painterResource(id = logoRes),
        contentDescription = model.displayName,
        colorFilter = ColorFilter.tint(tint ?: brandColor),
        modifier = modifier.size(size)
    )
}

@Composable
fun ModelAvatarBadge(
    model: AiModel,
    characterEmoji: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    val brandColor = getModelBrandColor(model)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.5.dp, brandColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!characterEmoji.isNullOrBlank()) {
            Text(
                text = characterEmoji,
                fontSize = (size.value * 0.5f).sp
            )
        } else if (model == AiModel.MR_A_LOCAL) {
            Image(
                painter = painterResource(id = R.drawable.mra_avatar),
                contentDescription = "Mr.A Assistant",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            ModelLogoIcon(
                model = model,
                size = size * 0.58f,
                tint = brandColor
            )
        }
    }
}
