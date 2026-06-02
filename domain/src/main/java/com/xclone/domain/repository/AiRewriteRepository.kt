package com.xclone.domain.repository

interface AiRewriteRepository {

    suspend fun rewritePrompt(
        prompt: String,
        findings: List<String>,
        profileName: String
    ): String
}