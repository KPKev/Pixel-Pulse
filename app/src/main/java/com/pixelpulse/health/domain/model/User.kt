package com.pixelpulse.health.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class User(
    val id: String,
    val email: String,
    val name: String,
    val dateOfBirth: LocalDate,
    val gender: Gender,
    val height: Double, // in cm
    val weight: Double, // in kg
    val activityLevel: ActivityLevel,
    val fitnessGoals: List<FitnessGoal>,
    val healthConditions: List<String> = emptyList(),
    val medications: List<String> = emptyList(),
    val emergencyContact: EmergencyContact? = null,
    val preferences: UserPreferences,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class Gender {
    MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
}

enum class ActivityLevel {
    SEDENTARY,      // Little to no exercise
    LIGHTLY_ACTIVE, // Light exercise 1-3 days/week
    MODERATELY_ACTIVE, // Moderate exercise 3-5 days/week
    VERY_ACTIVE,    // Hard exercise 6-7 days/week
    EXTREMELY_ACTIVE // Very hard exercise, physical job
}

enum class FitnessGoal {
    WEIGHT_LOSS,
    WEIGHT_GAIN,
    MUSCLE_BUILDING,
    CARDIOVASCULAR_FITNESS,
    STRENGTH_TRAINING,
    FLEXIBILITY,
    GENERAL_WELLNESS,
    STRESS_REDUCTION,
    BETTER_SLEEP,
    INJURY_RECOVERY
}

data class EmergencyContact(
    val name: String,
    val phoneNumber: String,
    val relationship: String
)

data class UserPreferences(
    val units: Units = Units.METRIC,
    val notifications: NotificationPreferences = NotificationPreferences(),
    val privacy: PrivacyPreferences = PrivacyPreferences(),
    val theme: ThemePreference = ThemePreference.SYSTEM
)

enum class Units {
    METRIC, IMPERIAL
}

data class NotificationPreferences(
    val workoutReminders: Boolean = true,
    val mealReminders: Boolean = true,
    val hydrationReminders: Boolean = true,
    val medicationReminders: Boolean = true,
    val healthAlerts: Boolean = true,
    val weeklyReports: Boolean = true
)

data class PrivacyPreferences(
    val shareDataWithHealthConnect: Boolean = true,
    val shareDataWithGoogleFit: Boolean = true,
    val allowAnalytics: Boolean = true,
    val allowPersonalization: Boolean = true
)

enum class ThemePreference {
    LIGHT, DARK, SYSTEM
} 