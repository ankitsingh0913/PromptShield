package com.xclone.domain.repository

interface AiRewriteRepository {

    suspend fun rewritePrompt(
        prompt: String,
        findings: List<String>,
        profileName: String
    ): String

    suspend fun explainRisk(
        prompt: String,
        findings: List<String>,
        riskScore: Int,
        profileName: String
    ): String
}