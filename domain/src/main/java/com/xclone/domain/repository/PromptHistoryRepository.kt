package com.xclone.domain.repository

import com.xclone.domain.model.PromptHistory
import kotlinx.coroutines.flow.Flow

interface PromptHistoryRepository {

    suspend fun insertPrompt(
        prompt: PromptHistory
    )

    fun getAllPrompts():
            Flow<List<PromptHistory>>

    suspend fun clearHistory()
}