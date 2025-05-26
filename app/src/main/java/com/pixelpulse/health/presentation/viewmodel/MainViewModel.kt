package com.pixelpulse.health.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelpulse.health.utils.ApiKeyValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    // Inject repositories here when created
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                // Validate API keys first
                val apiValidation = ApiKeyValidator.validateApiKeys()
                
                // Simulate initialization delay for splash screen
                kotlinx.coroutines.delay(1500)
                
                // Check if user is authenticated and onboarding is completed
                // This would typically check SharedPreferences or DataStore
                val isOnboardingCompleted = checkOnboardingStatus()
                val isAuthenticated = checkAuthenticationStatus()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = isAuthenticated,
                    isOnboardingCompleted = isOnboardingCompleted,
                    requiresAuthentication = !isAuthenticated && isOnboardingCompleted,
                    isBiometricEnabled = true,
                    hasValidApiKeys = apiValidation.isValid,
                    apiKeyWarnings = apiValidation.warnings
                )
            } catch (e: Exception) {
                // Handle initialization error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    isOnboardingCompleted = false,
                    requiresAuthentication = false,
                    isBiometricEnabled = false,
                    hasValidApiKeys = false,
                    error = e.message
                )
            }
        }
    }
    
    private suspend fun checkOnboardingStatus(): Boolean {
        // Check if user has completed onboarding
        // In a real implementation, this would check DataStore or SharedPreferences
        return try {
            // For now, return false to show onboarding on first launch
            // In production, implement actual persistence check
            false
        } catch (e: Exception) {
            android.util.Log.e("MainViewModel", "Error checking onboarding status", e)
            false
        }
    }
    
    private suspend fun checkAuthenticationStatus(): Boolean {
        // Check if user is authenticated (biometric or other auth)
        // In a real implementation, this would check authentication state
        return try {
            // For now, return false to require authentication
            // In production, implement actual authentication state check
            false
        } catch (e: Exception) {
            android.util.Log.e("MainViewModel", "Error checking authentication status", e)
            false
        }
    }

    fun onBiometricAuthenticationResult(success: Boolean) {
        _uiState.value = _uiState.value.copy(
            isAuthenticated = success,
            requiresAuthentication = false
        )
    }

    fun onOnboardingCompleted() {
        _uiState.value = _uiState.value.copy(
            isOnboardingCompleted = true
        )
    }

    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(
            isDarkTheme = !_uiState.value.isDarkTheme
        )
    }
}

data class MainUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val isOnboardingCompleted: Boolean = false,
    val requiresAuthentication: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isDarkTheme: Boolean = false,
    val hasValidApiKeys: Boolean = true,
    val apiKeyWarnings: List<String> = emptyList(),
    val error: String? = null
) 