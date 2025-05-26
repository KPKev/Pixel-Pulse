package com.pixelpulse.health.data.local.dao

import androidx.room.*

@Dao
interface NutritionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodEntry(foodEntry: FoodEntryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionGoals(goals: NutritionGoalsEntity)
    
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%'")
    suspend fun searchFoods(query: String): List<FoodEntity>
    
    @Query("SELECT * FROM food_entries WHERE userId = :userId AND timestamp BETWEEN :startDate AND :endDate")
    fun getFoodEntriesForDateRange(
        userId: String, 
        startDate: kotlinx.datetime.LocalDateTime, 
        endDate: kotlinx.datetime.LocalDateTime
    ): Flow<List<FoodEntryEntity>>
    
    @Query("SELECT * FROM nutrition_goals WHERE userId = :userId LIMIT 1")
    suspend fun getNutritionGoals(userId: String): NutritionGoalsEntity?
    
    @Update
    suspend fun updateNutritionGoals(goals: NutritionGoalsEntity)
    
    @Delete
    suspend fun deleteFoodEntry(foodEntry: FoodEntryEntity)
} 