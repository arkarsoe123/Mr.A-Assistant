package com.example.data.remote.brain

import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

object MathBrain {

    fun trySolve(input: String): String? {
        val clean = input.trim()
        val lower = clean.lowercase(Locale.ROOT)

        // 1. Percentage calculations
        val percentageResult = solvePercentage(clean, lower)
        if (percentageResult != null) return percentageResult

        // 2. Unit Conversions
        val unitResult = solveUnitConversion(clean, lower)
        if (unitResult != null) return unitResult

        // 3. Direct Math Expressions
        val directMath = solveDirectMath(clean)
        if (directMath != null) return directMath

        // 4. Geometry / Formulas
        val geometryResult = solveGeometry(clean, lower)
        if (geometryResult != null) return geometryResult

        return null
    }

    private fun solvePercentage(clean: String, lower: String): String? {
        // e.g. "20% of 50000" or "50000 ရဲ့ 20%" or "20 % of 1500"
        val pctOfRegex = Regex("""(\d+(\.\d+)?)\s*%\s*(?:of|ရဲ့|မှ)\s*(\d+(\.\d+)?)""")
        pctOfRegex.find(lower)?.let { match ->
            val pct = match.groupValues[1].toDoubleOrNull() ?: return null
            val base = match.groupValues[3].toDoubleOrNull() ?: return null
            val result = (pct / 100.0) * base
            val formatted = formatNumber(result)
            return """
                🧮 **ရာခိုင်နှုန်း တွက်ချက်မှု (Percentage Calculation):**
                
                • **အခြေခံပမာဏ:** ${formatNumber(base)}
                • **တွက်ချက်သည့် ရာခိုင်နှုန်း:** $pct%
                • **ဖော်မြူလာ:** `($pct / 100) × ${formatNumber(base)}`
                • **အဖြေရလဒ်:** **`$formatted`**
            """.trimIndent()
        }

        // e.g. "50000 + 10%" or "100000 - 15%"
        val pctAddSubRegex = Regex("""(\d+(\.\d+)?)\s*([\+\-])\s*(\d+(\.\d+)?)\s*%""")
        pctAddSubRegex.find(lower)?.let { match ->
            val base = match.groupValues[1].toDoubleOrNull() ?: return null
            val op = match.groupValues[3]
            val pct = match.groupValues[4].toDoubleOrNull() ?: return null
            val pctAmount = (pct / 100.0) * base
            val finalResult = if (op == "+") base + pctAmount else base - pctAmount
            val operationName = if (op == "+") "တိုးမြှင့်ခြင်း (Addition)" else "လျှော့စျေး / နုတ်ယူခြင်း (Discount / Deduction)"
            return """
                🧮 **ရာခိုင်နှုန်း အတိုး/အလျှော့ တွက်ချက်မှု:**
                
                • **အခြေခံတန်ဖိုး:** ${formatNumber(base)}
                • **ရာခိုင်နှုန်း ပမာဏ ($pct%):** ${formatNumber(pctAmount)}
                • **အမျိုးအစား:** $operationName
                • **နောက်ဆုံးကျသင့်ငွေ / ရလဒ်:** **`${formatNumber(finalResult)}`**
            """.trimIndent()
        }

        return null
    }

    private fun solveUnitConversion(clean: String, lower: String): String? {
        // Weight: kg to lbs / lbs to kg
        val kgToLbs = Regex("""(\d+(\.\d+)?)\s*(?:kg|ကီလို|kilogram)""")
        val lbsToKg = Regex("""(\d+(\.\d+)?)\s*(?:lbs|lb|ပေါင်|pounds?)""")
        val vissToKg = Regex("""(\d+(\.\d+)?)\s*(?:ပိဿာ|viss)""")

        if (lower.contains("lbs") || lower.contains("ပေါင်") || lower.contains("pound")) {
            kgToLbs.find(lower)?.let { match ->
                val kg = match.groupValues[1].toDoubleOrNull() ?: return null
                val lbs = kg * 2.20462
                return "⚖️ **အလေးချိန် ပြောင်းလဲခြင်း (Weight Conversion):**\n`$kg kg` = **`${"%.3f".format(lbs)} lbs`** (ပေါင်)"
            }
        }

        if (lower.contains("kg") || lower.contains("ကီလို") || lower.contains("kilogram")) {
            lbsToKg.find(lower)?.let { match ->
                val lbs = match.groupValues[1].toDoubleOrNull() ?: return null
                val kg = lbs / 2.20462
                return "⚖️ **အလေးချိန် ပြောင်းလဲခြင်း:**\n`$lbs lbs` = **`${"%.3f".format(kg)} kg`** (ကီလိုဂရမ်)"
            }
        }

        // Viss (ပိဿာ) to Kg
        vissToKg.find(lower)?.let { match ->
            val viss = match.groupValues[1].toDoubleOrNull() ?: return null
            val kg = viss * 1.63293
            val lbs = kg * 2.20462
            return """
                ⚖️ **မြန်မာ့ အလေးချိန် ပြောင်းလဲခြင်း (Myanmar Viss Conversion):**
                
                • **${formatNumber(viss)} ပိဿာ** = **`${"%.3f".format(kg)} kg`** (ကီလိုဂရမ်)
                • **ပေါင် (lbs) အားဖြင့်:** **`${"%.3f".format(lbs)} lbs`**
                *(စံနှုန်း: ၁ ပိဿာ = ၁.၆၃၂၉၃ ကီလိုဂရမ် / ၃.၆ ပေါင်)*
            """.trimIndent()
        }

        // Temperature: Celsius to Fahrenheit and vice versa
        val tempRegex = Regex("""(-?\d+(\.\d+)?)\s*°?\s*([cf])\b""")
        tempRegex.find(lower)?.let { match ->
            val value = match.groupValues[1].toDoubleOrNull() ?: return null
            val unit = match.groupValues[3].lowercase()
            return if (unit == "c") {
                val f = (value * 9.0 / 5.0) + 32.0
                "🌡️ **အပူချိန် ပြောင်းလဲခြင်း:**\n`$value°C` = **`${"%.2f".format(f)}°F`** (ဖာရင်ဟိုက်)"
            } else {
                val c = (value - 32.0) * 5.0 / 9.0
                "🌡️ **အပူချိန် ပြောင်းလဲခြင်း:**\n`$value°F` = **`${"%.2f".format(c)}°C`** (ဆဲလ်စီးယပ်)"
            }
        }

        // Distance: km to miles / miles to km
        val kmRegex = Regex("""(\d+(\.\d+)?)\s*(?:km|ကီလိုမီတာ)\b""")
        val milesRegex = Regex("""(\d+(\.\d+)?)\s*(?:miles?|မိုင်)\b""")

        if (lower.contains("mile") || lower.contains("မိုင်")) {
            kmRegex.find(lower)?.let { match ->
                val km = match.groupValues[1].toDoubleOrNull() ?: return null
                val miles = km * 0.621371
                return "🛣️ **အကွာအဝေး ပြောင်းလဲခြင်း:**\n`$km km` = **`${"%.3f".format(miles)} miles`** (မိုင်)"
            }
        }

        if (lower.contains("km") || lower.contains("ကီလိုမီတာ")) {
            milesRegex.find(lower)?.let { match ->
                val miles = match.groupValues[1].toDoubleOrNull() ?: return null
                val km = miles / 0.621371
                return "🛣️ **အကွာအဝေး ပြောင်းလဲခြင်း:**\n`$miles miles` = **`${"%.3f".format(km)} km`** (ကီလိုမီတာ)"
            }
        }

        return null
    }

