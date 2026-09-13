package com.example.data.remote.brain

import java.util.Locale

object WritingBrain {

    fun trySolve(input: String): String? {
        val clean = input.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // 1. Leave Letter
        if (clean.contains("ခွင့်") || lower.contains("leave letter")) {
            return solveLeaveLetter()
        }

        // 2. Resignation Letter
        if (clean.contains("အလုပ်ထွက်") || lower.contains("resignation")) {
            return solveResignationLetter()
        }

        // 3. Job Application / Cover Letter
        if (clean.contains("အလုပ်လျှောက်") || lower.contains("cover letter") || lower.contains("job application")) {
            return solveCoverLetter()
        }

        // 4. Business Email
        if (clean.contains("အီးမေးလ်") || lower.contains("email") || clean.contains("email ရေးနည်း")) {
            return solveBusinessEmail(clean)
        }

        // 5. Social Media Caption / Marketing Post
        if (clean.contains("caption") || clean.contains("ပို့စ်") || clean.contains("ကြော်ငြာ") ||
            clean.contains("marketing") || clean.contains("စာသား")
        ) {
            return solveSocialPost(clean)
        }

        // 6. Motivation / Quotes / Poems
        if (clean.contains("ကဗျာ") || clean.contains("စိတ်ဓာတ်") || clean.contains("ခွန်အား") ||
            clean.contains("quote") || clean.contains("motivation")
        ) {
            return solveMotivation(clean)
        }

        return null
    }

    private fun solveLeaveLetter(): String {
        return """
            📝 **ရုံးသုံး အလုပ်ခွင့်တိုင်ကြားလွှာ ပုံစံ (Leave Application Letter):**
            
            ရက်စွဲ။     ။ [ရက်စွဲ ထည့်ရန်]
            သို့
            ဌာနမှူး / မန်နေဂျာ
            [ဌာနအမည် / ကုမ္ပဏီအမည်]
            
            အကြောင်းအရာ။  ။ **အလုပ်ခွင့်တိုင်ကြားခြင်း**
            
            လေးစားအပ်ပါသော လူကြီးမင်းခင်ဗျာ-
            
            ကျွန်တော်/ကျွန်မ [မိမိအမည်] သည် [ဌာနအမည်] တွင် [ရာထူး] တာဝန်ကို ထမ်းဆောင်လျက်ရှိပါသည်။
            
            အရေးကြီးသော [ကိုယ်ရေးကိုယ်တာ ကိစ္စ / ကျန်းမာရေး အခြေအနေ] ကြောင့် [စတင်မည့်ရက်] မှ [ပြီးဆုံးမည့်ရက်] အထိ ([  ] ရက်ကြာ) ခွင့်ယူခွင့်ပြုပါရန် ရိုသေလေးစားစွာ လျှောက်ထားအပ်ပါသည်။
            
            ခွင့်ယူထားသည့် ကာလအတွင်း မိမိ၏ လုပ်ငန်းတာဝန်များကို လုပ်ဖော်ကိုင်ဖက် [လွှဲပြောင်းမည့်သူ အမည်] ထံသို့ စနစ်တကျ လွှဲပြောင်းထားရှိမည်ဖြစ်ပြီး အရေးပေါ် ဆက်သွယ်စရာရှိပါက ဖုန်း [ဖုန်းနံပါတ်] သို့ ဆက်သွယ်နိုင်ပါသည်။
            
            သို့ဖြစ်ပါ၍ ခွင့်ရက်အား ခွင့်ပြုပေးပါရန် လေးစားစွာ တင်ပြအပ်ပါသည်။
            
            လေးစားစွာဖြင့်-
            [မိမိလက်မှတ်]
            [မိမိအမည်]
            [ရာထူး]
        """.trimIndent()
    }

    private fun solveResignationLetter(): String {
        return """
            📝 **အလုပ်ထွက်ခွင့် လျှောက်လွှာ ပုံစံ (Formal Resignation Letter):**
            
            ရက်စွဲ။     ။ [ရက်စွဲ ထည့်ရန်]
            သို့
            စီမံခန့်ခွဲရေး ဒါရိုက်တာ / ဌာနမှူး
            [ကုမ္ပဏီအမည်]
            
            အကြောင်းအရာ။  ။ **တာဝန်မှ နုတ်ထွက်ခွင့် ပြုပါရန် လျှောက်ထားခြင်း**
            
            လေးစားအပ်ပါသော လူကြီးမင်းခင်ဗျာ-
            
            ကျွန်တော်/ကျွန်မ [မိမိအမည်] သည် [ကုမ္ပဏီအမည်] ၌ [ရာထူး] အဖြစ် တာဝန်ထမ်းဆောင်ခွင့် ရရှိခဲ့သည့်အတွက် အထူးပင် ကျေးဇူးတင်ရှိပါသည်။
            
            ယခုအခါ [ကိုယ်ရေးကိုယ်တာ အကြောင်းပြချက် / အသက်မွေးဝမ်းကျောင်းဆိုင်ရာ ရွေးချယ်မှုအသစ်] ကြောင့် [နောက်ဆုံးအလုပ်ဆင်းမည့်ရက်] တွင် မိမိ၏ တာဝန်များမှ နုတ်ထွက်ခွင့် ပြုပါရန် တင်ပြအပ်ပါသည်။
            
            ကုမ္ပဏီ၌ တာဝန်ထမ်းဆောင်စဉ် ကာလတစ်လျှောက်လုံးတွင် လမ်းညွှန်သင်ကြားပေးခဲ့ကြသော လူကြီးမင်းများနှင့် လုပ်ဖော်ကိုင်ဖက်များ အားလုံးကို အထူးကျေးဇူးတင်ရှိပြီး ကုမ္ပဏီကြီး ပိုမိုအောင်မြင် တိုးတက်ပါစေကြောင်း ဆုမွန်ကောင်း တောင်းအပ်ပါသည်။
            
            လေးစားစွာဖြင့်-
            [မိမိလက်မှတ်]
            [မိမိအမည်]
            [ရာထူး]
        """.trimIndent()
    }

