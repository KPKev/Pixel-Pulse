package com.pixelpulse.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_goals")
data class NutritionGoalsEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val dailyCalories: Int,
    val proteinPercentage: Double,
    val carbsPercentage: Double,
    val fatPercentage: Double,
    val fiberGrams: Double,
    val sodiumMg: Double,
    val waterMl: Double
) 