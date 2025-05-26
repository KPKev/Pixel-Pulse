package com.pixelpulse.health.utils

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class ApiKeyValidatorTest {

    @Test
    fun `getApiKeyStatus should return correct status map`() {
        // When
        val status = ApiKeyValidator.getApiKeyStatus()

        // Then
        assertThat(status).containsKey("GEMINI_API_KEY")
        assertThat(status).containsKey("NUTRITION_API_KEY")
        assertThat(status.size).isEqualTo(2)
    }

    @Test
    fun `validateApiKeys should return ValidationResult`() {
        // When
        val result = ApiKeyValidator.validateApiKeys()

        // Then
        assertThat(result).isNotNull()
        assertThat(result.missingKeys).isNotNull()
        assertThat(result.warnings).isNotNull()
    }

    @Test
    fun `hasRequiredApiKeys should return boolean`() {
        // When
        val hasKeys = ApiKeyValidator.hasRequiredApiKeys()

        // Then
        assertThat(hasKeys).isAnyOf(true, false)
    }
} 