package com.example.data.remote.brain

import com.example.data.local.entity.CharacterAiEntity
import com.example.data.model.AiModel
import com.example.data.model.ChatMessage
import java.util.Locale

object MrABrain {

    fun generate(
        model: AiModel,
        prompt: String,
        history: List<ChatMessage> = emptyList(),
        character: CharacterAiEntity? = null
    ): String {
        val clean = prompt.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // 1. If custom Character AI is active, adapt the voice, style, and persona
        if (character != null) {
            return generateCharacterResponse(model, clean, lower, character, history)
        }

        // 2. Math & Calculations (high priority)
        val mathSolution = MathBrain.trySolve(clean)
        if (mathSolution != null) {
            return mathSolution
        }

        // 3. Coding & Software Engineering
        val codingSolution = CodingBrain.trySolve(clean)
        if (codingSolution != null) {
            return codingSolution
        }

        // 4. Professional Writing & Letters
        val writingSolution = WritingBrain.trySolve(clean)
        if (writingSolution != null) {
            return writingSolution
        }

        // 5. Knowledge, Science, Culture, Finance & Health
        val knowledgeSolution = KnowledgeBrain.trySolve(clean)
        if (knowledgeSolution != null) {
            return knowledgeSolution
        }

        // 6. Greetings & Introduction
        if (clean.contains("မင်္ဂလာပါ") || lower.contains("hello") || lower == "hi" || lower == "hey" || clean.contains("မင်္ဂလာ")) {
            return """
                👋 **မင်္ဂလာပါခင်ဗျာ!**
                ကျွန်တော်ကတော့ လူကြီးမင်းတို့၏ နေ့စဉ်ဘဝ၊ လုပ်ငန်းဆောင်တာများနှင့် ဗဟုသုတများကို ကူညီပေးမည့် **Mr.A AI Assistant** ဖြစ်ပါတယ်။
                
                🧠 **Mr.A Brain အဆင့်မြှင့်တင်ထားသော အဓိက စွမ်းရည်များ:**
                - 🧮 **သင်္ချာနှင့် တွက်ချက်မှု:** ရာခိုင်နှုန်း (% of, အတိုးအလျှော့)၊ အလေးချိန် (kg, lbs, ပိဿာ)၊ အကွာအဝေး၊ အပူချိန်နှင့် ဂျီသြမေတြီ တွက်ချက်မှုများ
                - 💻 **ပရိုဂရမ်းမင်းနှင့် နည်းပညာ:** Android (Jetpack Compose, ViewModel), Kotlin, Python, SQL Database, Git Commands များကို ဥပမာ ကုဒ်များဖြင့် ရှင်းပြပေးခြင်း
                - 📝 **ရုံးသုံးနှင့် စီးပွားရေး စာရေးသားခြင်း:** အလုပ်ခွင့်တိုင်စာ၊ အလုပ်ထွက်စာ၊ အလုပ်လျှောက်လွှာ၊ စီးပွားရေး အီးမေးလ်နှင့် ကြော်ငြာစာသားများ
                - 💡 **အထွေထွေ ဗဟုသုတ:** ဉာဏ်ရည်တု (AI/LLM), ကျန်းမာရေး၊ ၅၀/၃၀/၂၀ ဘတ်ဂျက်စနစ်၊ အချိန်စီမံခန့်ခွဲမှုနှင့် မြန်မာ့ရိုးရာ ယဉ်ကျေးမှုများ
                - 🎭 **စိတ်ကြိုက် ကာရိုက်တာ ဖန်တီးခြင်း:** မိမိနှစ်သက်ရာ Character AI (ဥပမာ Megan, စာရေးဆရာ, အကြံပေး) ဖန်တီး၍ စကားပြောဆိုနိုင်ခြင်း
                
                လူကြီးမင်း မေးမြန်းလိုသည်များကို စိတ်တိုင်းကျ မေးမြန်းနိုင်ပါပြီခင်ဗျာ!
            """.trimIndent()
        }

        // 7. General Intelligent Reasoning Core
        return ReasoningBrain.synthesize(clean, lower, history)
    }

    private fun generateCharacterResponse(
        model: AiModel,
        clean: String,
        lower: String,
        character: CharacterAiEntity,
        history: List<ChatMessage>
    ): String {
        val mathSolution = MathBrain.trySolve(clean)
        if (mathSolution != null) {
            return "${character.avatarEmoji} **[${character.name}]**\n\n$mathSolution\n\n*${character.name} ၏ စည်းမျဉ်းအရ အဖြေတိကျမှုကို အတည်ပြုပြီးပါပြီ။*"
        }

        val persona = character.personaStyle
        val memory = if (character.memoryData.isNotBlank()) "\n📌 **မှတ်သားထားသော အချက်အလက်:** ${character.memoryData}" else ""
        val rules = if (character.strictnessRules.isNotBlank()) "\n⚡ **လိုက်နာသော စည်းမျဉ်း:** ${character.strictnessRules}" else ""

        if (clean.contains("မင်္ဂလာပါ") || clean.contains("hello") || lower == "hi" || clean.contains("မင်္ဂလာ")) {
            val greeting = if (character.greetingMessage.isNotBlank()) character.greetingMessage else "မင်္ဂလာပါ! ကျွန်တော်/ကျွန်မကတော့ ${character.name} ဖြစ်ပါတယ်။"
            return """
                ${character.avatarEmoji} **$greeting**
                
                🎭 **ကာရိုက်တာ စတိုင်:** $persona$memory$rules
                
                လူကြီးမင်း မေးလိုသော မေးခွန်းများ၊ ရေးသားလိုသော စာများနှင့် အကြံဉာဏ်များကို ကျွန်ုပ်၏ သီးသန့် ကာရိုက်တာစတိုင်ဖြင့် အကောင်းဆုံး ကူညီဖြေကြားပေးပါမည်။
            """.trimIndent()
        }

        val stylePrefix = when {
            persona.contains("Megan", ignoreCase = true) || persona.contains("သွေးအေး", ignoreCase = true) ->
                "⚠️ **[Megan Protocol Active]**\n*စောင့်ကြည့်ကာကွယ်မှု စနစ် အသင့်ရှိသည်။ အမှားအယွင်း မရှိစေရန် စစ်ဆေးထားသည်။*\n\n"
            persona.contains("နွေးထွေး", ignoreCase = true) || persona.contains("မိတ်ဆွေ", ignoreCase = true) ->
                "🌸 **[နွေးထွေးသော အဖော်မွန်]**\n"
            persona.contains("ဆရာ", ignoreCase = true) || persona.contains("ပညာရှင်", ignoreCase = true) ->
                "📖 **[ပညာရှင် အကြံပေး]**\n"
            else -> ""
        }

        val baseInsight = ReasoningBrain.synthesize(clean, lower, history)

        return """
            $stylePrefix${character.avatarEmoji} **${character.name} (${model.displayName} Mode):**
            
            လူကြီးမင်း မေးမြန်းထားသော: *"$clean"*
            
            ကျွန်ုပ် **${character.name}** ၏ သတ်မှတ်ထားသော စရိုက်လက္ခဏာ ($persona) အရ တုံ့ပြန်ပေးလိုက်ပါသည်-
            
            $baseInsight$memory
            
            ဆက်လက်၍ မည်သည့်အချက်ကို အသေးစိတ် ဆွေးနွေးလိုပါသလဲခင်ဗျာ?
        """.trimIndent()
    }
}
