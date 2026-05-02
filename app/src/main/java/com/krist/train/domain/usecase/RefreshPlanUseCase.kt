package com.krist.train.domain.usecase

import com.krist.train.data.repository.GoalRepository
import com.krist.train.domain.model.TrainingPlan

class RefreshPlanUseCase(
    private val goalRepository: GoalRepository,
    private val generateTrainingPlan: GenerateTrainingPlanUseCase,
) {
    suspend operator fun invoke(): TrainingPlan {
        val goal = requireNotNull(goalRepository.latestGoal()) { "Create a goal before generating a plan" }
        return generateTrainingPlan(goal)
    }
}
