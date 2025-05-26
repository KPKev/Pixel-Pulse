package com.pixelpulse.health.domain.usecase

import com.pixelpulse.health.domain.model.*
import com.pixelpulse.health.domain.repository.HealthDataRepository
import com.pixelpulse.health.data.remote.api.GeminiApiService
import com.pixelpulse.health.data.remote.dto.*
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GenerateHealthInsightsUseCase @Inject constructor(
    private val healthDataRepository: HealthDataRepository,
    private val geminiApiService: GeminiApiService
) {
    
    suspend operator fun invoke(
        userId: String,
        timeRange: TimeRange = TimeRange.LAST_7_DAYS
    ): Result<List<HealthInsight>> {
        return try {
            // Gather user's health data
            val endDate = LocalDateTime.now()
            val startDate = when (timeRange) {
                TimeRange.LAST_24_HOURS -> endDate.minusDays(1)
                TimeRange.LAST_7_DAYS -> endDate.minusDays(7)
                TimeRange.LAST_30_DAYS -> endDate.minusDays(30)
                TimeRange.LAST_90_DAYS -> endDate.minusDays(90)
            }
            
            val healthData = healthDataRepository.getHealthDataByType(
                userId = userId,
                dataType = HealthDataType.HEART_RATE,
                startDate = startDate,
                endDate = endDate
            ).first()
            
            // Create prompt for Gemini
            val prompt = buildHealthInsightPrompt(healthData, timeRange)
            
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.3f,
                    maxOutputTokens = 500
                )
            )
            
            val response = geminiApiService.generateHealthInsights(
                apiKey = "Bearer YOUR_API_KEY", // This should come from BuildConfig
                request = request
            )
            
            if (response.isSuccessful && response.body() != null) {
                val insights = parseHealthInsights(response.body()!!)
                Result.success(insights)
            } else {
                Result.failure(Exception("Failed to generate insights"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun buildHealthInsightPrompt(
        healthData: List<HealthData>,
        timeRange: TimeRange
    ): String {
        val dataPoints = healthData.joinToString("\n") { 
            "${it.timestamp}: ${it.value} ${it.unit}" 
        }
        
        return """
            As a health AI assistant, analyze the following health data and provide 3-5 actionable insights:
            
            Time Range: ${timeRange.name}
            Health Data (Heart Rate):
            $dataPoints
            
            Please provide insights in the following format:
            1. [Insight Title]: [Brief explanation and recommendation]
            
            Focus on:
            - Trends and patterns
            - Health recommendations
            - Potential concerns
            - Positive achievements
            
            Keep insights concise, actionable, and encouraging.
        """.trimIndent()
    }
    
    private fun parseHealthInsights(response: GeminiResponse): List<HealthInsight> {
        val content = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: return emptyList()
        
        return content.split("\n")
            .filter { it.matches(Regex("\\d+\\..*")) }
            .map { line ->
                val parts = line.split(":", limit = 2)
                HealthInsight(
                    id = generateId(),
                    title = parts.getOrNull(0)?.substringAfter(".")?.trim() ?: "Health Insight",
                    description = parts.getOrNull(1)?.trim() ?: line,
                    type = InsightType.GENERAL,
                    confidence = 0.8f,
                    generatedAt = LocalDateTime.now(),
                    source = "Gemini AI"
                )
            }
    }
    
    private fun generateId(): String = java.util.UUID.randomUUID().toString()
}

enum class TimeRange {
    LAST_24_HOURS,
    LAST_7_DAYS,
    LAST_30_DAYS,
    LAST_90_DAYS
}

data class HealthInsight(
    val id: String,
    val title: String,
    val description: String,
    val type: InsightType,
    val confidence: Float,
    val generatedAt: LocalDateTime,
    val source: String,
    val actionItems: List<String> = emptyList()
)

enum class InsightType {
    GENERAL,
    HEART_HEALTH,
    ACTIVITY,
    SLEEP,
    NUTRITION,
    STRESS,
    WARNING,
    ACHIEVEMENT
} 