package com.pixelpulse.health.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class Workout(
    val id: String,
    val userId: String,
    val name: String,
    val type: WorkoutType,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val duration: Duration,
    val exercises: List<Exercise>,
    val caloriesBurned: Int,
    val averageHeartRate: Int? = null,
    val maxHeartRate: Int? = null,
    val notes: String? = null,
    val difficulty: WorkoutDifficulty,
    val status: WorkoutStatus,
    val location: String? = null,
    val weather: String? = null
)

enum class WorkoutType {
    STRENGTH_TRAINING,
    CARDIO,
    YOGA,
    PILATES,
    RUNNING,
    CYCLING,
    SWIMMING,
    WALKING,
    HIKING,
    DANCING,
    MARTIAL_ARTS,
    SPORTS,
    STRETCHING,
    CROSSFIT,
    HIIT,
    CIRCUIT_TRAINING,
    BODYWEIGHT,
    FLEXIBILITY,
    BALANCE,
    OTHER
}

enum class WorkoutDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

enum class WorkoutStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    SKIPPED,
    CANCELLED
}

data class Exercise(
    val id: String,
    val name: String,
    val type: ExerciseType,
    val category: ExerciseCategory,
    val muscleGroups: List<MuscleGroup>,
    val sets: List<ExerciseSet>,
    val instructions: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val videoUrl: String? = null,
    val imageUrl: String? = null,
    val equipment: List<Equipment> = emptyList(),
    val difficulty: ExerciseDifficulty,
    val caloriesPerMinute: Double? = null
)

enum class ExerciseType {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    BALANCE,
    PLYOMETRIC,
    ISOMETRIC,
    COMPOUND,
    ISOLATION
}

enum class ExerciseCategory {
    CHEST,
    BACK,
    SHOULDERS,
    ARMS,
    LEGS,
    CORE,
    FULL_BODY,
    CARDIO,
    FLEXIBILITY
}

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    FOREARMS,
    CORE,
    ABS,
    OBLIQUES,
    QUADRICEPS,
    HAMSTRINGS,
    GLUTES,
    CALVES,
    TRAPS,
    LATS,
    DELTS
}

data class ExerciseSet(
    val setNumber: Int,
    val reps: Int? = null,
    val weight: Double? = null, // in kg
    val duration: Duration? = null,
    val distance: Double? = null, // in meters
    val restTime: Duration? = null,
    val completed: Boolean = false,
    val notes: String? = null
)

enum class Equipment {
    NONE,
    DUMBBELLS,
    BARBELL,
    KETTLEBELL,
    RESISTANCE_BANDS,
    PULL_UP_BAR,
    BENCH,
    TREADMILL,
    STATIONARY_BIKE,
    ROWING_MACHINE,
    ELLIPTICAL,
    YOGA_MAT,
    FOAM_ROLLER,
    MEDICINE_BALL,
    STABILITY_BALL,
    CABLE_MACHINE,
    SMITH_MACHINE,
    LEG_PRESS,
    OTHER
}

enum class ExerciseDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

data class WorkoutPlan(
    val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val goal: FitnessGoal,
    val duration: Int, // in weeks
    val workoutsPerWeek: Int,
    val difficulty: WorkoutDifficulty,
    val workouts: List<PlannedWorkout>,
    val createdBy: String, // "AI" or user ID
    val isActive: Boolean = false,
    val startDate: kotlinx.datetime.LocalDate? = null,
    val endDate: kotlinx.datetime.LocalDate? = null
)

data class PlannedWorkout(
    val dayOfWeek: Int, // 1-7 (Monday-Sunday)
    val week: Int,
    val workout: Workout,
    val isCompleted: Boolean = false,
    val completedDate: kotlinx.datetime.LocalDate? = null
)

data class WorkoutProgress(
    val userId: String,
    val exerciseId: String,
    val personalRecord: PersonalRecord? = null,
    val progressHistory: List<ProgressEntry> = emptyList(),
    val lastPerformed: LocalDateTime? = null,
    val totalSessions: Int = 0
)

data class PersonalRecord(
    val type: PRType,
    val value: Double,
    val unit: String,
    val achievedDate: LocalDateTime,
    val workoutId: String
)

enum class PRType {
    MAX_WEIGHT,
    MAX_REPS,
    LONGEST_DURATION,
    FASTEST_TIME,
    LONGEST_DISTANCE
}

data class ProgressEntry(
    val date: LocalDateTime,
    val metric: String, // "weight", "reps", "duration", etc.
    val value: Double,
    val workoutId: String
)

data class WorkoutRecommendation(
    val id: String,
    val userId: String,
    val workout: Workout,
    val reason: String,
    val confidence: Float, // 0.0 to 1.0
    val generatedBy: String, // "Gemini AI"
    val createdAt: LocalDateTime,
    val isAccepted: Boolean? = null
) 