package com.krist.train.domain.usecase

import com.krist.train.core.security.SecureSettingsStore
import com.krist.train.data.remote.ai.AiProvider
import com.krist.train.data.repository.GoalRepository
import com.krist.train.data.repository.TrainingPlanRepository
import com.krist.train.domain.model.Goal
import com.krist.train.domain.model.TrainingPlan
import com.krist.train.domain.model.TrainingPlanJsonExtractor
import com.krist.train.domain.prompt.TrainingPlanPromptBuilder
import kotlinx.serialization.json.Json

class GenerateTrainingPlanUseCase(
    private val getTrainingSummary: GetTrainingSummaryUseCase,
    private val goalRepository: GoalRepository,
    private val planRepository: TrainingPlanRepository,
    private val aiProvider: AiProvider,
    private val settingsStore: SecureSettingsStore,
    private val promptBuilder: TrainingPlanPromptBuilder,
    private val json: Json,
) {
    suspend operator fun invoke(goal: Goal): TrainingPlan {
        val apiKey = settingsStore.geminiApiKey.orEmpty()
        require(apiKey.isNotBlank()) { "Set Google AI API key in Settings first" }
        val model = settingsStore.geminiModel.orEmpty()
        require(model.isNotBlank()) { "Select a Google AI model in Settings first" }

        val goalId = if (goal.id == 0L) goalRepository.saveGoal(goal) else goal.id
        val savedGoal = goal.copy(id = goalId)
        val prompt = promptBuilder.build(getTrainingSummary(), savedGoal)
        val planJson = aiProvider.generateTrainingPlanJson(prompt, apiKey, model)
        val extractedPlanJson = TrainingPlanJsonExtractor.extractFirstObject(planJson)
        val plan = json.decodeFromString<TrainingPlan>(extractedPlanJson)
        planRepository.savePlan(goalId = goalId, rawJson = extractedPlanJson)
        return plan
    }
}
