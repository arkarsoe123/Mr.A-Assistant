package com.example.data.remote.brain

import java.util.Locale

object KnowledgeBrain {

    fun trySolve(input: String): String? {
        val clean = input.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // 1. AI, LLM, Machine Learning
        if (clean.contains("ai ဆိုတာ") || lower.contains("artificial intelligence") ||
            lower.contains("llm") || clean.contains("ဉာဏ်ရည်တု") || lower.contains("machine learning") ||
            clean.contains("prompt engineering")
        ) {
            return solveAiConcepts(clean, lower)
        }

        // 2. Health & Wellness
        if (clean.contains("ကျန်းမာရေး") || clean.contains("အစားအသောက်") || clean.contains("အိပ်မပျော်") ||
            clean.contains("စိတ်ဖိစီး") || clean.contains("ကယ်လိုရီ") || lower.contains("health")
        ) {
            return solveHealth()
        }

        // 3. Finance & Budgeting
        if (clean.contains("ငွေစု") || clean.contains("ဘတ်ဂျက်") || clean.contains("ငွေကြေး") ||
            clean.contains("ရင်းနှီးမြှုပ်နှံ") || lower.contains("finance") || lower.contains("budget")
        ) {
            return solveFinance()
        }

        // 4. Productivity & Time Management
        if (clean.contains("အချိန်") || clean.contains("အလုပ်ပြီးမြောက်") || clean.contains("pomodoro") ||
            clean.contains("စီမံခန့်ခွဲ") || lower.contains("productivity")
        ) {
            return solveProductivity()
        }

        // 5. Myanmar Culture, Places & Heritage
        if (clean.contains("မြန်မာ") || clean.contains("ပုဂံ") || clean.contains("ရန်ကုန်") ||
            clean.contains("မန္တလေး") || clean.contains("သင်္ကြန်") || clean.contains("အင်းလေး")
        ) {
            return solveMyanmarCulture(clean)
        }

        // 6. Science, Space & Physics
        if (clean.contains("နေအဖွဲ့အစည်း") || clean.contains("ကမ္ဘာ") || clean.contains("အာကာသ") ||
            clean.contains("ဆွဲငင်အား") || lower.contains("solar system") || lower.contains("gravity")
        ) {
            return solveScience(clean, lower)
        }

        return null
    }

    private fun solveAiConcepts(clean: String, lower: String): String {
        return """
            🧠 **ဉာဏ်ရည်တု (Artificial Intelligence & LLMs) ရှင်းလင်းချက်:**
            
            1. **AI ဆိုသည်မှာ အဘယ်နည်း?**
               - ကွန်ပျူတာနှင့် စက်ပစ္စည်းများအား လူသားများကဲ့သို့ တွေးခေါ်ခြင်း၊ သင်ယူလေ့လာခြင်းနှင့် ပြဿနာဖြေရှင်းနိုင်စေရန် ဖန်တီးထားသော နည်းပညာဖြစ်ပါသည်။
            
            2. **LLM (Large Language Model):**
               - Gemini, ChatGPT, DeepSeek စသည့် မော်ဒယ်များသည် စာသားပေါင်း ဘီလီယံချီသော ဒေတာများကို သင်ယူထားပြီး သဘာဝကျကျ ပြန်လည်ရေးသား ဖြေကြားပေးနိုင်သော အဆင့်မြင့် စာသား အင်ဂျင်များ ဖြစ်ကြပါသည်။
            
            3. **Prompt Engineering အကြံပြုချက်:**
               - **Role ပေးပါ:** "သင်သည် ဝါရင့် ဆော့ဖ်ဝဲအင်ဂျင်နီယာ တစ်ဦးအဖြစ်..."
               - **ရှင်းလင်းသော ညွှန်ကြားချက်ပေးပါ:** ရလဒ်ကို အချက် ၃ ချက်ဖြင့် ရှင်းပြပါ သို့မဟုတ် ဇယားပုံစံ ထုတ်ပေးပါ။
               - **ဥပမာ ထည့်သွင်းပေးပါ (Few-shot prompting):** လိုချင်သော ပုံစံနမူနာ ပေးထားခြင်းဖြင့် ပိုမိုတိကျသော အဖြေရရှိစေပါသည်။
        """.trimIndent()
    }

