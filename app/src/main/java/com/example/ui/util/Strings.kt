package com.example.ui.util

import com.example.data.local.AppLanguage

object Strings {
    fun get(lang: AppLanguage, en: String, my: String): String {
        return if (lang == AppLanguage.MYANMAR) my else en
    }

    // Common UI texts
    fun appSettings(lang: AppLanguage) = get(lang, "App Settings", "အက်ပ် ဆက်တင်များ")
    fun newChat(lang: AppLanguage) = get(lang, "New Chat", "စကားဝိုင်း အသစ် စတင်ရန်")
    fun chatHistory(lang: AppLanguage) = get(lang, "Chat History", "CHAT စာရင်းများ")
    fun clearOldChats(lang: AppLanguage) = get(lang, "Clear Old", "ရှင်းလင်းမည်")
    fun noChatsYet(lang: AppLanguage) = get(lang, "No conversations yet. Start a new one!", "စကားဝိုင်း မရှိသေးပါ။ အသစ် စတင်နိုင်ပါသည်။")
    fun manageApiKeys(lang: AppLanguage) = get(lang, "Manage API Keys", "API ကီးများ စီမံမည်")
    fun configureApiKey(lang: AppLanguage) = get(lang, "Configure API Key", "API ကီး ထည့်သွင်းမည်")
    fun appearance(lang: AppLanguage) = get(lang, "Appearance & Display", "အသွင်အပြင်နှင့် ပြသမှု")
    fun theme(lang: AppLanguage) = get(lang, "Theme", "မျက်နှာပြင် အရောင်စနစ်")
    fun language(lang: AppLanguage) = get(lang, "Language", "ဘာသာစကား")
    fun aiModelSettings(lang: AppLanguage) = get(lang, "AI Models & API Keys", "AI အင်ဂျင်နှင့် API ကီးများ")
    fun devProfile(lang: AppLanguage) = get(lang, "Developer Profile", "Developer ပရိုဖိုင်")
    fun devSupport(lang: AppLanguage) = get(lang, "Developer Support & Donations", "Dev ထောက်ပံ့ရေး အင်္ဂါရပ်များ")
    fun feedbackAndContact(lang: AppLanguage) = get(lang, "Feedback & Contact", "အကြံပြုချက်နှင့် ဆက်သွယ်ရန်")
    fun copiedToClipboard(lang: AppLanguage) = get(lang, "Copied to clipboard!", "ကူးယူပြီးပါပြီ!")
    fun sendFeedback(lang: AppLanguage) = get(lang, "Send Feedback via Email", "အီးမေးလ်ဖြင့် အကြံပြုချက်ပို့မည်")
    fun copyEmail(lang: AppLanguage) = get(lang, "Copy Email", "အီးမေးလ် ကူးယူမည်")
    fun copyNumber(lang: AppLanguage) = get(lang, "Copy KBZPay Number", "နံပါတ် ကူးယူမည်")
    fun viewQr(lang: AppLanguage) = get(lang, "View KBZPay QR Code", "KBZPay QR ကုဒ် ကြည့်မည်")
    fun close(lang: AppLanguage) = get(lang, "Close", "ပိတ်မည်")
    fun back(lang: AppLanguage) = get(lang, "Back", "နောက်သို့")
    fun active(lang: AppLanguage) = get(lang, "Active", "အသုံးပြုနေသည်")
    fun notConfigured(lang: AppLanguage) = get(lang, "Not Configured", "မထည့်ရသေးပါ")
    fun configured(lang: AppLanguage) = get(lang, "Configured", "ထည့်သွင်းထားသည်")
    fun offlineReady(lang: AppLanguage) = get(lang, "Offline Ready (No Key)", "အော့ဖ်လိုင်းသုံးနိုင်သည် (Key မလိုပါ)")

    // Developer details
    const val DEV_NAME = "Mr.A (Arkarsoe)"
    const val DEV_ROLE = "Lead Android & AI Developer 🇲🇲"
    const val DEV_MESSAGE_MY = "App ဆက်လက်ဖွံ့ဖြိူးတိုးတတ်စေရန်ချစ်သောမိတ်ဆွေများရဲ့ အထောက်ပံ့ကောင်းများကို အတိုင်းမသိဝမ်းသာကြောင်းနဲ့ အစွမ်းကုန်ဆက်လက်ကြိုးစားသွားမည်ဖြစ်ကြောင်း"
    const val DEV_MESSAGE_EN = "I am deeply grateful for all your kind support to keep developing and improving this app, and I promise to continue striving and giving my very best!"
    const val KBZ_PAY_NAME = "Arkarsoe"
    const val KBZ_PAY_PHONE = "09691529743"
    const val DEV_EMAIL = "arkarsoe.vip@gmail.com"
}
