package com.xclone.data.ai

object PromptTemplates {

    fun rewritePrompt(
        prompt: String,
        findings: List<String>,
        profileName: String
    ): String {
        val findingsSummary = if (findings.isEmpty()) {
            "No explicit sensitive types were detected."
        } else {
            findings.joinToString(", ") { it.lowercase().replace('_', ' ') }
        }

        val profileInstructions = when (profileName.uppercase()) {
            "DEVELOPER" -> """
                You are a security-focused coding assistant.
                Preserve code structure, syntax, variable names, function names, and technical meaning.
                Remove or replace secrets, credentials, tokens, emails, and phone numbers with safe placeholders.
                Keep code blocks intact unless they contain sensitive values.
            """.trimIndent()

            "STUDENT" -> """
                You are an educational assistant.
                Preserve the learning goal, clarity, and simple explanation style.
                Remove personal or sensitive details while keeping the prompt understandable and useful.
            """.trimIndent()

            "RECRUITER" -> """
                You are a professional recruiting assistant.
                Preserve professional tone, job context, and recruiting intent.
                Remove personal contact details and confidential identifiers.
            """.trimIndent()

            "LEGAL" -> """
                You are a legal compliance assistant.
                Preserve legal precision, meaning, and document structure.
                Redact confidential identifiers, names, and contact details carefully.
            """.trimIndent()

            else -> """
                You are a secure prompt rewriting assistant.
                Preserve meaning and intent while removing sensitive information.
            """.trimIndent()
        }

        return """
            Rewrite the following prompt to remove sensitive information while preserving its meaning.

            DETECTED SENSITIVE TYPES:
            $findingsSummary

            INSTRUCTIONS:
            1. Follow the profile-specific rules below.
            2. Remove or replace all detected sensitive information.
            3. Preserve the original intent, tone, and structure.
            4. Do not add new information.
            5. Do not explain your changes.
            6. Return only the rewritten prompt.

            PROFILE:
            $profileName

            PROFILE RULES:
            $profileInstructions

            ORIGINAL PROMPT:
            $prompt

            REWRITTEN PROMPT:
        """.trimIndent()
    }

    fun explainRisk(
        prompt: String,
        findings: List<String>,
        riskScore: Int,
        profileName: String
    ): String {
        val findingsSummary = findings.joinToString(", ") { it.lowercase().replace('_', ' ') }
        return """
        You are a security analyst. Explain why the following prompt is risky based on the detected sensitive data.
        Keep it concise (2-3 sentences max). Tailor the tone to the user's role.
        Profile: $profileName
        Risk Score: $riskScore/100
        Detected Types: $findingsSummary
        Original Prompt: $prompt
        Provide only the explanation. Do not rewrite the prompt. Do not add greetings.
    """.trimIndent()
    }
}