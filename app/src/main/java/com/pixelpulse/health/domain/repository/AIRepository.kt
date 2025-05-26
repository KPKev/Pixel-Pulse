package com.pixelpulse.health.domain.repository

import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    // Health insights
    suspend fun generateHealthInsights(
        userId: String,
        healthData: List<HealthData>,
        timeRange: TimeRange = TimeRange.LAST_7_DAYS
    ): Result<List<HealthInsight>>
    
    suspend fun generatePersonalizedRecommendations(
        userId: String,
        userProfile: User,
        recentActivity: List<HealthData>
    ): Result<List<AIRecommendation>>
    
    // Workout AI
    suspend fun generateWorkoutPlan(
        userId: String,
        fitnessGoals: List<FitnessGoal>,
        currentFitnessLevel: ActivityLevel,
        availableEquipment: List<Equipment>,
        timeConstraints: Int // minutes per session
    ): Result<WorkoutPlan>
    
    suspend fun analyzeWorkoutPerformance(
        userId: String,
        workoutHistory: List<Workout>
    ): Result<WorkoutAnalysis>
    
    suspend fun suggestWorkoutModifications(
        userId: String,
        currentWorkout: Workout,
        userFeedback: String
    ): Result<List<WorkoutModification>>
    
    // Nutrition AI
    suspend fun generateMealPlan(
        userId: String,
        nutritionGoals: NutritionGoals,
        dietaryRestrictions: List<DietaryRestriction>,
        preferences: List<String>
    ): Result<MealPlan>
    
    suspend fun analyzeNutritionalDeficiencies(
        userId: String,
        recentNutrition: List<DailyNutrition>
    ): Result<List<NutritionalDeficiency>>
    
    suspend fun suggestMealImprovements(
        userId: String,
        currentMeal: List<FoodEntry>
    ): Result<List<MealSuggestion>>
    
    // Food recognition
    suspend fun recognizeFoodFromImage(imageData: ByteArray): Result<FoodScanResult>
    suspend fun estimateFoodPortion(
        imageData: ByteArray,
        recognizedFood: Food
    ): Result<Double> // estimated grams
    
    // Health coaching
    suspend fun generateMotivationalMessage(
        userId: String,
        currentProgress: HealthProgress,
        goals: List<FitnessGoal>
    ): Result<String>
    
    suspend fun answerHealthQuestion(
        userId: String,
        question: String,
        context: HealthContext
    ): Result<String>
    
    suspend fun generateHealthReport(
        userId: String,
        timeRange: TimeRange
    ): Result<HealthReport>
    
    // Conversation
    suspend fun chatWithAI(
        userId: String,
        message: String,
        conversationHistory: List<ChatMessage>
    ): Result<ChatMessage>
    
    suspend fun getChatHistory(userId: String): Flow<List<ChatMessage>>
}

enum class TimeRange {
    LAST_24_HOURS,
    LAST_7_DAYS,
    LAST_30_DAYS,
    LAST_90_DAYS
}

data class HealthInsight(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val category: InsightCategory,
    val priority: InsightPriority,
    val actionable: Boolean,
    val recommendations: List<String>,
    val confidence: Float,
    val generatedAt: kotlinx.datetime.LocalDateTime
)

enum class InsightCategory {
    HEART_HEALTH,
    SLEEP_QUALITY,
    ACTIVITY_LEVEL,
    NUTRITION,
    STRESS_MANAGEMENT,
    WEIGHT_MANAGEMENT,
    GENERAL_WELLNESS
}

enum class InsightPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

data class AIRecommendation(
    val id: String,
    val userId: String,
    val type: RecommendationType,
    val title: String,
    val description: String,
    val actionItems: List<String>,
    val expectedBenefit: String,
    val difficulty: RecommendationDifficulty,
    val estimatedTimeToSeeResults: String,
    val confidence: Float,
    val generatedAt: kotlinx.datetime.LocalDateTime
)

enum class RecommendationDifficulty {
    EASY,
    MODERATE,
    CHALLENGING
}

data class WorkoutAnalysis(
    val userId: String,
    val overallProgress: String,
    val strengths: List<String>,
    val areasForImprovement: List<String>,
    val recommendedChanges: List<String>,
    val injuryRiskAssessment: String,
    val progressTrend: ProgressTrend
)

enum class ProgressTrend {
    IMPROVING,
    STABLE,
    DECLINING,
    INCONSISTENT
}

data class WorkoutModification(
    val exerciseId: String,
    val modificationType: ModificationType,
    val description: String,
    val reason: String
)

enum class ModificationType {
    INCREASE_WEIGHT,
    DECREASE_WEIGHT,
    INCREASE_REPS,
    DECREASE_REPS,
    INCREASE_SETS,
    DECREASE_SETS,
    CHANGE_EXERCISE,
    ADD_REST,
    REDUCE_REST
}

data class NutritionalDeficiency(
    val nutrient: String,
    val currentIntake: Double,
    val recommendedIntake: Double,
    val severity: DeficiencySeverity,
    val symptoms: List<String>,
    val foodSources: List<String>
)

enum class DeficiencySeverity {
    MILD,
    MODERATE,
    SEVERE
}

data class MealSuggestion(
    val type: SuggestionType,
    val description: String,
    val foodsToAdd: List<Food>,
    val foodsToReduce: List<Food>,
    val expectedBenefit: String
)

enum class SuggestionType {
    ADD_PROTEIN,
    ADD_VEGETABLES,
    REDUCE_SODIUM,
    REDUCE_SUGAR,
    INCREASE_FIBER,
    BALANCE_MACROS
}

data class HealthProgress(
    val userId: String,
    val goalsAchieved: Int,
    val totalGoals: Int,
    val weeklyProgress: Double, // percentage
    val monthlyProgress: Double, // percentage
    val keyMetrics: Map<String, Double>
)

data class HealthContext(
    val userProfile: User,
    val recentHealthData: List<HealthData>,
    val currentGoals: List<FitnessGoal>,
    val medicalConditions: List<String>
)

data class HealthReport(
    val userId: String,
    val timeRange: TimeRange,
    val summary: String,
    val keyFindings: List<String>,
    val recommendations: List<String>,
    val progressMetrics: Map<String, Double>,
    val generatedAt: kotlinx.datetime.LocalDateTime
)

data class ChatMessage(
    val id: String,
    val userId: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: kotlinx.datetime.LocalDateTime,
    val context: String? = null
) 