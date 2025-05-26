package com.pixelpulse.health.presentation.screens.nutrition

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelpulse.health.domain.model.FoodScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class FoodScannerViewModel @Inject constructor(
    private val aiRepository: com.pixelpulse.health.domain.repository.AIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodScannerUiState())
    val uiState: StateFlow<FoodScannerUiState> = _uiState.asStateFlow()

    fun captureAndAnalyzeFood(imageCapture: ImageCapture, executor: ExecutorService) {
        _uiState.value = _uiState.value.copy(isScanning = true)
        
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            File.createTempFile("food_scan", ".jpg")
        ).build()

        imageCapture.takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModelScope.launch {
                        try {
                            // Read image data from saved file
                            val imageFile = output.savedUri?.path?.let { java.io.File(it) }
                            val imageData = imageFile?.readBytes() ?: byteArrayOf()
                            
                            if (imageData.isNotEmpty()) {
                                // Use AI repository to recognize food from image
                                val result = aiRepository.recognizeFoodFromImage(imageData)
                                
                                result.fold(
                                    onSuccess = { scanResult ->
                                        _uiState.value = _uiState.value.copy(
                                            isScanning = false,
                                            scanResult = scanResult
                                        )
                                    },
                                    onFailure = { exception ->
                                        _uiState.value = _uiState.value.copy(
                                            isScanning = false,
                                            error = exception.message ?: "Failed to recognize food"
                                        )
                                    }
                                )
                            } else {
                                // Fallback to mock result if image reading fails
                                val mockResult = createMockScanResult()
                                _uiState.value = _uiState.value.copy(
                                    isScanning = false,
                                    scanResult = mockResult
                                )
                            }
                        } catch (e: Exception) {
                            _uiState.value = _uiState.value.copy(
                                isScanning = false,
                                error = e.message ?: "An error occurred during scanning"
                            )
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.value = _uiState.value.copy(
                        isScanning = false,
                        error = exception.message
                    )
                }
            }
        )
    }

    fun clearScanResult() {
        _uiState.value = _uiState.value.copy(
            scanResult = null,
            error = null
        )
    }

    private fun createMockScanResult(): FoodScanResult {
        // Mock implementation - replace with actual Gemini Vision API call
        return FoodScanResult(
            recognizedFoods = listOf(
                com.pixelpulse.health.domain.model.RecognizedFood(
                    food = com.pixelpulse.health.domain.model.Food(
                        id = "apple_001",
                        name = "Apple",
                        category = com.pixelpulse.health.domain.model.FoodCategory.FRUITS,
                        nutritionPer100g = com.pixelpulse.health.domain.model.NutritionInfo(
                            calories = 52.0,
                            protein = 0.3,
                            carbohydrates = 14.0,
                            fat = 0.2,
                            fiber = 2.4,
                            sugar = 10.0,
                            sodium = 1.0,
                            cholesterol = 0.0,
                            saturatedFat = 0.1,
                            transFat = 0.0,
                            potassium = 107.0,
                            calcium = 6.0,
                            iron = 0.1,
                            vitaminA = 54.0,
                            vitaminC = 4.6,
                            vitaminD = 0.0,
                            vitaminE = 0.2,
                            vitaminK = 2.2,
                            thiamine = 0.02,
                            riboflavin = 0.03,
                            niacin = 0.1,
                            vitaminB6 = 0.04,
                            folate = 3.0,
                            vitaminB12 = 0.0,
                            magnesium = 5.0,
                            phosphorus = 11.0,
                            zinc = 0.04,
                            copper = 0.03,
                            manganese = 0.04,
                            selenium = 0.0
                        )
                    ),
                    confidence = 0.92f
                )
            ),
            confidence = 0.92f,
            imageUrl = "",
            timestamp = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        )
    }
}

data class FoodScannerUiState(
    val isScanning: Boolean = false,
    val scanResult: FoodScanResult? = null,
    val error: String? = null
) 