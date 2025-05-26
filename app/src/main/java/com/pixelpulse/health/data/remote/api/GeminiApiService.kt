package com.pixelpulse.health.data.remote.api

import com.pixelpulse.health.data.remote.dto.GeminiRequest
import com.pixelpulse.health.data.remote.dto.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GeminiApiService {
    
    @POST("models/gemini-pro:generateContent")
    suspend fun generateHealthInsights(
        @Header("Authorization") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
    
    @POST("models/gemini-pro-vision:generateContent")
    suspend fun analyzeFoodImage(
        @Header("Authorization") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
    
    @POST("models/gemini-pro:generateContent")
    suspend fun generateWorkoutPlan(
        @Header("Authorization") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
    
    @POST("models/gemini-pro:generateContent")
    suspend fun generateNutritionAdvice(
        @Header("Authorization") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
} 