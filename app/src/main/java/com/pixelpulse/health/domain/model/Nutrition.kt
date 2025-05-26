package com.pixelpulse.health.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class Food(
    val id: String,
    val name: String,
    val brand: String? = null,
    val barcode: String? = null,
    val category: FoodCategory,
    val nutritionPer100g: NutritionInfo,
    val servingSizes: List<ServingSize> = emptyList(),
    val allergens: List<Allergen> = emptyList(),
    val isVerified: Boolean = false,
    val imageUrl: String? = null
)

data class NutritionInfo(
    val calories: Double, // kcal
    val protein: Double, // g
    val carbohydrates: Double, // g
    val fat: Double, // g
    val fiber: Double, // g
    val sugar: Double, // g
    val sodium: Double, // mg
    val cholesterol: Double, // mg
    val saturatedFat: Double, // g
    val transFat: Double, // g
    val potassium: Double, // mg
    val calcium: Double, // mg
    val iron: Double, // mg
    val vitaminA: Double, // IU
    val vitaminC: Double, // mg
    val vitaminD: Double, // IU
    val vitaminE: Double, // mg
    val vitaminK: Double, // mcg
    val thiamine: Double, // mg
    val riboflavin: Double, // mg
    val niacin: Double, // mg
    val vitaminB6: Double, // mg
    val folate: Double, // mcg
    val vitaminB12: Double, // mcg
    val magnesium: Double, // mg
    val phosphorus: Double, // mg
    val zinc: Double, // mg
    val copper: Double, // mg
    val manganese: Double, // mg
    val selenium: Double // mcg
)

enum class FoodCategory {
    FRUITS,
    VEGETABLES,
    GRAINS,
    PROTEIN,
    DAIRY,
    NUTS_SEEDS,
    OILS_FATS,
    BEVERAGES,
    SNACKS,
    DESSERTS,
    CONDIMENTS,
    HERBS_SPICES,
    PROCESSED_FOODS,
    FAST_FOOD,
    SUPPLEMENTS,
    OTHER
}

data class ServingSize(
    val name: String, // "1 cup", "1 medium", "1 slice"
    val grams: Double,
    val isDefault: Boolean = false
)

enum class Allergen {
    MILK,
    EGGS,
    FISH,
    SHELLFISH,
    TREE_NUTS,
    PEANUTS,
    WHEAT,
    SOYBEANS,
    SESAME,
    SULFITES,
    GLUTEN
}

data class FoodEntry(
    val id: String,
    val userId: String,
    val foodId: String,
    val food: Food,
    val quantity: Double, // in grams
    val servingSize: ServingSize? = null,
    val mealType: MealType,
    val timestamp: LocalDateTime,
    val notes: String? = null
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    PRE_WORKOUT,
    POST_WORKOUT,
    OTHER
}

data class DailyNutrition(
    val userId: String,
    val date: LocalDate,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
    val totalFiber: Double,
    val totalSugar: Double,
    val totalSodium: Double,
    val meals: Map<MealType, List<FoodEntry>>,
    val waterIntake: Double, // in ml
    val nutritionGoals: NutritionGoals,
    val caloriesBurned: Int = 0
)

data class NutritionGoals(
    val dailyCalories: Int,
    val proteinPercentage: Double, // 10-35%
    val carbsPercentage: Double, // 45-65%
    val fatPercentage: Double, // 20-35%
    val fiberGrams: Double,
    val sodiumMg: Double,
    val waterMl: Double,
    val customGoals: Map<String, Double> = emptyMap()
)

data class MealPlan(
    val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val meals: Map<LocalDate, DailyMealPlan>,
    val nutritionGoals: NutritionGoals,
    val dietaryRestrictions: List<DietaryRestriction> = emptyList(),
    val createdBy: String, // "AI" or user ID
    val isActive: Boolean = false
)

data class DailyMealPlan(
    val date: LocalDate,
    val breakfast: List<PlannedMeal> = emptyList(),
    val lunch: List<PlannedMeal> = emptyList(),
    val dinner: List<PlannedMeal> = emptyList(),
    val snacks: List<PlannedMeal> = emptyList(),
    val totalCalories: Double,
    val totalNutrition: NutritionInfo
)

data class PlannedMeal(
    val food: Food,
    val quantity: Double, // in grams
    val servingSize: ServingSize? = null,
    val isCompleted: Boolean = false
)

enum class DietaryRestriction {
    VEGETARIAN,
    VEGAN,
    GLUTEN_FREE,
    DAIRY_FREE,
    NUT_FREE,
    LOW_CARB,
    KETO,
    PALEO,
    MEDITERRANEAN,
    DASH,
    LOW_SODIUM,
    LOW_FAT,
    HIGH_PROTEIN,
    DIABETIC,
    HEART_HEALTHY,
    KOSHER,
    HALAL,
    RAW_FOOD,
    WHOLE30
}

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<String>,
    val prepTime: kotlin.time.Duration,
    val cookTime: kotlin.time.Duration,
    val servings: Int,
    val difficulty: RecipeDifficulty,
    val cuisine: String? = null,
    val tags: List<String> = emptyList(),
    val nutritionPerServing: NutritionInfo,
    val imageUrl: String? = null,
    val rating: Double? = null,
    val reviews: Int = 0,
    val createdBy: String,
    val dietaryRestrictions: List<DietaryRestriction> = emptyList()
)

data class RecipeIngredient(
    val food: Food,
    val quantity: Double,
    val unit: String,
    val notes: String? = null
)

enum class RecipeDifficulty {
    EASY,
    MEDIUM,
    HARD
}

data class NutritionRecommendation(
    val id: String,
    val userId: String,
    val type: RecommendationType,
    val title: String,
    val description: String,
    val recommendedFoods: List<Food> = emptyList(),
    val recommendedRecipes: List<Recipe> = emptyList(),
    val reason: String,
    val confidence: Float, // 0.0 to 1.0
    val generatedBy: String, // "Gemini AI"
    val createdAt: LocalDateTime,
    val isAccepted: Boolean? = null
)

enum class RecommendationType {
    MEAL_SUGGESTION,
    NUTRIENT_DEFICIENCY,
    CALORIE_ADJUSTMENT,
    HYDRATION_REMINDER,
    SUPPLEMENT_SUGGESTION,
    DIETARY_IMPROVEMENT,
    WEIGHT_GOAL_SUPPORT
}

data class FoodScanResult(
    val recognizedFoods: List<RecognizedFood>,
    val confidence: Float,
    val imageUrl: String,
    val timestamp: LocalDateTime
)

data class RecognizedFood(
    val food: Food,
    val confidence: Float,
    val boundingBox: BoundingBox? = null
)

data class BoundingBox(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
) 