package com.example.data.remote.brain

import java.util.Locale

object CodingBrain {

    fun trySolve(input: String): String? {
        val clean = input.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // 1. Android & Jetpack Compose
        if (lower.contains("compose") || lower.contains("viewmodel") || lower.contains("stateflow") ||
            clean.contains("အန်ဒရွိုက်") || lower.contains("android") || lower.contains("lazycolumn") ||
            lower.contains("scaffold") || lower.contains("room database")
        ) {
            return solveAndroidCompose(clean, lower)
        }

        // 2. Kotlin Specifics
        if (lower.contains("kotlin") || lower.contains("coroutine") || lower.contains("sealed class") ||
            lower.contains("data class") || lower.contains("extension function")
        ) {
            return solveKotlin(clean, lower)
        }

        // 3. Python
        if (lower.contains("python") || lower.contains("pip") || lower.contains("pandas") ||
            lower.contains("fastapi") || lower.contains("django")
        ) {
            return solvePython(clean, lower)
        }

        // 4. SQL & Database
        if (lower.contains("sql") || lower.contains("database") || lower.contains("sqlite") ||
            lower.contains("query") || lower.contains("join")
        ) {
            return solveSql(clean, lower)
        }

        // 5. Git & GitHub
        if (lower.contains("git") || lower.contains("github") || lower.contains("commit") ||
            lower.contains("merge") || lower.contains("branch")
        ) {
            return solveGit(clean, lower)
        }

        // 6. Web & JavaScript / TypeScript / React
        if (lower.contains("javascript") || lower.contains("typescript") || lower.contains("react") ||
            lower.contains("html") || lower.contains("css") || lower.contains("node")
        ) {
            return solveWebDev(clean, lower)
        }

        // 7. General Coding / Programming Question
        if (lower.contains("code") || lower.contains("programming") || clean.contains("ကုဒ်") ||
            clean.contains("ပရိုဂရမ်းမင်း") || lower.contains("developer") || lower.contains("algorithm")
        ) {
            return solveGeneralCoding(clean, lower)
        }

        return null
    }

    private fun solveAndroidCompose(clean: String, lower: String): String {
        if (lower.contains("lazycolumn") || clean.contains("list") || clean.contains("စာရင်း")) {
            return """
                📱 **Jetpack Compose: LazyColumn (စွမ်းဆောင်ရည်မြင့် List ပြသခြင်း)**
                
                Android တွင် RecyclerView အစား LazyColumn ကို အသုံးပြု၍ item များကို ချောမွေ့စွာ ပြသနိုင်ပါသည်-
                
                ```kotlin
                @Composable
                fun MessageList(items: List<String>) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items, key = { it }) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                ```
                
                💡 **အဓိက အချက်များ:**
                - `key` ထည့်သွင်းပေးခြင်းဖြင့် Recomposition ကို သက်သာစေပါသည်။
                - `contentPadding` ဖြင့် list အနားသတ်များတွင် padding သတ်မှတ်နိုင်ပါသည်။
            """.trimIndent()
        }

        if (lower.contains("viewmodel") || lower.contains("stateflow") || lower.contains("state")) {
            return """
                📱 **Android MVVM: ViewModel & StateFlow Pattern**
                
                ခေတ်မီ Android App များတွင် UI State စီမံခန့်ခွဲရန် ViewModel နှင့် StateFlow ကို တွဲဖက် အသုံးပြုပါသည်-
                
                ```kotlin
                class MyViewModel : ViewModel() {
                    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
                    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
                
                    fun loadData() {
                        viewModelScope.launch {
                            try {
                                val result = repository.fetchData()
                                _uiState.value = UiState.Success(result)
                            } catch (e: Exception) {
                                _uiState.value = UiState.Error(e.message ?: "Unknown Error")
                            }
                        }
                    }
                }
                
                // Compose UI တွင် နားထောင်ပုံ:
                @Composable
                fun MyScreen(viewModel: MyViewModel = viewModel()) {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    
                    when (val current = state) {
                        is UiState.Loading -> CircularProgressIndicator()
                        is UiState.Success -> Content(current.data)
                        is UiState.Error -> Text("Error: ${'$'}{current.message}")
                    }
                }
                ```
            """.trimIndent()
        }

        return """
            📱 **Modern Android Development (Jetpack Compose & Kotlin):**
            
            Android Application တည်ဆောက်ရာတွင် အကြံပြုထားသော အဓိက အစိတ်အပိုင်းများ:
            
            1. **Jetpack Compose:** Declarative UI toolkit ဖြစ်ပြီး XML မလိုဘဲ Kotlin ဖြင့် UI အလွယ်တကူ ရေးသားနိုင်ပါသည်။
            2. **Material 3 (M3):** Dynamic Color, Typography နှင့် Elevation များဖြင့် ခေတ်မီ Design System တည်ဆောက်ခြင်း။
            3. **Room Database:** SQLite ကို အခြေခံထားသော Local Persistence စနစ်ဖြစ်ပြီး Flow နှင့် တွဲဖက် အသုံးပြုနိုင်ပါသည်။
            4. **Kotlin Coroutines & Flow:** Asynchronous background tasks များနှင့် reactive data stream များကို လွယ်ကူစွာ ကိုင်တွယ်နိုင်ပါသည်။
            
            သိလိုသော Composable သို့မဟုတ် Android Architecture အသေးစိတ်ကို ဆက်လက် မေးမြန်းနိုင်ပါသည်ခင်ဗျာ။
        """.trimIndent()
    }

