package com.krist.train.data.remote.ai

import retrofit2.HttpException

class GeminiAiProvider(
    private val api: GeminiApi,
) : AiProvider {
    suspend fun listAvailableModels(apiKey: String): List<AiModel> = geminiCall {
        api.listModels(apiKey)
            .models
            .filter { "generateContent" in it.supportedGenerationMethods }
            .map { model ->
                AiModel(
                    name = model.name,
                    displayName = model.displayName ?: model.name.removePrefix("models/"),
                )
            }
    }

    suspend fun testConnection(apiKey: String, model: String): String = generateTrainingPlanJson(
        prompt = "Return only this JSON: {\"overview\":\"ok\",\"assumptions\":[],\"weeks\":[],\"recoveryGuidance\":[],\"warningSigns\":[]}",
        apiKey = apiKey,
        model = model,
    )

    override suspend fun generateTrainingPlanJson(prompt: String, apiKey: String, model: String): String = geminiCall {
        val response = api.generateContent(
            model = normalizeModelName(model),
            apiKey = apiKey,
            request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(prompt)),
                        role = "user",
                    ),
                ),
            ),
        )

        response.candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?.trim()
            ?.removePrefix("```json")
            ?.removePrefix("```")
            ?.removeSuffix("```")
            ?.trim()
            ?: error("AI provider returned an empty response")
    }

    private fun normalizeModelName(model: String): String {
        val trimmed = model.trim()
        require(trimmed.isNotBlank()) { "Select a Google AI model in Settings first" }
        return if (trimmed.startsWith("models/")) trimmed else "models/$trimmed"
    }

    private suspend fun <T> geminiCall(block: suspend () -> T): T = try {
        block()
    } catch (error: HttpException) {
        val details = error.response()?.errorBody()?.string()?.take(800).orEmpty()
        val message = buildString {
            append("Google AI request failed (HTTP ${error.code()})")
            if (details.isNotBlank()) append(": $details")
        }
        throw IllegalStateException(message, error)
    }
}
