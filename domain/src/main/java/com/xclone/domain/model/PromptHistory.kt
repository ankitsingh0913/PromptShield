package com.xclone.domain.model

data class PromptHistory(
    val id: Long = 0,

    val originalText: String,

    val cleanedText: String,

    val riskScore: Int,

    val timestamp: Long
)
