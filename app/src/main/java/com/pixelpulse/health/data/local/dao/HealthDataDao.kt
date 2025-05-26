package com.pixelpulse.health.data.local.dao

import androidx.room.*
import com.pixelpulse.health.data.local.entity.HealthDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface HealthDataDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthData(healthData: HealthDataEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthDataList(healthDataList: List<HealthDataEntity>)
    
    @Query("SELECT * FROM health_data WHERE userId = :userId AND dataType = :dataType AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getHealthDataByType(
        userId: String,
        dataType: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HealthDataEntity>>
    
    @Query("SELECT * FROM health_data WHERE userId = :userId AND dataType = :dataType ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestHealthData(userId: String, dataType: String): HealthDataEntity?
    
    @Query("SELECT * FROM health_data WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllHealthData(userId: String): Flow<List<HealthDataEntity>>
    
    @Query("DELETE FROM health_data WHERE userId = :userId AND dataType = :dataType")
    suspend fun deleteHealthDataByType(userId: String, dataType: String)
    
    @Query("DELETE FROM health_data WHERE userId = :userId")
    suspend fun deleteAllHealthData(userId: String)
} 