    private fun solveHealth(): String {
        return """
            🌿 **ကျန်းမာရေးနှင့် နေထိုင်မှုပုံစံ အကြံပြုချက် (Health & Wellness):**
            
            ၁။ **ရေသောက်သုံးမှု:**
               - နေ့စဉ် ရေ ၂.၅ လီတာမှ ၃ လီတာခန့်ကို တစ်နေ့တာလုံး အညီအမျှ ခွဲဝေ သောက်သုံးပါ။
            
            ၂။ **အာဟာရ မျှတမှု:**
               - အသီးအရွက်နှင့် အမျှင်ဓာတ် များများစားသုံးပါ။
               - အချို၊ အငန်နှင့် အဆီများသော အစားအစာများကို လျှော့ချပါ။
            
            ၃။ **နှစ်ခြိုက်စွာ အိပ်စက်ခြင်း (Sleep Hygiene):**
               - ညအိပ်ရာမဝင်မီ ၁ နာရီအလိုတွင် ဖုန်းနှင့် စခရင်များကို ရှောင်ကြဉ်ပါ။
               - ညစဉ် ၇ နာရီမှ ၈ နာရီအထိ အချိန်မှန် အိပ်စက်ပါ။
            
            ၄။ **စိတ်ဖိစီးမှု လျှော့ချခြင်း:**
               - မိနစ် ၂၀ ခန့် လမ်းလျှောက်ခြင်း သို့မဟုတ် အသက်ရှူ လေ့ကျင့်ခန်း လုပ်ဆောင်ပါ။
               
            *(မှတ်ချက်: တိကျသော ကျန်းမာရေးကုသမှုများအတွက် အထူးကုဆရာဝန်နှင့် တိုင်ပင်ပါ)*
        """.trimIndent()
    }

    private fun solveFinance(): String {
        return """
            💰 **ကိုယ်ပိုင် ငွေကြေးစီမံခန့်ခွဲမှု အခြေခံ (Personal Finance 50/30/20 Rule):**
            
            ၁။ **၅၀% - မရှိမဖြစ် လိုအပ်ချက်များ (Needs):**
               - အိမ်လခ၊ စားသောက်စရိတ်၊ ရေမီး၊ သွားလာစရိတ်နှင့် ကျန်းမာရေး။
            
            ၂။ **၃၀% - စိတ်ကြိုက် လိုအင်ဆန္ဒများ (Wants):**
               - အပန်းဖြေခြင်း၊ စျေးဝယ်ခြင်း၊ ဝါသနာပါရာ အသုံးစရိတ်များ။
            
            ၃။ **၂၀% - စုဆောင်းငွေနှင့် ရင်းနှီးမြှုပ်နှံမှု (Savings & Investments):**
               - အရေးပေါ် ရန်ပုံငွေ (Emergency Fund: အနည်းဆုံး ၃ လမှ ၆ လစာ ကုန်ကျစရိတ်)။
               - အနာဂတ် ရင်းနှီးမြှုပ်နှံမှုနှင့် စုငွေ။
            
            💡 **အကြံပြုချက်:** နေ့စဉ် အသုံးစရိတ်များကို မှတ်တမ်းတင်ထားခြင်းဖြင့် မလိုအပ်သော ငွေယိုပေါက်များကို သိရှိနိုင်ပါသည်။
        """.trimIndent()
    }

