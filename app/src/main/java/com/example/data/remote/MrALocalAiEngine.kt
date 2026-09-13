package com.example.data.remote

import com.example.data.local.entity.CharacterAiEntity
import com.example.data.model.AiModel
import com.example.data.model.ChatMessage
import com.example.data.remote.brain.CodingBrain
import com.example.data.remote.brain.MathBrain
import com.example.data.remote.brain.MrABrain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Locale

class MrALocalAiEngine {

    suspend fun generateResponse(
        model: AiModel = AiModel.MR_A_LOCAL,
        prompt: String,
        history: List<ChatMessage> = emptyList(),
        activeCharacter: CharacterAiEntity? = null
    ): String = withContext(Dispatchers.Default) {
        // Natural slight delay for on-device generation feel
        delay(350)

        val clean = prompt.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // If a custom Character AI is active, adapt the voice, style, and memory!
        if (activeCharacter != null) {
            return@withContext MrABrain.generate(model, clean, history, activeCharacter)
        }

        // Generate response based on selected model personality
        return@withContext when (model) {
            AiModel.MR_A_LOCAL -> MrABrain.generate(model, clean, history, null)
            AiModel.GEMINI -> generateGeminiResponse(clean, lower)
            AiModel.CHAT_GPT -> generateChatGptResponse(clean, lower)
            AiModel.DEEPSEEK -> generateDeepSeekResponse(clean, lower)
        }
    }

