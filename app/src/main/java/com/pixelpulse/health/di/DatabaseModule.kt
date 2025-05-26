package com.pixelpulse.health.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pixelpulse.health.data.local.HealthDatabase
import com.pixelpulse.health.data.local.dao.*
import com.pixelpulse.health.utils.SecurityUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHealthDatabase(@ApplicationContext context: Context): HealthDatabase {
        // Use secure key generation for database encryption
        val passphrase = SecurityUtils.getDatabaseKey(context)
        return HealthDatabase.getDatabase(context, passphrase)
    }

    @Provides
    fun provideUserDao(database: HealthDatabase): UserDao = database.userDao()

    @Provides
    fun provideHealthDataDao(database: HealthDatabase): HealthDataDao = database.healthDataDao()

    @Provides
    fun provideWorkoutDao(database: HealthDatabase): WorkoutDao = database.workoutDao()

    @Provides
    fun provideNutritionDao(database: HealthDatabase): NutritionDao = database.nutritionDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
} 