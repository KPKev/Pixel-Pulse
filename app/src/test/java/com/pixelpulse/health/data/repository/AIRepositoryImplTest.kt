package com.pixelpulse.health.data.repository

import com.pixelpulse.health.data.remote.api.GeminiApiService
import com.pixelpulse.health.data.remote.dto.GeminiResponse
import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.Response
import com.google.common.truth.Truth.assertThat

class AIRepositoryImplTest {

    @Mock
    private lateinit var geminiApiService: GeminiApiService

    private lateinit var aiRepository: AIRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        aiRepository = AIRepositoryImpl(geminiApiService)
    }

    @Test
    fun `generateHealthInsights should return success when API call succeeds`() = runTest {
        // Given
        val userId = "test_user_123"
        val healthData = listOf(
            HealthData(
                id = "1",
                userId = userId,
                timestamp = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
                dataType = HealthDataType.HEART_RATE,
                value = 72.0,
                unit = "bpm",
                source = DataSource.MANUAL,
                confidence = 1.0f,
                notes = null
            )
        )
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateHealthInsights(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.generateHealthInsights(userId, healthData)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { insights ->
            assertThat(insights).isNotEmpty()
            assertThat(insights.first().userId).isEqualTo(userId)
        }
    }

    @Test
    fun `generateWorkoutPlan should return success with valid parameters`() = runTest {
        // Given
        val userId = "test_user_123"
        val fitnessGoals = listOf(FitnessGoal.WEIGHT_LOSS)
        val activityLevel = ActivityLevel.INTERMEDIATE
        val equipment = listOf(Equipment.DUMBBELLS)
        val timeConstraints = 45
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateWorkoutPlan(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.generateWorkoutPlan(
            userId, fitnessGoals, activityLevel, equipment, timeConstraints
        )

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { workoutPlan ->
            assertThat(workoutPlan.userId).isEqualTo(userId)
            assertThat(workoutPlan.name).isNotEmpty()
            assertThat(workoutPlan.workouts).isNotEmpty()
        }
    }

    @Test
    fun `generateMealPlan should return success with nutrition goals`() = runTest {
        // Given
        val userId = "test_user_123"
        val nutritionGoals = NutritionGoals(
            userId = userId,
            dailyCalories = 2000,
            protein = 150.0,
            carbs = 200.0,
            fat = 80.0,
            fiber = 25.0,
            water = 2000.0,
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
        val dietaryRestrictions = listOf(DietaryRestriction.VEGETARIAN)
        val preferences = listOf("High protein", "Low carb")
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateNutritionAdvice(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.generateMealPlan(userId, nutritionGoals, dietaryRestrictions, preferences)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { mealPlan ->
            assertThat(mealPlan.userId).isEqualTo(userId)
            assertThat(mealPlan.name).isNotEmpty()
            assertThat(mealPlan.totalCalories).isEqualTo(2000)
        }
    }

    @Test
    fun `recognizeFoodFromImage should return success with image data`() = runTest {
        // Given
        val imageData = "test_image_data".toByteArray()
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.analyzeFoodImage(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.recognizeFoodFromImage(imageData)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { scanResult ->
            assertThat(scanResult.recognizedFoods).isNotEmpty()
            assertThat(scanResult.confidence).isGreaterThan(0.0f)
        }
    }

    @Test
    fun `chatWithAI should return success with message`() = runTest {
        // Given
        val userId = "test_user_123"
        val message = "How can I improve my health?"
        val conversationHistory = emptyList<ChatMessage>()
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateHealthInsights(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.chatWithAI(userId, message, conversationHistory)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { chatMessage ->
            assertThat(chatMessage.userId).isEqualTo(userId)
            assertThat(chatMessage.content).isNotEmpty()
            assertThat(chatMessage.isFromUser).isFalse()
        }
    }

    @Test
    fun `generateMotivationalMessage should return encouraging message`() = runTest {
        // Given
        val userId = "test_user_123"
        val progress = HealthProgress(
            userId = userId,
            currentWeight = 70.0,
            targetWeight = 65.0,
            bodyFatPercentage = 15.0,
            muscleMass = 55.0,
            lastUpdated = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
        val goals = listOf(FitnessGoal.WEIGHT_LOSS)
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateHealthInsights(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.generateMotivationalMessage(userId, progress, goals)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { message ->
            assertThat(message).isNotEmpty()
            assertThat(message).contains("Great job")
        }
    }

    @Test
    fun `analyzeWorkoutPerformance should return analysis with scores`() = runTest {
        // Given
        val userId = "test_user_123"
        val workoutHistory = listOf(
            Workout(
                id = "workout_1",
                userId = userId,
                name = "Morning Run",
                type = WorkoutType.CARDIO,
                startTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
                endTime = null,
                duration = kotlin.time.Duration.parse("30m"),
                exercises = emptyList(),
                caloriesBurned = 300,
                difficulty = WorkoutDifficulty.INTERMEDIATE,
                status = WorkoutStatus.COMPLETED
            )
        )
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.generateHealthInsights(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.analyzeWorkoutPerformance(userId, workoutHistory)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { analysis ->
            assertThat(analysis.userId).isEqualTo(userId)
            assertThat(analysis.overallProgress).isAtLeast(0.0)
            assertThat(analysis.overallProgress).isAtMost(100.0)
            assertThat(analysis.recommendations).isNotEmpty()
        }
    }

    @Test
    fun `API call failure should return failure result`() = runTest {
        // Given
        val userId = "test_user_123"
        val healthData = emptyList<HealthData>()
        whenever(geminiApiService.generateHealthInsights(any(), any()))
            .thenThrow(RuntimeException("Network error"))

        // When
        val result = aiRepository.generateHealthInsights(userId, healthData)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `estimateFoodPortion should return reasonable portion size`() = runTest {
        // Given
        val imageData = "test_image_data".toByteArray()
        val recognizedFood = Food(
            id = "apple_001",
            name = "Apple",
            category = FoodCategory.FRUITS,
            nutritionPer100g = NutritionInfo(
                calories = 52.0, protein = 0.3, carbohydrates = 14.0, fat = 0.2,
                fiber = 2.4, sugar = 10.0, sodium = 1.0, cholesterol = 0.0,
                saturatedFat = 0.1, transFat = 0.0, potassium = 107.0, calcium = 6.0,
                iron = 0.1, vitaminA = 54.0, vitaminC = 4.6, vitaminD = 0.0,
                vitaminE = 0.2, vitaminK = 2.2, thiamine = 0.02, riboflavin = 0.03,
                niacin = 0.1, vitaminB6 = 0.04, folate = 3.0, vitaminB12 = 0.0,
                magnesium = 5.0, phosphorus = 11.0, zinc = 0.04, copper = 0.03,
                manganese = 0.04, selenium = 0.0
            )
        )
        val mockResponse = Response.success(GeminiResponse(candidates = emptyList()))
        whenever(geminiApiService.analyzeFoodImage(any(), any())).thenReturn(mockResponse)

        // When
        val result = aiRepository.estimateFoodPortion(imageData, recognizedFood)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.let { portionSize ->
            assertThat(portionSize).isGreaterThan(0.0)
            assertThat(portionSize).isLessThan(1000.0) // Reasonable upper bound
        }
    }
} 