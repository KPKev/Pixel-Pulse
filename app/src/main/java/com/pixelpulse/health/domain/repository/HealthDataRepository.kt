package com.pixelpulse.health.domain.repository

import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface HealthDataRepository {
    suspend fun insertHealthData(healthData: HealthData): Result<Unit>
    suspend fun getHealthDataByType(
        userId: String,
        dataType: HealthDataType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HealthData>>
    
    suspend fun getLatestHealthData(
        userId: String,
        dataType: HealthDataType
    ): Result<HealthData?>
    
    suspend fun insertHeartRateData(heartRateData: HeartRateData): Result<Unit>
    suspend fun getHeartRateData(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HeartRateData>>
    
    suspend fun insertSleepData(sleepData: SleepData): Result<Unit>
    suspend fun getSleepData(
        userId: String,
        date: LocalDate
    ): Result<SleepData?>
    
    suspend fun insertActivityData(activityData: ActivityData): Result<Unit>
    suspend fun getActivityData(
        userId: String,
        date: LocalDate
    ): Result<ActivityData?>
    
    suspend fun insertBloodPressureReading(reading: BloodPressureReading): Result<Unit>
    suspend fun getBloodPressureReadings(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<BloodPressureReading>>
    
    suspend fun insertStressData(stressData: StressData): Result<Unit>
    suspend fun getStressData(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<StressData>>
    
    suspend fun insertHydrationData(hydrationData: HydrationData): Result<Unit>
    suspend fun getHydrationData(
        userId: String,
        date: LocalDate
    ): Result<List<HydrationData>>
    
    suspend fun getAllHealthData(userId: String): Flow<List<HealthData>>
    suspend fun deleteHealthData(healthDataId: String): Result<Unit>
    suspend fun updateHealthData(healthData: HealthData): Result<Unit>
    
    suspend fun syncWithGoogleFit(userId: String): Result<Unit>
    suspend fun syncWithHealthConnect(userId: String): Result<Unit>
} 