package com.pixelpulse.health.domain.repository

import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface WorkoutRepository {
    suspend fun createWorkout(workout: Workout): Result<Workout>
    suspend fun getWorkoutById(workoutId: String): Result<Workout?>
    suspend fun getUserWorkouts(userId: String): Flow<List<Workout>>
    suspend fun updateWorkout(workout: Workout): Result<Workout>
    suspend fun deleteWorkout(workoutId: String): Result<Unit>
    
    suspend fun getWorkoutsByType(userId: String, type: WorkoutType): Flow<List<Workout>>
    suspend fun getWorkoutsByDateRange(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Workout>>
    
    suspend fun createWorkoutPlan(workoutPlan: WorkoutPlan): Result<WorkoutPlan>
    suspend fun getActiveWorkoutPlan(userId: String): Result<WorkoutPlan?>
    suspend fun updateWorkoutPlan(workoutPlan: WorkoutPlan): Result<WorkoutPlan>
    suspend fun deleteWorkoutPlan(planId: String): Result<Unit>
    
    suspend fun recordWorkoutProgress(progress: WorkoutProgress): Result<Unit>
    suspend fun getWorkoutProgress(userId: String, exerciseId: String): Result<WorkoutProgress?>
    suspend fun getPersonalRecords(userId: String): Flow<List<PersonalRecord>>
    
    suspend fun generateWorkoutRecommendation(userId: String): Result<WorkoutRecommendation>
    suspend fun getWorkoutRecommendations(userId: String): Flow<List<WorkoutRecommendation>>
    suspend fun acceptWorkoutRecommendation(recommendationId: String): Result<Unit>
    suspend fun rejectWorkoutRecommendation(recommendationId: String): Result<Unit>
} 