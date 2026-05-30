package com.xclone.data.di

import com.xclone.data.ai.GeminiApi
import com.xclone.data.repository.AiRewriteRepositoryImpl
import com.xclone.domain.repository.AiRewriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor()
            .apply {
                level =
                    HttpLoggingInterceptor.Level.BODY
            }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)

            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )

            .readTimeout(
                60,
                TimeUnit.SECONDS
            )

            .writeTimeout(
                60,
                TimeUnit.SECONDS
            )

            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(
                "https://generativelanguage.googleapis.com/"
            )
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApi(
        retrofit: Retrofit
    ): GeminiApi {

        return retrofit.create(
            GeminiApi::class.java
        )
    }

    @Provides
    @Singleton
    fun provideAiRepository(
        api: GeminiApi
    ): AiRewriteRepository {

        return AiRewriteRepositoryImpl(
            api
        )
    }
}