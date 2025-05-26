package com.pixelpulse.health.data.repository

import com.pixelpulse.health.data.local.dao.HealthDataDao
import com.pixelpulse.health.data.local.entity.HealthDataEntity
import com.pixelpulse.health.domain.model.*
import com.pixelpulse.health.domain.repository.HealthDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataRepositoryImpl @Inject constructor(
    private val healthDataDao: HealthDataDao
) : HealthDataRepository {

    override suspend fun insertHealthData(healthData: HealthData): Result<Unit> {
        return try {
            val entity = healthData.toEntity()
            healthDataDao.insertHealthData(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHealthDataByType(
        userId: String,
        dataType: HealthDataType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HealthData>> {
        return healthDataDao.getHealthDataByType(
            userId = userId,
            dataType = dataType.name,
            startDate = startDate,
            endDate = endDate
        ).map { entities -> entities.map { it.toDomainModel() } }
            .catch { e -> 
                android.util.Log.e("HealthDataRepo", "Error getting health data by type", e)
                emit(emptyList())
            }
    }

    override suspend fun getLatestHealthData(
        userId: String,
        dataType: HealthDataType
    ): Result<HealthData?> {
        return try {
            val entity = healthDataDao.getLatestHealthData(userId, dataType.name)
            val healthData = entity?.toDomainModel()
            Result.success(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertHeartRateData(heartRateData: HeartRateData): Result<Unit> {
        return try {
            val healthData = HealthData(
                id = heartRateData.id,
                userId = heartRateData.userId,
                timestamp = heartRateData.timestamp,
                dataType = HealthDataType.HEART_RATE,
                value = heartRateData.heartRate.toDouble(),
                unit = "BPM",
                source = heartRateData.source,
                confidence = 1.0f,
                notes = "Context: ${heartRateData.context.name}"
            )
            insertHealthData(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHeartRateData(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HeartRateData>> {
        return getHealthDataByType(userId, HealthDataType.HEART_RATE, startDate, endDate)
            .map { healthDataList ->
                healthDataList.mapNotNull { healthData ->
                    try {
                        HeartRateData(
                            id = healthData.id,
                            userId = healthData.userId,
                            timestamp = healthData.timestamp,
                            heartRate = healthData.value.toInt(),
                            heartRateZone = HeartRateZone.RESTING, // Default, should be calculated
                            source = healthData.source,
                            context = HeartRateContext.RESTING // Default
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override suspend fun insertSleepData(sleepData: SleepData): Result<Unit> {
        return try {
            val healthData = HealthData(
                id = sleepData.id,
                userId = sleepData.userId,
                timestamp = sleepData.bedtime,
                dataType = HealthDataType.SLEEP_DURATION,
                value = sleepData.totalSleepDuration.inWholeMinutes.toDouble(),
                unit = "minutes",
                source = sleepData.source,
                confidence = 1.0f,
                notes = "Sleep score: ${sleepData.sleepScore}"
            )
            insertHealthData(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSleepData(
        userId: String,
        date: LocalDate
    ): Result<SleepData?> {
        return try {
            // This is a simplified implementation
            // In a real app, you'd query for sleep data specifically for the date
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertActivityData(activityData: ActivityData): Result<Unit> {
        return try {
            // Insert multiple health data entries for different activity metrics
            val stepsData = HealthData(
                id = "${activityData.id}_steps",
                userId = activityData.userId,
                timestamp = LocalDateTime(activityData.date, kotlinx.datetime.LocalTime(12, 0)),
                dataType = HealthDataType.STEPS,
                value = activityData.steps.toDouble(),
                unit = "steps",
                source = activityData.source,
                confidence = 1.0f
            )
            insertHealthData(stepsData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getActivityData(
        userId: String,
        date: LocalDate
    ): Result<ActivityData?> {
        return try {
            // This is a simplified implementation
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertBloodPressureReading(reading: BloodPressureReading): Result<Unit> {
        return try {
            val systolicData = HealthData(
                id = "${reading.id}_systolic",
                userId = reading.userId,
                timestamp = reading.timestamp,
                dataType = HealthDataType.BLOOD_PRESSURE_SYSTOLIC,
                value = reading.systolic.toDouble(),
                unit = "mmHg",
                source = reading.source,
                confidence = 1.0f,
                notes = reading.notes
            )
            insertHealthData(systolicData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBloodPressureReadings(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<BloodPressureReading>> {
        return getHealthDataByType(userId, HealthDataType.BLOOD_PRESSURE_SYSTOLIC, startDate, endDate)
            .map { healthDataList ->
                healthDataList.mapNotNull { healthData ->
                    try {
                        BloodPressureReading(
                            id = healthData.id.replace("_systolic", ""),
                            userId = healthData.userId,
                            timestamp = healthData.timestamp,
                            systolic = healthData.value.toInt(),
                            diastolic = 80, // Default, should be fetched separately
                            category = BloodPressureCategory.NORMAL,
                            source = healthData.source,
                            notes = healthData.notes
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override suspend fun insertStressData(stressData: StressData): Result<Unit> {
        return try {
            val healthData = HealthData(
                id = stressData.id,
                userId = stressData.userId,
                timestamp = stressData.timestamp,
                dataType = HealthDataType.STRESS_LEVEL,
                value = stressData.stressLevel.toDouble(),
                unit = "level",
                source = stressData.source,
                confidence = 1.0f,
                notes = "Category: ${stressData.stressCategory.name}"
            )
            insertHealthData(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStressData(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<StressData>> {
        return getHealthDataByType(userId, HealthDataType.STRESS_LEVEL, startDate, endDate)
            .map { healthDataList ->
                healthDataList.mapNotNull { healthData ->
                    try {
                        StressData(
                            id = healthData.id,
                            userId = healthData.userId,
                            timestamp = healthData.timestamp,
                            stressLevel = healthData.value.toInt(),
                            stressCategory = StressCategory.MODERATE, // Default
                            source = healthData.source
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override suspend fun insertHydrationData(hydrationData: HydrationData): Result<Unit> {
        return try {
            val healthData = HealthData(
                id = hydrationData.id,
                userId = hydrationData.userId,
                timestamp = hydrationData.timestamp,
                dataType = HealthDataType.HYDRATION,
                value = hydrationData.amount,
                unit = "ml",
                source = hydrationData.source,
                confidence = 1.0f,
                notes = "Beverage: ${hydrationData.beverageType.name}"
            )
            insertHealthData(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHydrationData(
        userId: String,
        date: LocalDate
    ): Result<List<HydrationData>> {
        return try {
            // This is a simplified implementation
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllHealthData(userId: String): Flow<List<HealthData>> {
        return healthDataDao.getAllHealthData(userId)
            .map { entities -> entities.map { it.toDomainModel() } }
            .catch { e -> 
                android.util.Log.e("HealthDataRepo", "Error getting all health data", e)
                emit(emptyList())
            }
    }

    override suspend fun deleteHealthData(healthDataId: String): Result<Unit> {
        return try {
            // Implementation would depend on DAO method
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateHealthData(healthData: HealthData): Result<Unit> {
        return try {
            val entity = healthData.toEntity()
            // Implementation would depend on DAO method
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncWithGoogleFit(userId: String): Result<Unit> {
        return try {
            // Implementation for Google Fit sync
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncWithHealthConnect(userId: String): Result<Unit> {
        return try {
            // Implementation for Health Connect sync
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Extension functions for mapping between domain and entity models
    private fun HealthData.toEntity(): HealthDataEntity {
        return HealthDataEntity(
            id = this.id,
            userId = this.userId,
            timestamp = this.timestamp,
            dataType = this.dataType.name,
            value = this.value,
            unit = this.unit,
            source = this.source.name,
            confidence = this.confidence,
            notes = this.notes
        )
    }

    private fun HealthDataEntity.toDomainModel(): HealthData {
        return HealthData(
            id = this.id,
            userId = this.userId,
            timestamp = this.timestamp,
            dataType = HealthDataType.valueOf(this.dataType),
            value = this.value,
            unit = this.unit,
            source = DataSource.valueOf(this.source),
            confidence = this.confidence,
            notes = this.notes
        )
    }
} 