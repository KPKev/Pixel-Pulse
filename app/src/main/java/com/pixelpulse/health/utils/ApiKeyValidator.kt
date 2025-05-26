package com.pixelpulse.health.utils

import android.util.Log
import com.pixelpulse.health.BuildConfig

object ApiKeyValidator {
    
    private const val TAG = "ApiKeyValidator"
    
    data class ValidationResult(
        val isValid: Boolean,
        val missingKeys: List<String> = emptyList(),
        val warnings: List<String> = emptyList()
    )
    
    fun validateApiKeys(): ValidationResult {
        val missingKeys = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        // Check Gemini API Key
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            missingKeys.add("GEMINI_API_KEY")
            Log.w(TAG, "Gemini API key is missing - AI features will be disabled")
        } else if (BuildConfig.GEMINI_API_KEY.length < 20) {
            warnings.add("GEMINI_API_KEY appears to be invalid (too short)")
        }
        
        // Check Nutrition API Key
        if (BuildConfig.NUTRITION_API_KEY.isBlank()) {
            missingKeys.add("NUTRITION_API_KEY")
            Log.w(TAG, "Nutrition API key is missing - food database features will be limited")
        } else if (BuildConfig.NUTRITION_API_KEY.length < 10) {
            warnings.add("NUTRITION_API_KEY appears to be invalid (too short)")
        }
        
        val isValid = missingKeys.isEmpty()
        
        if (isValid) {
            Log.i(TAG, "All API keys validated successfully")
        } else {
            Log.e(TAG, "Missing API keys: ${missingKeys.joinToString(", ")}")
        }
        
        if (warnings.isNotEmpty()) {
            Log.w(TAG, "API key warnings: ${warnings.joinToString(", ")}")
        }
        
        return ValidationResult(
            isValid = isValid,
            missingKeys = missingKeys,
            warnings = warnings
        )
    }
    
    fun getApiKeyStatus(): Map<String, String> {
        return mapOf(
            "GEMINI_API_KEY" to if (BuildConfig.GEMINI_API_KEY.isNotBlank()) "✓ Configured" else "✗ Missing",
            "NUTRITION_API_KEY" to if (BuildConfig.NUTRITION_API_KEY.isNotBlank()) "✓ Configured" else "✗ Missing"
        )
    }
    
    fun hasRequiredApiKeys(): Boolean {
        // At minimum, we need Gemini API key for core AI functionality
        return BuildConfig.GEMINI_API_KEY.isNotBlank()
    }
} 