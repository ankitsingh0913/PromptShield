package com.xclone.data.ai

object PromptTemplates {

    fun rewritePrompt(
        prompt: String,
        findings: List<String>
    ): String {

        return """
        Rewrite the following prompt
        to remove sensitive information
        while preserving meaning.

        Return only the rewritten text.

        Prompt:

        $prompt
        """.trimIndent()
    }
}

//"""
//You are a privacy assistant.
//
//Remove or generalize any sensitive information such as:
//- emails
//- phone numbers
//- API keys
//- passwords
//- personal identifiers
//
//Preserve the original meaning.
//
//Return only the rewritten prompt.
//
//Original Prompt:
//$prompt
//"""