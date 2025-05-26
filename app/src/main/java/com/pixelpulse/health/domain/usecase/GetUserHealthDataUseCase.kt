package com.pixelpulse.health.domain.usecase

import com.pixelpulse.health.domain.model.HealthData
import com.pixelpulse.health.domain.model.HealthDataType
import com.pixelpulse.health.domain.repository.HealthDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetUserHealthDataUseCase @Inject constructor(
    private val healthDataRepository: HealthDataRepository
) {
    operator fun invoke(
        userId: String,
        dataType: HealthDataType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<HealthData>> {
        return healthDataRepository.getHealthDataByType(
            userId = userId,
            dataType = dataType,
            startDate = startDate,
            endDate = endDate
        )
    }
} 