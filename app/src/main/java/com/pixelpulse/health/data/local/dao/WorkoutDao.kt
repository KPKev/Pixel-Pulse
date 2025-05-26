package com.pixelpulse.health.data.local.dao

import androidx.room.*

@Dao
interface WorkoutDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)
    
    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY id DESC")
    fun getUserWorkouts(userId: String): Flow<List<WorkoutEntity>>
    
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String): WorkoutEntity?
    
    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)
    
    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)
    
    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: String)
    
    @Query("SELECT * FROM workouts WHERE userId = :userId AND type = :type")
    fun getWorkoutsByType(userId: String, type: String): Flow<List<WorkoutEntity>>
} 