package com.krist.train.data.remote.ai

interface AiProvider {
    suspend fun generateTrainingPlanJson(prompt: String, apiKey: String, model: String): String
}

data class AiModel(
    val name: String,
    val displayName: String,
)