    private fun solveKotlin(clean: String, lower: String): String {
        return """
            🎯 **Kotlin Programming Essentials & Best Practices:**
            
            ```kotlin
            // 1. Data Class & Sealed Class Hierarchy
            sealed interface Result<out T> {
                data class Success<T>(val data: T) : Result<T>
                data class Error(val exception: Throwable) : Result<Nothing>
                object Loading : Result<Nothing>
            }
            
            // 2. Coroutine Scope & Suspend Functions
            suspend fun fetchUserProfile(userId: String): Result<UserProfile> = withContext(Dispatchers.IO) {
                try {
                    val profile = apiService.getUser(userId)
                    Result.Success(profile)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            
            // 3. Extension Functions
            fun String.toBurmeseGreeting(): String = "မင်္ဂလာပါ ${'$'}this"
            ```
            
            💡 **Kotlin ၏ အားသာချက်များ:**
            - **Null Safety:** `?`, `?.`, `?:` များဖြင့် NullPointerException ကို Compile time ၌ ကြိုတင်ကာကွယ်နိုင်ခြင်း။
            - **Concise Syntax:** Boilerplate code နည်းပါးပြီး readability မြင့်မားခြင်း။
            - **Coroutines:** Thread များထက် ပေါ့ပါးပြီး memory သက်သာသော asynchronous စနစ်။
        """.trimIndent()
    }

    private fun solvePython(clean: String, lower: String): String {
        return """
            🐍 **Python Programming & Best Practices:**
            
            ```python
            # 1. List Comprehension & Modern Python
            numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            even_squares = [x**2 for x in numbers if x % 2 == 0]
            print(f"Even squares: {even_squares}")
            
            # 2. Safe Dictionary Handling & Type Hinting
            from typing import Optional, Dict
            
            def get_user_email(user_data: Dict[str, str]) -> Optional[str]:
                return user_data.get("email", None)
            
            # 3. Context Manager for Files
            with open("data.txt", "w", encoding="utf-8") as f:
                f.write("မြန်မာစာနှင့် Python စမ်းသပ်ချက်\n")
            ```
            
            💡 **အသုံးများသော Python Libraries:**
            - **FastAPI / Flask:** Modern API services တည်ဆောက်ရန်
            - **Pandas / NumPy:** Data analysis နှင့် သိပ္ပံနည်းကျ တွက်ချက်မှုများအတွက်
            - **Requests / httpx:** Web requests နှင့် API စားသုံးရန်
        """.trimIndent()
    }

