package com.pixelpulse.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    indices = [
        androidx.room.Index(value = ["userId"]),
        androidx.room.Index(value = ["type"]),
        androidx.room.Index(value = ["userId", "type"])
    ]
)
data class WorkoutEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val type: String,
    val duration: Long, // in milliseconds
    val caloriesBurned: Int
) 