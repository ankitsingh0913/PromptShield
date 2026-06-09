package com.xclone.data.repository

import com.xclone.data.local.dao.PromptHistoryDao
import com.xclone.data.local.entity.PromptHistoryEntity
import com.xclone.domain.model.PromptHistory
import com.xclone.domain.repository.PromptHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PromptHistoryRepositoryImpl @Inject constructor(
    private val dao: PromptHistoryDao
): PromptHistoryRepository {

    override suspend fun insertPrompt(prompt: PromptHistory) {
        dao.insertPrompt(
            PromptHistoryEntity(
                id = prompt.id,
                originalText = prompt.originalText,
                cleanedText = prompt.cleanedText,
                riskScore = prompt.riskScore,
                timestamp = prompt.timestamp
            )
        )
    }

    override fun getAllPrompts(): Flow<List<PromptHistory>> {
        return dao.getAllPrompts().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getPromptById(id: Long): Flow<PromptHistory?> {
        return dao.getPromptById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun clearHistory() {
        dao.clearHistory()
    }

    private fun PromptHistoryEntity.toDomain(): PromptHistory {
        return PromptHistory(
            id = id,
            originalText = originalText,
            cleanedText = cleanedText,
            riskScore = riskScore,
            timestamp = timestamp
        )
    }

}