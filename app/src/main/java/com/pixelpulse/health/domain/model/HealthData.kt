package com.pixelpulse.health.domain.model

import kotlinx.datetime.LocalDateTime

data class HealthData(
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val dataType: HealthDataType,
    val value: Double,
    val unit: String,
    val source: DataSource,
    val confidence: Float = 1.0f, // 0.0 to 1.0
    val notes: String? = null
)

enum class HealthDataType {
    HEART_RATE,
    BLOOD_PRESSURE_SYSTOLIC,
    BLOOD_PRESSURE_DIASTOLIC,
    BLOOD_OXYGEN,
    BODY_TEMPERATURE,
    WEIGHT,
    BODY_FAT_PERCENTAGE,
    MUSCLE_MASS,
    BONE_DENSITY,
    STEPS,
    DISTANCE,
    CALORIES_BURNED,
    ACTIVE_MINUTES,
    SLEEP_DURATION,
    SLEEP_QUALITY,
    STRESS_LEVEL,
    HYDRATION,
    BLOOD_GLUCOSE
}

enum class DataSource {
    MANUAL_ENTRY,
    PIXEL_SENSORS,
    GOOGLE_FIT,
    HEALTH_CONNECT,
    WEARABLE_DEVICE,
    MEDICAL_DEVICE,
    THIRD_PARTY_APP
}

data class HeartRateData(
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val heartRate: Int, // BPM
    val restingHeartRate: Int? = null,
    val maxHeartRate: Int? = null,
    val heartRateZone: HeartRateZone,
    val source: DataSource,
    val context: HeartRateContext = HeartRateContext.RESTING
)

enum class HeartRateZone {
    RESTING,     // < 60% max HR
    FAT_BURN,    // 60-70% max HR
    CARDIO,      // 70-85% max HR
    PEAK,        // 85-100% max HR
    ABOVE_MAX    // > 100% max HR
}

enum class HeartRateContext {
    RESTING,
    EXERCISE,
    STRESS,
    SLEEP,
    RECOVERY
}

data class BloodPressureReading(
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val systolic: Int, // mmHg
    val diastolic: Int, // mmHg
    val pulse: Int? = null, // BPM
    val category: BloodPressureCategory,
    val source: DataSource,
    val notes: String? = null
)

enum class BloodPressureCategory {
    NORMAL,          // < 120/80
    ELEVATED,        // 120-129/<80
    HIGH_STAGE_1,    // 130-139/80-89
    HIGH_STAGE_2,    // ≥140/≥90
    HYPERTENSIVE_CRISIS // >180/>120
}

data class SleepData(
    val id: String,
    val userId: String,
    val date: kotlinx.datetime.LocalDate,
    val bedtime: LocalDateTime,
    val wakeTime: LocalDateTime,
    val totalSleepDuration: kotlin.time.Duration,
    val sleepStages: List<SleepStage>,
    val sleepQuality: SleepQuality,
    val sleepScore: Int, // 0-100
    val source: DataSource
)

data class SleepStage(
    val stage: SleepStageType,
    val startTime: LocalDateTime,
    val duration: kotlin.time.Duration
)

enum class SleepStageType {
    AWAKE,
    LIGHT_SLEEP,
    DEEP_SLEEP,
    REM_SLEEP
}

enum class SleepQuality {
    POOR,
    FAIR,
    GOOD,
    EXCELLENT
}

data class ActivityData(
    val id: String,
    val userId: String,
    val date: kotlinx.datetime.LocalDate,
    val steps: Int,
    val distance: Double, // in meters
    val caloriesBurned: Int,
    val activeMinutes: Int,
    val sedentaryMinutes: Int,
    val floors: Int? = null,
    val source: DataSource
)

data class StressData(
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val stressLevel: Int, // 1-10 scale
    val stressCategory: StressCategory,
    val triggers: List<String> = emptyList(),
    val copingStrategies: List<String> = emptyList(),
    val source: DataSource
)

enum class StressCategory {
    LOW,
    MODERATE,
    HIGH,
    SEVERE
}

data class HydrationData(
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val amount: Double, // in ml
    val beverageType: BeverageType,
    val source: DataSource
)

enum class BeverageType {
    WATER,
    COFFEE,
    TEA,
    JUICE,
    SPORTS_DRINK,
    ALCOHOL,
    OTHER
} 