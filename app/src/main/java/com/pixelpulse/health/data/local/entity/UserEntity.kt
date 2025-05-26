package com.pixelpulse.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val dateOfBirth: LocalDate,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activityLevel: String,
    val fitnessGoals: List<String>,
    val healthConditions: List<String>,
    val medications: List<String>,
    val emergencyContactName: String?,
    val emergencyContactPhone: String?,
    val emergencyContactRelationship: String?,
    val units: String,
    val notificationPreferences: String, // JSON string
    val privacyPreferences: String, // JSON string
    val theme: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) 