    private fun solveCoverLetter(): String {
        return """
            💼 **အလုပ်လျှောက်လွှာ (Job Cover Letter) နမူနာ:**
            
            Dear Hiring Manager,
            
            I am writing to express my strong interest in the **[Position Title]** role at **[Company Name]**. With my background in [Your Field/Expertise] and proven experience in [Key Skills], I am confident in contributing significantly to your team's success.
            
            Key highlights I bring:
            • Over [X] years of experience in [Core Skills].
            • Strong problem-solving abilities, teamwork, and proactive communication.
            • Dedication to delivering high-quality results on schedule.
            
            Thank you for considering my application. I look forward to the opportunity to discuss my qualifications further.
            
            Sincerely,
            [Your Name]
            [Contact Information & LinkedIn]
        """.trimIndent()
    }

    private fun solveBusinessEmail(clean: String): String {
        return """
            ✉️ **ရုံးသုံး စီးပွားရေး အီးမေးလ် ပုံစံ (Professional Business Email):**
            
            **Subject:** [ပရောဂျက်အမည် / အကြောင်းအရာ အကျဉ်း] - Update / Discussion
            
            Dear [Recipient Name],
            
            I hope this email finds you well.
            
            I am writing to provide a quick update regarding **[Topic / Project]**. We have successfully completed the initial phase and are on track for the upcoming milestone.
            
            **Key Points:**
            1. [အဓိက အချက်အလက် (၁)]
            2. [အဓိက အချက်အလက် (၂)]
            
            Please let me know if you have any questions or if you would like to schedule a brief call this week.
            
            Best regards,
            
            [Your Name]  
            [Your Title]  
            [Company Name]
        """.trimIndent()
    }

    private fun solveSocialPost(clean: String): String {
        return """
            📱 **လူမှုကွန်ရက် ပို့စ်နှင့် ကြော်ငြာ စာသား နမူနာ (Social Media Post Template):**
            
            ✨ **[စိတ်ဝင်စားဖွယ် ခေါင်းစဉ်ကြီး (Catchy Headline)]** ✨
            
            လူကြီးမင်းတို့ရဲ့ နေ့စဉ်ဘဝ/လုပ်ငန်းခွင်အတွက် အကောင်းမွန်ဆုံး အထောက်အကူပြုမည့် [ထုတ်ကုန် သို့မဟုတ် ဝန်ဆောင်မှုအမည်] ရောက်ရှိလာပါပြီ!
            
            🔥 **အဓိက အားသာချက်များ:**
            ✅ [အချက် ၁: အချိန်ကုန်သက်သာစေခြင်း]
            ✅ [အချက် ၂: စိတ်ချရသော အရည်အသွေးနှင့် ဝန်ဆောင်မှု]
            ✅ [အချက် ၃: သင့်တင့်သော စျေးနှုန်း]
            
            👉 အသေးစိတ် စုံစမ်းမေးမြန်းလိုပါက Messenger သို့မဟုတ် ဖုန်း [ဖုန်းနံပါတ်] သို့ အချိန်မရွေး ဆက်သွယ်နိုင်ပါသည်။
            
            #Business #Service #Myanmar #Quality #Offer
        """.trimIndent()
    }

    private fun solveMotivation(clean: String): String {
        return """
            🌟 **စိတ်ခွန်အားဖြည့် အတွေးစကား (Words of Encouragement):**
            
            > *"အောင်မြင်မှုဆိုတာ တစ်ရက်တည်းနဲ့ ရောက်လာတာ မဟုတ်ပါဘူး။  
            > နေ့စဉ်နေ့တိုင်း သင်စိုက်ထုတ်လိုက်တဲ့ သေးငယ်တဲ့ ကြိုးစားမှုတွေ ပေါင်းစပ်ရာကနေ ဖြစ်ပေါ်လာတာပါ။"*
            
            🌿 **မှတ်သားဖွယ်ရာ အချက်များ:**
            ၁။ အမှားဆိုတာ သင်ယူမှုရဲ့ အစဖြစ်တာကြောင့် စိတ်ဓာတ်မကျပါနဲ့။
            ၂။ ကိုယ့်ကိုယ်ကို သူတစ်ပါးနဲ့ မနှိုင်းယှဉ်ပါနဲ့၊ မနေ့က ကိုယ့်ထက် ဒီနေ့ ကိုယ်က ပိုတော်နေဖို့ပဲ အရေးကြီးပါတယ်။
            ၃။ ဇွဲ၊ လုံ့လနှင့် တစ်စိုက်မတ်မတ် ကြိုးစားမှုသည် မည်သည့်အခက်အခဲကိုမဆို ကျော်လွှားနိုင်စေပါသည်။
            
            သင့်ရဲ့ ကြိုးစားမှုတိုင်း အောင်မြင်ပါစေခင်ဗျာ!
        """.trimIndent()
    }
}
