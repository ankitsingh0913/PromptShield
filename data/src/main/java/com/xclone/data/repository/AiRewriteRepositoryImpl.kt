package com.xclone.data.repository

import android.util.Log
import com.xclone.data.BuildConfig
import com.xclone.data.ai.Content
import com.xclone.data.ai.GeminiApi
import com.xclone.data.ai.GeminiRequest
import com.xclone.data.ai.Part
import com.xclone.data.ai.PromptTemplates
import com.xclone.domain.repository.AiRewriteRepository
import javax.inject.Inject

class AiRewriteRepositoryImpl @Inject constructor(
    private val api: GeminiApi,
) : AiRewriteRepository {

    override suspend fun rewritePrompt(
        prompt: String,
        findings: List<String>,
        profileName: String
    ): String {

        val rewrittenPrompt = PromptTemplates.rewritePrompt(
            prompt = prompt,
            findings = findings,
            profileName = profileName
        )

        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = rewrittenPrompt)
                    )
                )
            )
        )

        Log.d("GEMINI_KEY", BuildConfig.GEMINI_API_KEY)

        val response = api.generateContent(
            BuildConfig.GEMINI_API_KEY,
            request
        )

        return response
            .candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: "Unable to generate suggestion"
    }
}