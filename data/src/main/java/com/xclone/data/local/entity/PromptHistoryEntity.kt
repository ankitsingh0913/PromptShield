package com.xclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PromptHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalText: String,
    val cleanedText: String,
    val riskScore: Int,
    val timestamp: Long
)
