package com.pixelpulse.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "food_entries",
    indices = [
        androidx.room.Index(value = ["userId"]),
        androidx.room.Index(value = ["foodId"]),
        androidx.room.Index(value = ["timestamp"]),
        androidx.room.Index(value = ["userId", "timestamp"]),
        androidx.room.Index(value = ["mealType"])
    ]
)
data class FoodEntryEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val foodId: String,
    val quantity: Double,
    val mealType: String,
    val timestamp: LocalDateTime
) 