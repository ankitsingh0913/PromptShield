package com.xclone.detector_engine.regex

object RegexPatterns {
    val EMAIL = Regex(
            "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        )

    val PHONE =  Regex(
            "(\\+91)?[6-9]\\d{9}"
        )

    val OPENAI_KEY = Regex(
            "sk-[A-Za-z0-9]{20,}"
        )

    val PASSWORD = Regex(
            "(?i)(password|pwd|pass)\\s*[:=]\\s*\\S+"
        )

    val CODE_BLOCK = Regex(
            "```[\\s\\S]*?```"
        )
}