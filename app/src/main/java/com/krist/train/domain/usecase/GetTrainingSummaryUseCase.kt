package com.krist.train.domain.usecase

import com.krist.train.data.repository.ActivityRepository
import com.krist.train.domain.analysis.TrainingSummary
import com.krist.train.domain.analysis.TrainingSummaryCalculator

class GetTrainingSummaryUseCase(
    private val activityRepository: ActivityRepository,
    private val calculator: TrainingSummaryCalculator,
) {
    suspend operator fun invoke(): TrainingSummary = calculator.calculate(activityRepository.getActivities())
}
