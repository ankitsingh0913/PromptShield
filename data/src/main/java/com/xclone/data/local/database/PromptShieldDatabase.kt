package com.xclone.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xclone.data.local.dao.PromptHistoryDao
import com.xclone.data.local.entity.PromptHistoryEntity

@Database(
    entities = [PromptHistoryEntity::class],
    version = 1,
    exportSchema = false)
abstract class PromptShieldDatabase: RoomDatabase() {
    abstract fun promptHistoryDao(): PromptHistoryDao
}