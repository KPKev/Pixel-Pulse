package com.pixelpulse.health.presentation.screens.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Nutrition",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Add food */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Daily Nutrition Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Today's Nutrition",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            NutritionSummaryItem(
                                label = "Calories",
                                value = "1,850",
                                goal = "2,200",
                                unit = "kcal"
                            )
                            NutritionSummaryItem(
                                label = "Protein",
                                value = "85",
                                goal = "120",
                                unit = "g"
                            )
                            NutritionSummaryItem(
                                label = "Carbs",
                                value = "220",
                                goal = "275",
                                unit = "g"
                            )
                            NutritionSummaryItem(
                                label = "Fat",
                                value = "65",
                                goal = "75",
                                unit = "g"
                            )
                        }
                    }
                }
            }

            item {
                // AI Nutrition Insights
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "AI Insights",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Nutrition Insights",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "• You're 350 calories below your goal. Consider a healthy snack.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Great protein intake! You're on track for muscle recovery.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Try adding more fiber-rich foods to your next meal.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                // Food Logging Options
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Log Food",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FoodLoggingButton(
                                icon = Icons.Default.CameraAlt,
                                label = "Scan Food",
                                onClick = { /* Open camera scanner */ }
                            )
                            FoodLoggingButton(
                                icon = Icons.Default.QrCodeScanner,
                                label = "Scan Barcode",
                                onClick = { /* Open barcode scanner */ }
                            )
                            FoodLoggingButton(
                                icon = Icons.Default.Search,
                                label = "Search Food",
                                onClick = { /* Open food search */ }
                            )
                            FoodLoggingButton(
                                icon = Icons.Default.Edit,
                                label = "Manual Entry",
                                onClick = { /* Open manual entry */ }
                            )
                        }
                    }
                }
            }

            item {
                // Today's Meals
                Text(
                    text = "Today's Meals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                listOf(
                    MealEntry("Breakfast", "Oatmeal with berries", "450 kcal", "8:30 AM"),
                    MealEntry("Lunch", "Grilled chicken salad", "520 kcal", "12:45 PM"),
                    MealEntry("Snack", "Greek yogurt", "150 kcal", "3:15 PM"),
                    MealEntry("Dinner", "Salmon with vegetables", "730 kcal", "7:00 PM")
                )
            ) { meal ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (meal.type) {
                                "Breakfast" -> Icons.Default.WbSunny
                                "Lunch" -> Icons.Default.LunchDining
                                "Dinner" -> Icons.Default.DinnerDining
                                else -> Icons.Default.Restaurant
                            },
                            contentDescription = meal.type,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = meal.type,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = meal.food,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${meal.calories} • ${meal.time}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { /* Edit meal */ }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Meal"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionSummaryItem(
    label: String,
    value: String,
    goal: String,
    unit: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "/ $goal $unit",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FoodLoggingButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp)
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
            style = MaterialTheme.typography.bodySmall
        )
    }
}

data class MealEntry(
    val type: String,
    val food: String,
    val calories: String,
    val time: String
) 