package com.schcool.trainer.di

import android.content.Context
import androidx.room.Room
import com.schcool.trainer.data.AppDatabase
import com.schcool.trainer.data.AttemptDao
import com.schcool.trainer.data.LlmService
import com.schcool.trainer.data.PhoneDao
import com.schcool.trainer.data.RubricScorer
import com.schcool.trainer.data.ScenarioDao
import com.schcool.trainer.data.TemplateLlmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "trainer.db").build()
    }

    @Provides
    fun providePhoneDao(database: AppDatabase): PhoneDao = database.phoneDao()

    @Provides
    fun provideScenarioDao(database: AppDatabase): ScenarioDao = database.scenarioDao()

    @Provides
    fun provideAttemptDao(database: AppDatabase): AttemptDao = database.attemptDao()

    @Provides
    @Singleton
    fun provideLlmService(): LlmService = TemplateLlmService()

    @Provides
    @Singleton
    fun provideRubricScorer(): RubricScorer = RubricScorer()
}