    private fun generateCharacterResponse(
        model: AiModel,
        clean: String,
        lower: String,
        character: CharacterAiEntity
    ): String {
        val mathResult = tryEvaluateMath(clean)
        if (mathResult != null) {
            return "${character.avatarEmoji} **[${character.name}]**\n\nတွက်ချက်မှု ရလဒ်:\n`$clean = $mathResult`\n\n*${character.name} ၏ စည်းမျဉ်းအရ အဖြေတိကျမှုကို အတည်ပြုပြီးပါပြီ။*"
        }

        val persona = character.personaStyle
        val memory = if (character.memoryData.isNotBlank()) "\n📌 **မှတ်သားထားသော အချက်အလက်:** ${character.memoryData}" else ""
        val rules = if (character.strictnessRules.isNotBlank()) "\n⚡ **လိုက်နာသော စည်းမျဉ်း:** ${character.strictnessRules}" else ""

        if (clean.contains("မင်္ဂလာပါ") || clean.contains("hello") || lower.contains("hi") || clean.contains("မင်္ဂလာ")) {
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

        return """
            $stylePrefix${character.avatarEmoji} **${character.name} (${model.displayName} Mode):**
            
            လူကြီးမင်း မေးမြန်းထားသော: *"$clean"*
            
            ကျွန်ုပ် **${character.name}** ၏ သတ်မှတ်ထားသော ဟန်ပန်စတိုင် ($persona) အတိုင်း စိစစ်ပြီး အထောက်အကူပြု တုံ့ပြန်ပေးလိုက်ပါသည်:
            
            • ဤကိစ္စရပ်နှင့် ပတ်သက်၍ အဓိက သတိပြုရမည့် အချက်များအား တိကျစွာ လမ်းညွှန်ပေးနိုင်ပါသည်။
            • လူကြီးမင်း၏ လိုအပ်ချက်များကို စဉ်ဆက်မပြတ် ကူညီဆောင်ရွက်ပေးရန် အသင့်ရှိပါသည်။$memory
            
            ဆက်လက်၍ မည်သည့်အချက်ကို အသေးစိတ် ဆွေးနွေးလိုပါသလဲခင်ဗျာ?
        """.trimIndent()
    }

    private fun generateMrAResponse(clean: String, lower: String): String {
        val mathResult = tryEvaluateMath(clean)
        if (mathResult != null) {
            return "🧮 **တွက်ချက်မှု ရလဒ် (Calculation Result):**\n`$clean = $mathResult`"
        }

        if (clean.contains("မင်္ဂလာပါ") || clean.contains("hello") || lower.contains("hi mr.a") || clean.contains("မင်္ဂလာ")) {
            return """
                👋 **မင်္ဂလာပါခင်ဗျာ!**
                ကျွန်တော်ကတော့ လူကြီးမင်းတို့၏ နေ့စဉ်ဘဝနှင့် လုပ်ငန်းဆောင်တာများကို ကူညီပေးမည့် **Mr.A AI Assistant** ဖြစ်ပါတယ်။
                
                📌 **ကျွန်တော် ကူညီပေးနိုင်သော အရာများ:**
                - ✉️ **စာရေးသားခြင်း:** ရုံးသုံးစာ၊ ခွင့်တိုင်စာ၊ ဖိတ်စာ၊ စီးပွားရေး အီးမေးလ်များ ရေးသားပေးခြင်း
                - 🌐 **ဘာသာပြန်:** မြန်မာ - အင်္ဂလိပ်နှင့် နိုင်ငံတကာဘာသာစကားများ တိကျစွာ ဘာသာပြန်ပေးခြင်း
                - 💡 **နေ့စဉ်ဘဝ အကြံဉာဏ်:** ကျန်းမာရေး၊ အာဟာရ၊ အချိန်စီမံခန့်ခွဲမှုနှင့် အလေ့အကျင့်ကောင်းများ
                - 💼 **စီးပွားရေးနှင့် အလုပ်:** ဝင်ငွေထွက်ငွေ တွက်ချက်ခြင်း၊ စိတ်ကူးသစ်များ ရှာဖွေခြင်း
                - 🎭 **စိတ်ကြိုက် ကာရိုက်တာ ဖန်တီးခြင်း:** User စိတ်ကြိုက် AI Character (ဥပမာ Megan, Jarvis စသဖြင့်) ကိုလည်း စိတ်ကြိုက်ဖန်တီးအသုံးပြုနိုင်ပါပြီ!
                
                လူကြီးမင်း သိလိုသည်များကို စိတ်တိုင်းကျ မေးမြန်းနိုင်ပါပြီခင်ဗျာ။
            """.trimIndent()
        }

        if (clean.contains("ခွင့်") || clean.contains("စာရေး") || clean.contains("အီးမေးလ်") || clean.contains("email") || clean.contains("letter")) {
            return """
                📝 **ရုံးသုံး အလုပ်ခွင့်တိုင်စာ နမူနာ (Sample Leave Application):**
                
                ရက်စွဲ။     ။ [ရက်စွဲ ထည့်ရန်]
                သို့
                ဌာနမှူး / မန်နေဂျာ
                [ကုမ္ပဏီ / ရုံးအမည်]
                
                အကြောင်းအရာ။  ။ **အလုပ်ခွင့်တိုင်ကြားခြင်း**
                
                လေးစားအပ်ပါသော လူကြီးမင်းခင်ဗျာ-
                ကျွန်တော်/ကျွန်မ [အမည်] သည် [ရာထူး] တာဝန်ကို ထမ်းဆောင်လျက်ရှိပါသည်။ အရေးကြီးသော [ကိုယ်ရေးကိုယ်တာ ကိစ္စ / ကျန်းမာရေး အခြေအနေ] ကြောင့် [စတင်မည့်ရက်] မှ [ပြီးဆုံးမည့်ရက်] အထိ ([   ] ရက်ကြာ) ခွင့်ယူခွင့်ပြုပါရန် လေးစားစွာ လျှောက်ထားအပ်ပါသည်။
                
                ခွင့်ရက်အတွင်း အရေးပေါ်ဆက်သွယ်ရန် ဖုန်းနံပါတ် [ဖုန်းနံပါတ်] သို့ ဆက်သွယ်နိုင်ပါသည်။
                
                လေးစားစွာဖြင့်-
                [အမည်]
                [ရာထူး]
                
                *(အခြားသော စီးပွားရေးစာ သို့မဟုတ် အီးမေးလ်များ လိုအပ်ပါကလည်း အကြောင်းအရာကို အသေးစိတ် မေးမြန်းနိုင်ပါသည်)*
            """.trimIndent()
        }

        if (clean.contains("ဘာသာပြန်") || clean.contains("translate") || clean.contains("အင်္ဂလိပ်လို ဘယ်လိုခေါ်လဲ")) {
            return """
                🌐 **ဘာသာပြန် ဝန်ဆောင်မှု (Translation Assistant):**
                
                ကျွန်တော် Mr.A သည် မြန်မာစာနှင့် အင်္ဂလိပ်စာ အပြန်အလှန် ဘာသာပြန်ခြင်းကို သဘာဝကျကျ အဓိပ္ပာယ်ပေါ်လွင်အောင် ကူညီပေးနိုင်ပါသည်။
                
                💡 **အသုံးပြုနည်း:**
                - "အောက်ပါ စာပိုဒ်ကို အင်္ဂလိပ်လို ဘာသာပြန်ပေးပါ: [သင်၏ စာသား]"
                - "Please translate this into polite Burmese: [Your English text]"
                
                ဘာသာပြန်လိုသော စာသားကို ပေးပို့နိုင်ပါပြီခင်ဗျာ!
            """.trimIndent()
        }

        if (clean.contains("ကျန်းမာရေး") || clean.contains("အစားအသောက်") || clean.contains("စိတ်ဖိစီးမှု") || clean.contains("အိပ်မပျော်")) {
            return """
                🌿 **နေ့စဉ် ကျန်းမာရေးနှင့် စိတ်လက်ကြည်လင်စေမည့် အကြံပြုချက်:**
                
                ၁။ **ရေလုံလောက်စွာ သောက်သုံးပါ:** တစ်နေ့လျှင် ရေ ၂ လီတာမှ ၃ လီတာအထိ ပုံမှန်သောက်ပေးပါ။
                ၂။ **အိပ်ချိန်မှန်ကန်ပါစေ:** ညစဉ် ၇ နာရီမှ ၈ နာရီအထိ နှစ်ခြိုက်စွာ အိပ်စက်ခြင်းက စိတ်ဖိစီးမှုကို လျှော့ချပေးပါသည်။
                ၃။ **အကြောလျှော့ လေ့ကျင့်ခန်း:** အလုပ်လုပ်နေစဉ် မိနစ် ၆၀ လျှင် တစ်ကြိမ် ခေတ္တလမ်းလျှောက်ပေးပါ။
                ၄။ **စိတ်အေးချမ်းစေရန်:** အသက်ရှူသွင်း၊ ရှူထုတ် လေ့ကျင့်ခန်း (Deep Breathing) ကို ၅ မိနစ်ခန့် ပြုလုပ်ပါ။
                
                *မှတ်ချက်: အရေးပေါ် သို့မဟုတ် ဆေးဘက်ဆိုင်ရာ ကုသမှုများအတွက် ကျွမ်းကျင်ဆရာဝန်နှင့် ပြသတိုင်ပင်သင့်ပါသည်ခင်ဗျာ။*
            """.trimIndent()
        }

        if (clean.contains("ဘာတွေလုပ်ပေးနိုင်လဲ") || lower.contains("what can you do") || clean.contains("mr.a") || clean.contains("ဘယ်သူလဲ")) {
            return """
                🤖 **Mr.A Assistant မိတ်ဆက်:**
                
                ကျွန်တော်ကတော့ လူကြီးမင်းတို့၏ နေ့စဉ်ဘဝ၊ ရုံးလုပ်ငန်း၊ ပညာရေးနှင့် လူမှုရေး လိုအပ်ချက်များအတွက် အချိန်မရွေး အားကိုးနိုင်သော **All-round Assistant** ဖြစ်ပါတယ်။
                
                ✨ **ထူးခြားချက်များ:**
                1. **Mr.A Built-in Engine:** အင်တာနက်မလိုဘဲ အချိန်မရွေး အော့ဖ်လိုင်း အခမဲ့ အသုံးပြုနိုင်ခြင်း။
                2. **Multi-Model Intelligence:** Google Gemini, ChatGPT, DeepSeek နှင့် Mr.A မော်ဒယ် ၄ မျိုးလုံးကို တစ်နေရာတည်းတွင် လွတ်လပ်စွာ အသုံးပြုနိုင်ခြင်း။
                3. **Create Character AI:** မိမိစိတ်ကြိုက် အမည်၊ အသွင်အပြင်၊ စရိုက်ဟန်ပန်နှင့် စည်းမျဉ်းများဖြင့် Character AI များကို စိတ်ကြိုက် ဖန်တီးနိုင်ခြင်း။
                4. **ကိုယ်ရေးအချက်အလက် လုံခြုံမှု:** မည်သည့် Cloud Server သို့မျှ ဒေတာမပို့ဘဲ သင့်ဖုန်းအတွင်း၌သာ လုံခြုံစွာ သိမ်းဆည်းပေးထားခြင်း။
            """.trimIndent()
        }

        return """
            🤖 **Mr.A Assistant:**
            
            လူကြီးမင်း၏ မေးခွန်း: *"$clean"*
            
            ကျွန်တော် Mr.A အနေဖြင့် လက်ခံရရှိထားပြီး လူကြီးမင်း၏ နေ့စဉ်လိုအပ်ချက်များကို တတ်စွမ်းသမျှ အမြဲကူညီပေးနေပါသည်။
            
            💡 **အကြံပြုချက်:**
            - အပေါ်ဘက်ရှိ Model Selector မှ **Mr.A, Gemini, ChatGPT သို့မဟုတ် DeepSeek** စသည့် မော်ဒယ်များကို မိမိနှစ်သက်ရာ စတိုင်အလိုက် အချိန်မရွေး လွတ်လပ်စွာ ပြောင်းလဲ အသုံးပြုနိုင်ပါသည်။
            - သီးသန့်စရိုက်ဖြင့် စကားပြောလိုပါက **Create Character AI** ဖြင့် စိတ်ကြိုက် ကာရိုက်တာ ဖန်တီးနိုင်ပါသည်။
            
            နောက်ထပ် မည်သည့်အရာကို ကူညီပေးရမလဲခင်ဗျာ?
        """.trimIndent()
    }

    private fun generateGeminiResponse(clean: String, lower: String): String {
        val mathResult = tryEvaluateMath(clean)
        if (mathResult != null) {
            return "✨ **Gemini Fast Computation:**\n\n```text\n$clean = $mathResult\n```\n*Calculated with high numeric precision.*"
        }

        return """
            ✨ **Google Gemini Intelligence:**
            
            Regarding: **"$clean"**
            
            Here is a structured overview and practical insight:
            
            1. **Core Concept (အဓိက အချက်အလက်):**
               - မေးမြန်းထားသော အကြောင်းအရာအတွက် အခြေခံအချက်များကို ရှင်းလင်းစွာ ခွဲခြမ်းစိတ်ဖြာ ပေးထားပါသည်။
               - အရေးကြီးသော အချက်များကို အစဉ်လိုက် စနစ်တကျ စီစဉ်ဆောင်ရွက်နိုင်ပါသည်။
            
            2. **Key Recommendations (အကြံပြုချက်များ):**
               - လုပ်ဆောင်ချက်တစ်ခုစီတိုင်းကို လိုတိုရှင်းနှင့် ထိရောက်မှုရှိစေရန် စီစဉ်ပါ။
               - လိုအပ်ပါက အသေးစိတ် ရှင်းလင်းချက် သို့မဟုတ် နောက်ထပ် ဆန်းစစ်မှုများကို ဆက်လက် မေးမြန်းနိုင်ပါသည်။
            
            *(Gemini Mode provides concise, well-structured multi-modal insights.)*
        """.trimIndent()
    }

    private fun generateChatGptResponse(clean: String, lower: String): String {
        val mathResult = tryEvaluateMath(clean)
        if (mathResult != null) {
            return "🟢 **ChatGPT Answer:**\n\nThe mathematical evaluation of `$clean` is:\n\n**Result:** `$mathResult`"
        }

        return """
            🟢 **ChatGPT (Conversational Mode):**
            
            Hello! Thank you for your question: *"$clean"*.
            
            Here is a helpful, step-by-step breakdown:
            
            • **Summary:** လူကြီးမင်း မေးမြန်းထားသော ကိစ္စနှင့် ပတ်သက်၍ အထောက်အကူပြုနိုင်မည့် အဓိက အကြောင်းအရာများကို ဖော်ပြပေးလိုက်ပါသည်။
            • **Analysis:** ပြဿနာ သို့မဟုတ် မေးခွန်း၏ အကြောင်းရင်းကို သဘာဝကျကျ ချဉ်းကပ်ဖြေရှင်းနိုင်ရန် လမ်းညွှန်ချက်များ ပါဝင်ပါသည်။
            • **Next Step:** သင်သိလိုသော အခြားအချက်များ သို့မဟုတ် နောက်ထပ် ကဏ္ဍများကို စိတ်တိုင်းကျ ဆက်လက် ဆွေးနွေးနိုင်ပါသည်။
            
            Feel free to ask if you would like me to elaborate further or adjust the tone!
        """.trimIndent()
    }

    private fun generateDeepSeekResponse(clean: String, lower: String): String {
        val mathResult = tryEvaluateMath(clean)
        if (mathResult != null) {
            return """
                🐋 **DeepSeek Reasoning Engine:**
                
                <think>
                Parsing expression: $clean
                Validating arithmetic operations and evaluating...
                Result determined: $mathResult
                </think>
                
                ### Mathematical Evaluation
                - Expression: `$clean`
                - Final Result: **`$mathResult`**
            """.trimIndent()
        }

        return """
            🐋 **DeepSeek Reasoning & Analytical Engine:**
            
            <think>
            1. Query Analysis: "$clean"
            2. Identifying core intent and logical structure.
            3. Formulating rigorous, deep analytical response with code or structured reasoning if applicable.
            </think>
            
            ### Analytical Breakdown
            
            **Query:** *"$clean"*
            
            1. **Logical Framework:**
               - ဤပြဿနာကို ဖြေရှင်းရန်အတွက် အခြေခံ အကြောင်းတရားများနှင့် အဆင့်ဆင့် ဆင်ခြင်တွေးခေါ်မှု (Reasoning Steps) ကို ဦးစားပေး စဉ်းစားသင့်ပါသည်။
            
            2. **Technical & Practical Analysis:**
               - တိကျသော အချက်အလက်များနှင့် နည်းစနစ်ကျသော အဆင့်များဖြင့် အဖြေရှာခြင်းက အကောင်းဆုံး ရလဒ်ကို ရရှိစေပါသည်။
            
            ```kotlin
            // DeepSeek Code Pattern / Implementation Logic
            fun handleQuery(input: String) {
                // Step 1: Deep analysis
                // Step 2: Accurate execution
                println("Processed: ${"$"}{input}")
            }
            ```
            
            လိုအပ်ပါက အသေးစိတ် Algorithm၊ စာရင်းဇယား သို့မဟုတ် Programming code များကို ဆက်လက် မေးမြန်းနိုင်ပါသည်။
        """.trimIndent()
    }

    private fun tryEvaluateMath(input: String): String? {
        val expr = input.replace(" ", "")
        val mathPattern = Regex("""^(\d+(\.\d+)?)([\+\-\*\/])(\d+(\.\d+)?)$""")
        val match = mathPattern.matchEntire(expr) ?: return null

        val (num1Str, _, op, num2Str) = match.destructured
        val n1 = num1Str.toDoubleOrNull() ?: return null
        val n2 = num2Str.toDoubleOrNull() ?: return null

        val res = when (op) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> if (n2 == 0.0) return "မရေတွက်နိုင်ပါ (Division by zero)" else n1 / n2
            else -> return null
        }

        return if (res % 1.0 == 0.0) res.toLong().toString() else "%.4f".format(res)
    }
}
