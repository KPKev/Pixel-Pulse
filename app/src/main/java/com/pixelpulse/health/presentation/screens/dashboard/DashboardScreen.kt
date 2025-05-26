package com.pixelpulse.health.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelpulse.health.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToWorkout: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Header
            WelcomeHeader(userName = uiState.userName)
        }

        item {
            // AI Insights Card
            AIInsightsCard(
                insights = uiState.aiInsights,
                onViewMore = { /* Navigate to AI chat */ }
            )
        }

        item {
            // Health Metrics Overview
            HealthMetricsOverview(
                heartRate = uiState.currentHeartRate,
                steps = uiState.todaySteps,
                calories = uiState.todayCalories,
                sleep = uiState.lastNightSleep
            )
        }

        item {
            // Quick Actions
            QuickActionsSection(
                onStartWorkout = onNavigateToWorkout,
                onLogFood = onNavigateToNutrition,
                onScanFood = { /* Navigate to food scanner */ },
                onViewProfile = onNavigateToProfile
            )
        }

        item {
            // Today's Goals Progress
            TodaysGoalsCard(
                waterGoal = uiState.waterGoal,
                waterCurrent = uiState.waterCurrent,
                calorieGoal = uiState.calorieGoal,
                calorieCurrent = uiState.todayCalories,
                stepsGoal = uiState.stepsGoal,
                stepsCurrent = uiState.todaySteps
            )
        }

        item {
            // Recent Activities
            RecentActivitiesCard(
                activities = uiState.recentActivities
            )
        }
    }
}

@Composable
fun WelcomeHeader(userName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            HealthGradientStart,
                            HealthGradientEnd
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Good morning, $userName!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready to achieve your health goals today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun AIInsightsCard(
    insights: List<String>,
    onViewMore: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Insights",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Insights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(onClick = onViewMore) {
                    Text("View More")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            insights.take(2).forEach { insight ->
                Text(
                    text = "• $insight",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun HealthMetricsOverview(
    heartRate: Int,
    steps: Int,
    calories: Int,
    sleep: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Today's Health Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    listOf(
                        HealthMetric("Heart Rate", "$heartRate BPM", Icons.Default.Favorite, HeartRateRed),
                        HealthMetric("Steps", "$steps", Icons.Default.DirectionsWalk, StepsGreen),
                        HealthMetric("Calories", "$calories kcal", Icons.Default.LocalFireDepartment, CaloriesOrange),
                        HealthMetric("Sleep", "${sleep}h", Icons.Default.Bedtime, SleepPurple)
                    )
                ) { metric ->
                    HealthMetricCard(metric = metric)
                }
            }
        }
    }
}

@Composable
fun HealthMetricCard(metric: HealthMetric) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = metric.icon,
                contentDescription = metric.title,
                tint = metric.color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = metric.value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = metric.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onStartWorkout: () -> Unit,
    onLogFood: () -> Unit,
    onScanFood: () -> Unit,
    onViewProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.FitnessCenter,
                    label = "Start Workout",
                    onClick = onStartWorkout
                )
                QuickActionButton(
                    icon = Icons.Default.Restaurant,
                    label = "Log Food",
                    onClick = onLogFood
                )
                QuickActionButton(
                    icon = Icons.Default.CameraAlt,
                    label = "Scan Food",
                    onClick = onScanFood
                )
                QuickActionButton(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = onViewProfile
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TodaysGoalsCard(
    waterGoal: Int,
    waterCurrent: Int,
    calorieGoal: Int,
    calorieCurrent: Int,
    stepsGoal: Int,
    stepsCurrent: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Today's Goals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            GoalProgressItem(
                title = "Water",
                current = waterCurrent,
                goal = waterGoal,
                unit = "ml",
                color = WaterBlue
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            GoalProgressItem(
                title = "Calories",
                current = calorieCurrent,
                goal = calorieGoal,
                unit = "kcal",
                color = CaloriesOrange
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            GoalProgressItem(
                title = "Steps",
                current = stepsCurrent,
                goal = stepsGoal,
                unit = "",
                color = StepsGreen
            )
        }
    }
}

@Composable
fun GoalProgressItem(
    title: String,
    current: Int,
    goal: Int,
    unit: String,
    color: androidx.compose.ui.graphics.Color
) {
    val progress = (current.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$current / $goal $unit",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun RecentActivitiesCard(
    activities: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Recent Activities",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (activities.isEmpty()) {
                Text(
                    text = "No recent activities",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                activities.forEach { activity ->
                    Text(
                        text = "• $activity",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

data class HealthMetric(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
) 