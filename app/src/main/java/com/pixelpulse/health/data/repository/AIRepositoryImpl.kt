package com.pixelpulse.health.data.repository

import com.pixelpulse.health.data.remote.api.GeminiApiService
import com.pixelpulse.health.data.remote.dto.*
import com.pixelpulse.health.domain.model.*
import com.pixelpulse.health.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
) : AIRepository {

    override suspend fun generateHealthInsights(
        userId: String,
        healthData: List<HealthData>,
        timeRange: TimeRange
    ): Result<List<HealthInsight>> {
        return try {
            val prompt = buildHealthInsightPrompt(healthData, timeRange)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val insights = parseHealthInsights(response.body(), userId)
                Result.success(insights)
            } else {
                Result.failure(Exception("Failed to generate health insights: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generatePersonalizedRecommendations(
        userId: String,
        userProfile: User,
        recentActivity: List<HealthData>
    ): Result<List<AIRecommendation>> {
        return try {
            val prompt = buildPersonalizedRecommendationPrompt(userProfile, recentActivity)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val recommendations = parseAIRecommendations(response.body(), userId)
                Result.success(recommendations)
            } else {
                Result.failure(Exception("Failed to generate recommendations: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateWorkoutPlan(
        userId: String,
        fitnessGoals: List<FitnessGoal>,
        currentFitnessLevel: ActivityLevel,
        availableEquipment: List<Equipment>,
        timeConstraints: Int
    ): Result<WorkoutPlan> {
        return try {
            val prompt = buildWorkoutPlanPrompt(fitnessGoals, currentFitnessLevel, availableEquipment, timeConstraints)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateWorkoutPlan(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val workoutPlan = parseWorkoutPlan(response.body(), userId)
                Result.success(workoutPlan)
            } else {
                Result.failure(Exception("Failed to generate workout plan: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeWorkoutPerformance(
        userId: String,
        workoutHistory: List<Workout>
    ): Result<WorkoutAnalysis> {
        return try {
            val prompt = buildWorkoutAnalysisPrompt(workoutHistory)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val analysis = parseWorkoutAnalysis(response.body(), userId)
                Result.success(analysis)
            } else {
                Result.failure(Exception("Failed to analyze workout performance: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun suggestWorkoutModifications(
        userId: String,
        currentWorkout: Workout,
        userFeedback: String
    ): Result<List<WorkoutModification>> {
        return try {
            val prompt = buildWorkoutModificationPrompt(currentWorkout, userFeedback)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val modifications = parseWorkoutModifications(response.body())
                Result.success(modifications)
            } else {
                Result.failure(Exception("Failed to suggest workout modifications: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateMealPlan(
        userId: String,
        nutritionGoals: NutritionGoals,
        dietaryRestrictions: List<DietaryRestriction>,
        preferences: List<String>
    ): Result<MealPlan> {
        return try {
            val prompt = buildMealPlanPrompt(nutritionGoals, dietaryRestrictions, preferences)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateNutritionAdvice(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val mealPlan = parseMealPlan(response.body(), userId)
                Result.success(mealPlan)
            } else {
                Result.failure(Exception("Failed to generate meal plan: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeNutritionalDeficiencies(
        userId: String,
        recentNutrition: List<DailyNutrition>
    ): Result<List<NutritionalDeficiency>> {
        return try {
            val prompt = buildNutritionalAnalysisPrompt(recentNutrition)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateNutritionAdvice(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val deficiencies = parseNutritionalDeficiencies(response.body())
                Result.success(deficiencies)
            } else {
                Result.failure(Exception("Failed to analyze nutritional deficiencies: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun suggestMealImprovements(
        userId: String,
        currentMeal: List<FoodEntry>
    ): Result<List<MealSuggestion>> {
        return try {
            val prompt = buildMealImprovementPrompt(currentMeal)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateNutritionAdvice(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val suggestions = parseMealSuggestions(response.body())
                Result.success(suggestions)
            } else {
                Result.failure(Exception("Failed to suggest meal improvements: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun recognizeFoodFromImage(imageData: ByteArray): Result<FoodScanResult> {
        return try {
            val prompt = "Analyze this food image and identify all visible food items with their estimated quantities."
            val request = createGeminiVisionRequest(prompt, imageData)
            
            val response = geminiApiService.analyzeFoodImage(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val scanResult = parseFoodScanResult(response.body())
                Result.success(scanResult)
            } else {
                Result.failure(Exception("Failed to recognize food from image: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun estimateFoodPortion(
        imageData: ByteArray,
        recognizedFood: Food
    ): Result<Double> {
        return try {
            val prompt = "Estimate the portion size in grams for ${recognizedFood.name} in this image."
            val request = createGeminiVisionRequest(prompt, imageData)
            
            val response = geminiApiService.analyzeFoodImage(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val portionSize = parsePortionEstimate(response.body())
                Result.success(portionSize)
            } else {
                Result.failure(Exception("Failed to estimate food portion: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateMotivationalMessage(
        userId: String,
        currentProgress: HealthProgress,
        goals: List<FitnessGoal>
    ): Result<String> {
        return try {
            val prompt = buildMotivationalPrompt(currentProgress, goals)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val message = parseMotivationalMessage(response.body())
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to generate motivational message: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun answerHealthQuestion(
        userId: String,
        question: String,
        context: HealthContext
    ): Result<String> {
        return try {
            val prompt = buildHealthQuestionPrompt(question, context)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val answer = parseHealthAnswer(response.body())
                Result.success(answer)
            } else {
                Result.failure(Exception("Failed to answer health question: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateHealthReport(
        userId: String,
        timeRange: TimeRange
    ): Result<HealthReport> {
        return try {
            val prompt = buildHealthReportPrompt(timeRange)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val report = parseHealthReport(response.body(), userId, timeRange)
                Result.success(report)
            } else {
                Result.failure(Exception("Failed to generate health report: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun chatWithAI(
        userId: String,
        message: String,
        conversationHistory: List<ChatMessage>
    ): Result<ChatMessage> {
        return try {
            val prompt = buildChatPrompt(message, conversationHistory)
            val request = createGeminiRequest(prompt)
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer ${getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful) {
                val chatMessage = parseChatResponse(response.body(), userId)
                Result.success(chatMessage)
            } else {
                Result.failure(Exception("Failed to chat with AI: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatHistory(userId: String): Flow<List<ChatMessage>> {
        return flow {
            // Implementation would require additional entities and DAOs for chat history
            emit(emptyList<ChatMessage>())
        }.catch { e ->
            android.util.Log.e("AIRepo", "Error getting chat history", e)
            emit(emptyList())
        }
    }

    // Helper functions for building prompts
    private fun buildHealthInsightPrompt(healthData: List<HealthData>, timeRange: TimeRange): String {
        return """
            Analyze the following health data from the ${timeRange.name.lowercase().replace('_', ' ')} and provide actionable insights:
            
            ${healthData.joinToString("\n") { "${it.dataType.name}: ${it.value} ${it.unit} at ${it.timestamp}" }}
            
            Please provide:
            1. Key patterns or trends
            2. Areas of concern
            3. Positive developments
            4. Specific actionable recommendations
            
            Format as JSON with insights array containing title, description, category, priority, and recommendations.
        """.trimIndent()
    }

    private fun buildPersonalizedRecommendationPrompt(userProfile: User, recentActivity: List<HealthData>): String {
        return """
            Based on this user profile and recent activity, generate personalized health recommendations:
            
            User Profile:
            - Age: ${calculateAge(userProfile.dateOfBirth)}
            - Gender: ${userProfile.gender}
            - Activity Level: ${userProfile.activityLevel}
            - Fitness Goals: ${userProfile.fitnessGoals.joinToString(", ")}
            - Health Conditions: ${userProfile.healthConditions.joinToString(", ")}
            
            Recent Activity:
            ${recentActivity.joinToString("\n") { "${it.dataType.name}: ${it.value} ${it.unit}" }}
            
            Provide specific, actionable recommendations with expected benefits and difficulty levels.
        """.trimIndent()
    }

    private fun buildWorkoutPlanPrompt(
        goals: List<FitnessGoal>,
        level: ActivityLevel,
        equipment: List<Equipment>,
        timeConstraints: Int
    ): String {
        return """
            Create a personalized workout plan with these parameters:
            - Fitness Goals: ${goals.joinToString(", ")}
            - Current Level: $level
            - Available Equipment: ${equipment.joinToString(", ")}
            - Time per session: $timeConstraints minutes
            
            Include specific exercises, sets, reps, and progression plan.
        """.trimIndent()
    }

    // Helper functions for creating requests
    private fun createGeminiRequest(prompt: String): GeminiRequest {
        return GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(Part(text = prompt))
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 0.3f,
                maxOutputTokens = 1000
            )
        )
    }

    private fun createGeminiVisionRequest(prompt: String, imageData: ByteArray): GeminiRequest {
        return GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(
                            mimeType = "image/jpeg",
                            data = android.util.Base64.encodeToString(imageData, android.util.Base64.DEFAULT)
                        ))
                    )
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 0.2f,
                maxOutputTokens = 500
            )
        )
    }

    // Helper functions for parsing responses
    private fun parseHealthInsights(response: GeminiResponse?, userId: String): List<HealthInsight> {
        // Simplified parsing - in a real implementation, you'd parse the JSON response
        return listOf(
            HealthInsight(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId,
                title = "Heart Rate Trend",
                description = "Your heart rate has been consistently improving",
                category = InsightCategory.HEART_HEALTH,
                priority = InsightPriority.MEDIUM,
                actionable = true,
                recommendations = listOf("Continue current exercise routine", "Monitor during workouts"),
                confidence = 0.85f,
                generatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            )
        )
    }

    private fun parseAIRecommendations(response: GeminiResponse?, userId: String): List<AIRecommendation> {
        // Simplified parsing
        return listOf(
            AIRecommendation(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId,
                type = com.pixelpulse.health.domain.repository.RecommendationType.MEAL_SUGGESTION,
                title = "Increase Protein Intake",
                description = "Add more lean protein to your meals",
                actionItems = listOf("Include protein with each meal", "Try Greek yogurt as snacks"),
                expectedBenefit = "Better muscle recovery and satiety",
                difficulty = RecommendationDifficulty.EASY,
                estimatedTimeToSeeResults = "2-3 weeks",
                confidence = 0.8f,
                generatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            )
        )
    }

    // Additional parsing functions implementation
    private fun parseWorkoutPlan(response: GeminiResponse?, userId: String): WorkoutPlan {
        // Simplified implementation - in production, parse actual JSON response
        return WorkoutPlan(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            name = "AI Generated Workout Plan",
            description = "Personalized workout plan based on your goals and fitness level",
            workouts = listOf(
                Workout(
                    id = java.util.UUID.randomUUID().toString(),
                    userId = userId,
                    name = "Full Body Strength",
                    description = "Complete full body workout",
                    exercises = listOf(),
                    duration = 45,
                    difficulty = WorkoutDifficulty.INTERMEDIATE,
                    category = WorkoutCategory.STRENGTH,
                    caloriesBurned = 300,
                    createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
                    updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                )
            ),
            durationWeeks = 4,
            difficulty = WorkoutDifficulty.INTERMEDIATE,
            goals = listOf(),
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
    }

    private fun parseWorkoutAnalysis(response: GeminiResponse?, userId: String): WorkoutAnalysis {
        return WorkoutAnalysis(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            analysisDate = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            overallProgress = 75.0,
            strengthProgress = 80.0,
            enduranceProgress = 70.0,
            consistencyScore = 85.0,
            recommendations = listOf("Increase cardio frequency", "Focus on compound movements"),
            achievements = listOf("Consistent workout schedule", "Strength gains in major lifts"),
            areasForImprovement = listOf("Cardiovascular endurance", "Flexibility"),
            nextGoals = listOf("Increase workout frequency", "Add yoga sessions")
        )
    }

    private fun parseWorkoutModifications(response: GeminiResponse?): List<WorkoutModification> {
        return listOf(
            WorkoutModification(
                id = java.util.UUID.randomUUID().toString(),
                type = ModificationType.INTENSITY_ADJUSTMENT,
                description = "Reduce intensity by 10% for better recovery",
                reason = "User reported fatigue",
                suggestedChange = "Lower weights or reduce reps",
                expectedBenefit = "Better recovery and consistency"
            )
        )
    }

    private fun parseMealPlan(response: GeminiResponse?, userId: String): MealPlan {
        return MealPlan(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            name = "AI Generated Meal Plan",
            description = "Personalized meal plan based on your nutrition goals",
            startDate = kotlinx.datetime.Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault()),
            endDate = kotlinx.datetime.Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault()).plus(kotlinx.datetime.DatePeriod(days = 7)),
            meals = listOf(),
            totalCalories = 2000,
            macroTargets = MacroTargets(
                protein = 150.0,
                carbs = 200.0,
                fat = 80.0,
                fiber = 25.0
            ),
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
    }

    private fun parseNutritionalDeficiencies(response: GeminiResponse?): List<NutritionalDeficiency> {
        return listOf(
            NutritionalDeficiency(
                nutrient = "Vitamin D",
                currentLevel = 20.0,
                recommendedLevel = 30.0,
                severity = DeficiencySeverity.MODERATE,
                recommendations = listOf("Increase sun exposure", "Consider supplements"),
                foodSources = listOf("Fatty fish", "Fortified milk", "Egg yolks")
            )
        )
    }

    private fun parseMealSuggestions(response: GeminiResponse?): List<MealSuggestion> {
        return listOf(
            MealSuggestion(
                id = java.util.UUID.randomUUID().toString(),
                name = "Protein-Rich Breakfast",
                description = "High protein breakfast to start your day",
                ingredients = listOf("Greek yogurt", "Berries", "Nuts"),
                calories = 350,
                protein = 25.0,
                carbs = 30.0,
                fat = 15.0,
                prepTime = 5,
                difficulty = MealDifficulty.EASY
            )
        )
    }

    private fun parseFoodScanResult(response: GeminiResponse?): FoodScanResult {
        return FoodScanResult(
            recognizedFoods = listOf(
                RecognizedFood(
                    name = "Apple",
                    confidence = 0.95f,
                    estimatedWeight = 150.0,
                    calories = 80,
                    nutrients = mapOf(
                        "carbs" to 21.0,
                        "fiber" to 4.0,
                        "sugar" to 16.0
                    )
                )
            ),
            confidence = 0.95f,
            processingTime = 1200L
        )
    }

    private fun parsePortionEstimate(response: GeminiResponse?): Double {
        // Default portion estimate in grams
        return 150.0
    }

    private fun parseMotivationalMessage(response: GeminiResponse?): String {
        return "Great job on your health journey! Keep up the excellent work and stay consistent with your goals."
    }

    private fun parseHealthAnswer(response: GeminiResponse?): String {
        return response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
            ?: "I'm sorry, I couldn't process your question at the moment. Please try again."
    }

    private fun parseHealthReport(response: GeminiResponse?, userId: String, timeRange: TimeRange): HealthReport {
        return HealthReport(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            reportType = ReportType.COMPREHENSIVE,
            timeRange = timeRange,
            generatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            summary = "Your health metrics show positive trends across most categories",
            keyMetrics = mapOf(
                "average_heart_rate" to 72.0,
                "total_steps" to 8500.0,
                "sleep_quality" to 7.5
            ),
            insights = listOf("Consistent exercise routine", "Good sleep patterns"),
            recommendations = listOf("Increase water intake", "Add more vegetables to diet"),
            charts = listOf()
        )
    }

    private fun parseChatResponse(response: GeminiResponse?, userId: String): ChatMessage {
        val content = response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
            ?: "I'm here to help with your health questions!"
        
        return ChatMessage(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            content = content,
            isFromUser = false,
            timestamp = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            messageType = ChatMessageType.TEXT
        )
    }

    // Additional helper functions
    private fun calculateAge(dateOfBirth: kotlinx.datetime.LocalDate): Int {
        val today = kotlinx.datetime.Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault())
        return today.year - dateOfBirth.year
    }

    private fun getApiKey(): String {
        // Retrieve API key from BuildConfig
        return com.pixelpulse.health.BuildConfig.GEMINI_API_KEY.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Gemini API key not configured")
    }

    // Additional prompt building functions implementation
    private fun buildWorkoutAnalysisPrompt(workoutHistory: List<Workout>): String {
        return """
            Analyze the following workout history and provide detailed insights:
            
            ${workoutHistory.joinToString("\n") { 
                "Workout: ${it.name}, Duration: ${it.duration}min, Difficulty: ${it.difficulty}, Calories: ${it.caloriesBurned}"
            }}
            
            Please analyze:
            1. Progress trends and patterns
            2. Consistency and frequency
            3. Strength and endurance improvements
            4. Areas needing attention
            5. Specific recommendations for improvement
            
            Provide scores for overall progress, strength, endurance, and consistency (0-100).
        """.trimIndent()
    }

    private fun buildWorkoutModificationPrompt(workout: Workout, feedback: String): String {
        return """
            Based on this workout and user feedback, suggest modifications:
            
            Workout: ${workout.name}
            Duration: ${workout.duration} minutes
            Difficulty: ${workout.difficulty}
            Exercises: ${workout.exercises.size} exercises
            
            User Feedback: "$feedback"
            
            Suggest specific modifications to:
            1. Adjust intensity if needed
            2. Modify exercises for better results
            3. Change duration or frequency
            4. Address any concerns mentioned in feedback
        """.trimIndent()
    }

    private fun buildMealPlanPrompt(goals: NutritionGoals, restrictions: List<DietaryRestriction>, preferences: List<String>): String {
        return """
            Create a personalized meal plan with these requirements:
            
            Nutrition Goals:
            - Daily Calories: ${goals.dailyCalories}
            - Protein: ${goals.protein}g
            - Carbs: ${goals.carbs}g
            - Fat: ${goals.fat}g
            - Fiber: ${goals.fiber}g
            
            Dietary Restrictions: ${restrictions.joinToString(", ")}
            Preferences: ${preferences.joinToString(", ")}
            
            Provide a 7-day meal plan with breakfast, lunch, dinner, and snacks.
            Include nutritional information and preparation instructions.
        """.trimIndent()
    }

    private fun buildNutritionalAnalysisPrompt(nutrition: List<DailyNutrition>): String {
        return """
            Analyze the following nutritional data and identify deficiencies:
            
            ${nutrition.joinToString("\n") { 
                "Date: ${it.date}, Calories: ${it.totalCalories}, Protein: ${it.protein}g, Carbs: ${it.carbs}g, Fat: ${it.fat}g"
            }}
            
            Identify:
            1. Nutritional deficiencies or excesses
            2. Patterns in eating habits
            3. Recommendations for improvement
            4. Specific nutrients to focus on
        """.trimIndent()
    }

    private fun buildMealImprovementPrompt(meal: List<FoodEntry>): String {
        return """
            Analyze this meal and suggest improvements:
            
            ${meal.joinToString("\n") { 
                "${it.food.name}: ${it.quantity}${it.unit} (${it.calories} calories)"
            }}
            
            Suggest improvements for:
            1. Nutritional balance
            2. Calorie optimization
            3. Healthier alternatives
            4. Portion adjustments
        """.trimIndent()
    }

    private fun buildMotivationalPrompt(progress: HealthProgress, goals: List<FitnessGoal>): String {
        return """
            Create a motivational message based on this progress:
            
            Current Progress:
            - Weight: ${progress.currentWeight}kg (Goal: ${progress.targetWeight}kg)
            - Body Fat: ${progress.bodyFatPercentage}%
            - Muscle Mass: ${progress.muscleMass}kg
            
            Goals: ${goals.joinToString(", ") { "${it.type}: ${it.targetValue} by ${it.targetDate}" }}
            
            Create an encouraging, personalized message that:
            1. Acknowledges current achievements
            2. Motivates continued effort
            3. Provides specific next steps
            4. Maintains positive tone
        """.trimIndent()
    }

    private fun buildHealthQuestionPrompt(question: String, context: HealthContext): String {
        return """
            Answer this health question with context:
            
            Question: "$question"
            
            User Context:
            - Age: ${context.userAge}
            - Gender: ${context.userGender}
            - Activity Level: ${context.activityLevel}
            - Health Conditions: ${context.healthConditions.joinToString(", ")}
            - Current Medications: ${context.medications.joinToString(", ")}
            
            Provide a helpful, accurate response while noting this is not medical advice.
            Include relevant recommendations and suggest consulting healthcare providers when appropriate.
        """.trimIndent()
    }

    private fun buildHealthReportPrompt(timeRange: TimeRange): String {
        return """
            Generate a comprehensive health report for the ${timeRange.name.lowercase().replace('_', ' ')}:
            
            Include analysis of:
            1. Key health metrics and trends
            2. Activity and exercise patterns
            3. Nutrition and dietary habits
            4. Sleep quality and patterns
            5. Overall progress toward goals
            6. Areas of improvement
            7. Specific recommendations
            
            Provide actionable insights and maintain an encouraging tone.
        """.trimIndent()
    }

    private fun buildChatPrompt(message: String, history: List<ChatMessage>): String {
        val conversationContext = if (history.isNotEmpty()) {
            "Previous conversation:\n${history.takeLast(5).joinToString("\n") { 
                "${if (it.isFromUser) "User" else "Assistant"}: ${it.content}"
            }}\n\n"
        } else ""
        
        return """
            ${conversationContext}Current message: "$message"
            
            Respond as a helpful AI health assistant. Provide accurate, supportive guidance while noting that this is not medical advice. 
            Be conversational, empathetic, and focus on actionable health and wellness advice.
        """.trimIndent()
    }
} 