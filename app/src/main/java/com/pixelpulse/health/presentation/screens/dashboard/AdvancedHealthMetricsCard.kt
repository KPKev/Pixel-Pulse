package com.pixelpulse.health.presentation.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pixelpulse.health.domain.model.HealthDataType
import com.pixelpulse.health.presentation.ui.theme.*

@Composable
fun AdvancedHealthMetricsCard(
    healthMetrics: List<HealthMetric>,
    onMetricClick: (HealthDataType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Health Metrics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = { /* Navigate to detailed view */ }) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = "View Trends",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(healthMetrics) { metric ->
                    HealthMetricItem(
                        metric = metric,
                        onClick = { onMetricClick(metric.type) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMetricItem(
    metric: HealthMetric,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = metric.icon,
                contentDescription = metric.name,
                tint = metric.color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = metric.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${metric.currentValue} ${metric.unit}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mini trend chart
            MiniTrendChart(
                dataPoints = metric.trendData,
                color = metric.color,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Trend indicator
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (metric.trend > 0) Icons.Default.TrendingUp 
                                 else if (metric.trend < 0) Icons.Default.TrendingDown 
                                 else Icons.Default.TrendingFlat,
                    contentDescription = "Trend",
                    tint = if (metric.trend > 0) Color.Green 
                           else if (metric.trend < 0) Color.Red 
                           else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = "${if (metric.trend > 0) "+" else ""}${metric.trend}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (metric.trend > 0) Color.Green 
                            else if (metric.trend < 0) Color.Red 
                            else Color.Gray
                )
            }
        }
    }
}

@Composable
fun MiniTrendChart(
    dataPoints: List<Float>,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (dataPoints.size < 2) return@Canvas
        
        val width = size.width
        val height = size.height
        val stepX = width / (dataPoints.size - 1)
        
        val minValue = dataPoints.minOrNull() ?: 0f
        val maxValue = dataPoints.maxOrNull() ?: 1f
        val valueRange = maxValue - minValue
        
        val path = Path()
        
        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - ((value - minValue) / valueRange * height)
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        drawPath(
            path = path,
            color = color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
        
        // Draw data points
        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - ((value - minValue) / valueRange * height)
            
            drawCircle(
                color = color,
                radius = 2.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

data class HealthMetric(
    val type: HealthDataType,
    val name: String,
    val currentValue: String,
    val unit: String,
    val icon: ImageVector,
    val color: Color,
    val trend: Float, // Percentage change
    val trendData: List<Float> // Last 7 days data for mini chart
)

// Sample data for demonstration
@Composable
fun getSampleHealthMetrics(): List<HealthMetric> {
    return listOf(
        HealthMetric(
            type = HealthDataType.HEART_RATE,
            name = "Heart Rate",
            currentValue = "72",
            unit = "BPM",
            icon = Icons.Default.Favorite,
            color = Color.Red,
            trend = 2.5f,
            trendData = listOf(70f, 68f, 72f, 75f, 71f, 69f, 72f)
        ),
        HealthMetric(
            type = HealthDataType.STEPS,
            name = "Steps",
            currentValue = "8,543",
            unit = "",
            icon = Icons.Default.DirectionsWalk,
            color = Color.Blue,
            trend = 15.2f,
            trendData = listOf(6000f, 7200f, 8100f, 7800f, 8500f, 9200f, 8543f)
        ),
        HealthMetric(
            type = HealthDataType.SLEEP_DURATION,
            name = "Sleep",
            currentValue = "7.5",
            unit = "hrs",
            icon = Icons.Default.Bedtime,
            color = Color.Magenta,
            trend = -5.1f,
            trendData = listOf(8.2f, 7.8f, 8.0f, 7.2f, 7.5f, 7.1f, 7.5f)
        ),
        HealthMetric(
            type = HealthDataType.CALORIES_BURNED,
            name = "Calories",
            currentValue = "1,850",
            unit = "kcal",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFFF6B35),
            trend = 8.7f,
            trendData = listOf(1600f, 1750f, 1820f, 1680f, 1900f, 1950f, 1850f)
        )
    )
} 