    private fun solveDirectMath(input: String): String? {
        val expr = input.replace(" ", "").replace("x", "*").replace("X", "*").replace("÷", "/")

        // Support sqrt: sqrt(64) or √64
        val sqrtRegex = Regex("""(?:sqrt|\√)\((\d+(\.\d+)?)\)|(?:sqrt|\√)(\d+(\.\d+)?)""")
        sqrtRegex.find(expr)?.let { match ->
            val numStr = match.groupValues[1].ifBlank { match.groupValues[3] }
            val n = numStr.toDoubleOrNull() ?: return null
            if (n < 0) return "အနှုတ်ကိန်းများအတွက် Square root သည် သီးသန့်ကိန်း (Imaginary Number) ဖြစ်ပါသည်။"
            val res = sqrt(n)
            return "🧮 **Square Root တွက်ချက်မှု:**\n`√$n` = **`${formatNumber(res)}`**"
        }

        // Support power: a^b
        val powerRegex = Regex("""^(\d+(\.\d+)?)\^(\d+(\.\d+)?)$""")
        powerRegex.matchEntire(expr)?.let { match ->
            val base = match.groupValues[1].toDoubleOrNull() ?: return null
            val exp = match.groupValues[3].toDoubleOrNull() ?: return null
            val res = base.pow(exp)
            return "🧮 **ထပ်ညွှန်း တွက်ချက်မှု (Exponentiation):**\n`$base ^ $exp` = **`${formatNumber(res)}`**"
        }

        // Two-operand standard arithmetic: a + b, a - b, a * b, a / b
        val standardRegex = Regex("""^(\-?\d+(\.\d+)?)([\+\-\*\/])(\-?\d+(\.\d+)?)$""")
        val match = standardRegex.matchEntire(expr) ?: return null

        val n1 = match.groupValues[1].toDoubleOrNull() ?: return null
        val op = match.groupValues[3]
        val n2 = match.groupValues[4].toDoubleOrNull() ?: return null

        val res = when (op) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> {
                if (n2 == 0.0) return "⚠️ သုညဖြင့် စား၍မရပါ (Cannot divide by zero)"
                n1 / n2
            }
            else -> return null
        }

        return "🧮 **သင်္ချာတွက်ချက်မှု ရလဒ် (Math Calculation):**\n\n```text\n$input = ${formatNumber(res)}\n```"
    }

    private fun solveGeometry(clean: String, lower: String): String? {
        // Circle Area: "circle area radius 5" or "စက်ဝိုင်း အချင်းဝက် 7"
        if ((lower.contains("circle") || clean.contains("စက်ဝိုင်း")) && (lower.contains("area") || clean.contains("ဧရိယာ") || clean.contains("အကျယ်"))) {
            val radiusRegex = Regex("""(\d+(\.\d+)?)""")
            radiusRegex.find(clean)?.let { match ->
                val r = match.groupValues[1].toDoubleOrNull() ?: return null
                val area = Math.PI * r * r
                val perimeter = 2 * Math.PI * r
                return """
                    📐 **စက်ဝိုင်း ဧရိယာနှင့် ပတ်လည်အနား တွက်ချက်ခြင်း:**
                    
                    • **အချင်းဝက် (Radius r):** $r
                    • **ဧရိယာဖော်မြူလာ:** `Area = π × r²`
                    • **ဧရိယာ ရလဒ်:** **`${"%.4f".format(area)}`** စတုရန်းယူနစ်
                    • **ပတ်လည်အနား (Circumference = 2πr):** **`${"%.4f".format(perimeter)}`**
                """.trimIndent()
            }
        }

        return null
    }

    private fun formatNumber(num: Double): String {
        return if (num % 1.0 == 0.0 && num < 1e12 && num > -1e12) {
            num.roundToLong().toString()
        } else {
            "%.4f".format(num).trimEnd('0').trimEnd('.')
        }
    }
}
