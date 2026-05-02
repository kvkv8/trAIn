package com.krist.train.data.remote.ai

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig(),
)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double = 0.4,
    val responseMimeType: String = "application/json",
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null,
)

@Serializable
data class GeminiPart(
    val text: String,
)

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList(),
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent? = null,
)

@Serializable
data class GeminiModelsResponse(
    val models: List<GeminiModelDto> = emptyList(),
)

@Serializable
data class GeminiModelDto(
    val name: String,
    val displayName: String? = null,
    val supportedGenerationMethods: List<String> = emptyList(),
)
