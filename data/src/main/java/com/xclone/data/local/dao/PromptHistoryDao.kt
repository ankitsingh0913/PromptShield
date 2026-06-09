package com.xclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xclone.data.local.entity.PromptHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrompt(prompt: PromptHistoryEntity)

    @Query("SELECT * FROM PromptHistoryEntity ORDER BY timestamp DESC")
    fun getAllPrompts(): Flow<List<PromptHistoryEntity>>

    @Query("SELECT * FROM PromptHistoryEntity WHERE id = :id LIMIT 1")
    fun getPromptById(id: Long): Flow<PromptHistoryEntity?>

    @Query("DELETE FROM PromptHistoryEntity")
    suspend fun clearHistory()
}