    private fun solveProductivity(): String {
        return """
            ⏱️ **အချိန်စီမံခန့်ခွဲမှုနှင့် အလုပ်ပြီးမြောက်မှု နည်းလမ်းများ (Productivity Techniques):**
            
            ၁။ **Pomodoro Technique (၂၅/၅ နည်းလမ်း):**
               - အလုပ်ကို ၂၅ မိနစ် အာရုံစိုက်လုပ်ပါ။
               - ပြီးလျှင် ၅ မိနစ် ခေတ္တအနားယူပါ။
               - ၄ ကြိမ်ပြည့်ပါက ၁၅-၃၀ မိနစ် အနားယူပါ။
            
            ၂။ **Eisenhower Matrix (ဦးစားပေး အဆင့်ခွဲခြားခြင်း):**
               - **အရေးကြီးပြီး အရေးပေါ်:** ချက်ချင်းလုပ်ပါ။
               - **အရေးကြီးသော်လည်း အရေးမပေါ်:** အချိန်ဇယားဆွဲ၍ စနစ်တကျလုပ်ပါ။
               - **အရေးမကြီးသော်လည်း အရေးပေါ်:** အခြားသူထံ လွှဲအပ်ပါ။
               - **အရေးလည်းမကြီး၊ အရေးလည်းမပေါ်:** ဖယ်ရှားပစ်ပါ။
        """.trimIndent()
    }

    private fun solveMyanmarCulture(clean: String): String {
        return """
            🇲🇲 **မြန်မာ့ယဉ်ကျေးမှုနှင့် သမိုင်းဝင် အမွေအနှစ်များ (Myanmar Heritage):**
            
            • **ပုဂံရှေးဟောင်းယဉ်ကျေးမှုနယ်မြေ:** UNESCO ကမ္ဘာ့အမွေအနှစ်စာရင်းဝင်ဖြစ်ပြီး ၁၁ ရာစုမှ ၁၃ ရာစုအတွင်း တည်ထားခဲ့သော စေတီပုထိုးပေါင်း ၂၀၀၀ ကျော် တည်ရှိပါသည်။
            • **ရွှေတိဂုံစေတီတော်:** ရန်ကုန်မြို့၏ အထင်ကရ သမိုင်းဝင် တန်ခိုးကြီး စေတီတော်မြတ်ကြီးဖြစ်ပြီး ဉာဏ်တော် ၃၂၆ ပေ မြင့်မားပါသည်။
            • **အင်းလေးကန်:** ရှမ်းပြည်နယ်ရှိ သဘာဝကန်ကြီးဖြစ်ပြီး ခြေထောက်ဖြင့် လှေလှော်ခတ်သော အင်းသားရိုးရာဓလေ့နှင့် ရေပေါ်ကျွန်းစိုက်ခင်းများဖြင့် ထင်ရှားပါသည်။
            • **မြန်မာ့ရိုးရာ မဟာသင်္ကြန်:** နှစ်ဟောင်းမှ အညစ်အကြေးများကို ရေစင်ပက်ဖျန်း ဆေးကြောသည့် နှစ်သစ်ကူး ရိုးရာပွဲတော်ဖြစ်ပါသည်။
        """.trimIndent()
    }

    private fun solveScience(clean: String, lower: String): String {
        return """
            🌌 **နေအဖွဲ့အစည်းနှင့် စကြဝဠာ အချက်အလက်များ (Solar System & Universe):**
            
            • **ဗဟိုချက်:** နေ (Sun) သည် နေအဖွဲ့အစည်း ဒြပ်ထုစုစုပေါင်း၏ ၉၉.၈% ကျော်ကို ပိုင်ဆိုင်ထားသည်။
            • **ဂြိုဟ်များ အစဉ်လိုက်:** ဗုဒ္ဓဟူး (Mercury)၊ သောကြာ (Venus)၊ ကမ္ဘာ (Earth)၊ အင်္ဂါ (Mars)၊ ကြာသပတေး (Jupiter)၊ စနေ (Saturn)၊ ယူရေးနပ်စ် (Uranus)၊ နက်ပကျွန်း (Neptune)။
            • **အလင်းအလျင် (Speed of Light):** အလင်းသည် တစ်စက္ကန့်လျှင် ကီလိုမီတာ ၃၀၀,၀၀၀ (မိုင်ပေါင်း ၁၈၆,၀၀၀) ခန့် ပြေးသွားနိုင်သည်။
            • **ဆွဲငင်အား (Gravity):** ကမ္ဘာ့ဆွဲငင်အားသည် အရာဝတ္ထုအားလုံးကို ၉.၈ m/s² နှုန်းဖြင့် ဗဟိုသို့ ဆွဲငင်ထားပါသည်။
        """.trimIndent()
    }
}