    private fun solveSql(clean: String, lower: String): String {
        return """
            🗄️ **SQL Database Query Guide:**
            
            ```sql
            -- 1. Create Table with Constraints
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            
            -- 2. Inner Join Query (အသုံးအများဆုံး ချိတ်ဆက်မှု)
            SELECT u.name, o.order_id, o.total_amount
            FROM users u
            INNER JOIN orders o ON u.id = o.user_id
            WHERE o.status = 'COMPLETED'
            ORDER BY o.total_amount DESC
            LIMIT 10;
            
            -- 3. Group By & Aggregations
            SELECT category, COUNT(*) AS total_items, AVG(price) AS avg_price
            FROM products
            GROUP BY category
            HAVING COUNT(*) > 5;
            ```
            
            💡 **Index အကြံပြုချက်:** မကြာခဏ ရှာဖွေရလေ့ရှိသော columns များ (ဥပမာ `user_id`, `email`) ပေါ်တွင် Index သတ်မှတ်ပေးခြင်းဖြင့် Query အရှိန်ကို အဆမတန် မြန်ဆန်စေပါသည်။
        """.trimIndent()
    }

    private fun solveGit(clean: String, lower: String): String {
        return """
            🐙 **Essential Git Commands Cheat Sheet:**
            
            ```bash
            # 1. Repository စတင်ခြင်းနှင့် အခြေအနေ စစ်ဆေးခြင်း
            git status
            git log --oneline -n 5
            
            # 2. ပြင်ဆင်ချက်များ သိမ်းဆည်းခြင်း (Commit)
            git add .
            git commit -m "feat: Add new chat functionality"
            
            # 3. Branch အသစ် ဖန်တီးခြင်းနှင့် ပြောင်းခြင်း
            git checkout -b feature/user-profile
            git push origin feature/user-profile
            
            # 4. နောက်ဆုံး Commit ကို စာသားပြင်ခြင်း သို့မဟုတ် ပြန်ပြင်ခြင်း
            git commit --amend -m "fix: Update message formatting"
            
            # 5. လက်ရှိ ပြင်ဆင်ထားသည်များကို ခေတ္တ သိမ်းထားခြင်း (Stash)
            git stash
            git pull origin main
            git stash pop
            ```
        """.trimIndent()
    }

    private fun solveWebDev(clean: String, lower: String): String {
        return """
            🌐 **Web Development & React / TypeScript Guide:**
            
            ```typescript
            // React Functional Component with Hooks
            import React, { useState, useEffect } from 'react';
            
            interface UserCardProps {
              userId: string;
              initialName: string;
            }
            
            export const UserCard: React.FC<UserCardProps> = ({ userId, initialName }) => {
              const [name, setName] = useState<string>(initialName);
              const [loading, setLoading] = useState<boolean>(false);
            
              useEffect(() => {
                console.log(`User mounted: ${'$'}{userId}`);
              }, [userId]);
            
              return (
                <div className="p-4 rounded-xl shadow bg-white dark:bg-zinc-900 border border-zinc-200">
                  <h3 className="font-bold text-lg text-primary">{name}</h3>
                  <p className="text-sm text-zinc-500">ID: {userId}</p>
                </div>
              );
            };
            ```
        """.trimIndent()
    }

    private fun solveGeneralCoding(clean: String, lower: String): String {
        return """
            💻 **Software Engineering & Coding Best Practices:**
            
            1. **Clean Code အခြေခံများ:**
               - **DRY (Don't Repeat Yourself):** ထပ်ခါတလဲလဲ ကုဒ်များကို function သို့မဟုတ် component ခွဲထုတ်ပါ။
               - **Meaningful Names:** အဓိပ္ပာယ် ရှင်းလင်းသော variable နှင့် function အမည်များကို သုံးပါ။
               - **Single Responsibility (SRP):** Function တစ်ခုသည် လုပ်ငန်းတစ်ခုတည်းကိုသာ တိကျစွာ လုပ်ဆောင်သင့်ပါသည်။
            
            2. **Error Handling & Resilience:**
               - Unhandled exceptions များကို ကာကွယ်ရန် `try-catch` နှင့် graceful degradation စနစ်သုံးပါ။
               - Network သို့မဟုတ် IO လုပ်ငန်းများတွင် timeout သတ်မှတ်ပါ။
            
            အသေးစိတ် ရေးသားလိုသော ပရိုဂရမ်းမင်းဘာသာစကား သို့မဟုတ် ပြဿနာကို ဆက်လက် ပြောပြပေးနိုင်ပါသည်ခင်ဗျာ။
        """.trimIndent()
    }
}
