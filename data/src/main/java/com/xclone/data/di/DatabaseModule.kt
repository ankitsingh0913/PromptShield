package com.xclone.data.di

import android.content.Context
import androidx.room.Room
import com.xclone.data.local.dao.PromptHistoryDao
import com.xclone.data.local.database.PromptShieldDatabase
import com.xclone.data.repository.PromptHistoryRepositoryImpl
import com.xclone.domain.repository.PromptHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PromptShieldDatabase {
        return Room.databaseBuilder(
            context,
            PromptShieldDatabase::class.java,
            "prompt_shield_database"
        ).build()
    }

    @Provides
    fun providePromptDao(
        db: PromptShieldDatabase
    ): PromptHistoryDao{
        return db.promptHistoryDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        promptHistoryDao: PromptHistoryDao
    ): PromptHistoryRepository {
        return PromptHistoryRepositoryImpl(promptHistoryDao)
    }
}