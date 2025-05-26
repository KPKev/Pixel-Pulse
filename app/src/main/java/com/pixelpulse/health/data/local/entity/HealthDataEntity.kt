package com.pixelpulse.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "health_data",
    indices = [
        androidx.room.Index(value = ["userId"]),
        androidx.room.Index(value = ["dataType"]),
        androidx.room.Index(value = ["timestamp"]),
        androidx.room.Index(value = ["userId", "dataType"]),
        androidx.room.Index(value = ["userId", "timestamp"]),
        androidx.room.Index(value = ["userId", "dataType", "timestamp"])
    ]
)
data class HealthDataEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val dataType: String,
    val value: Double,
    val unit: String,
    val source: String,
    val confidence: Float = 1.0f,
    val notes: String? = null
) 