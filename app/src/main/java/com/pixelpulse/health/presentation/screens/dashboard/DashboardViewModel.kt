package com.pixelpulse.health.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    // Inject repositories here when created
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Simulate loading data
            // In real implementation, this would fetch from repositories
            _uiState.value = _uiState.value.copy(
                userName = "Alex",
                currentHeartRate = 72,
                todaySteps = 8543,
                todayCalories = 1850,
                lastNightSleep = 7.5,
                waterGoal = 2000,
                waterCurrent = 1200,
                calorieGoal = 2200,
                stepsGoal = 10000,
                aiInsights = listOf(
                    "Your heart rate variability has improved by 15% this week",
                    "Consider adding more protein to your breakfast for better energy",
                    "Your sleep quality is excellent - keep up the consistent bedtime!"
                ),
                recentActivities = listOf(
                    "Morning run - 30 minutes",
                    "Logged breakfast - 450 calories",
                    "Drank 500ml water",
                    "Completed strength training"
                ),
                isLoading = false
            )
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Simulate refresh
            kotlinx.coroutines.delay(1000)
            loadDashboardData()
        }
    }
}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val currentHeartRate: Int = 0,
    val todaySteps: Int = 0,
    val todayCalories: Int = 0,
    val lastNightSleep: Double = 0.0,
    val waterGoal: Int = 2000,
    val waterCurrent: Int = 0,
    val calorieGoal: Int = 2200,
    val stepsGoal: Int = 10000,
    val aiInsights: List<String> = emptyList(),
    val recentActivities: List<String> = emptyList(),
    val error: String? = null
) 