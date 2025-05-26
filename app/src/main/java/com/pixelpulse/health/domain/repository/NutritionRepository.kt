package com.pixelpulse.health.domain.repository

import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface NutritionRepository {
    // Food management
    suspend fun searchFoods(query: String): Result<List<Food>>
    suspend fun getFoodById(foodId: String): Result<Food?>
    suspend fun getFoodByBarcode(barcode: String): Result<Food?>
    suspend fun createFood(food: Food): Result<Food>
    suspend fun updateFood(food: Food): Result<Food>
    
    // Food entries
    suspend fun logFoodEntry(foodEntry: FoodEntry): Result<FoodEntry>
    suspend fun getFoodEntriesForDate(userId: String, date: LocalDate): Flow<List<FoodEntry>>
    suspend fun getFoodEntriesForDateRange(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<FoodEntry>>
    suspend fun updateFoodEntry(foodEntry: FoodEntry): Result<FoodEntry>
    suspend fun deleteFoodEntry(entryId: String): Result<Unit>
    
    // Daily nutrition
    suspend fun getDailyNutrition(userId: String, date: LocalDate): Result<DailyNutrition?>
    suspend fun calculateDailyNutrition(userId: String, date: LocalDate): Result<DailyNutrition>
    
    // Nutrition goals
    suspend fun setNutritionGoals(userId: String, goals: NutritionGoals): Result<NutritionGoals>
    suspend fun getNutritionGoals(userId: String): Result<NutritionGoals?>
    suspend fun updateNutritionGoals(userId: String, goals: NutritionGoals): Result<NutritionGoals>
    
    // Meal planning
    suspend fun createMealPlan(mealPlan: MealPlan): Result<MealPlan>
    suspend fun getActiveMealPlan(userId: String): Result<MealPlan?>
    suspend fun updateMealPlan(mealPlan: MealPlan): Result<MealPlan>
    suspend fun deleteMealPlan(planId: String): Result<Unit>
    
    // Recipes
    suspend fun searchRecipes(query: String, dietaryRestrictions: List<DietaryRestriction> = emptyList()): Result<List<Recipe>>
    suspend fun getRecipeById(recipeId: String): Result<Recipe?>
    suspend fun createRecipe(recipe: Recipe): Result<Recipe>
    suspend fun getFavoriteRecipes(userId: String): Flow<List<Recipe>>
    
    // Food scanning
    suspend fun scanFood(imageData: ByteArray): Result<FoodScanResult>
    suspend fun analyzeFoodImage(imageData: ByteArray): Result<List<RecognizedFood>>
    
    // Nutrition recommendations
    suspend fun generateNutritionRecommendation(userId: String): Result<NutritionRecommendation>
    suspend fun getNutritionRecommendations(userId: String): Flow<List<NutritionRecommendation>>
    suspend fun acceptNutritionRecommendation(recommendationId: String): Result<Unit>
    suspend fun rejectNutritionRecommendation(recommendationId: String): Result<Unit>
    
    // Water tracking
    suspend fun logWaterIntake(userId: String, amount: Double, timestamp: LocalDateTime): Result<Unit>
    suspend fun getWaterIntakeForDate(userId: String, date: LocalDate): Result<Double>
} 