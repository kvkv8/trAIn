package com.krist.train.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrainingPlan(
    val overview: String = "",
    @Serializable(with = FlexibleStringListSerializer::class)
    val assumptions: List<String> = emptyList(),
    val weeks: List<TrainingWeek> = emptyList(),
    @Serializable(with = FlexibleStringListSerializer::class)
    val recoveryGuidance: List<String> = emptyList(),
    @Serializable(with = FlexibleStringListSerializer::class)
    val warningSigns: List<String> = emptyList(),
)

@Serializable
data class TrainingWeek(
    val week: Int = 0,
    val focus: String = "",
    val workouts: List<Workout> = emptyList(),
)

@Serializable
data class Workout(
    val day: String = "",
    val type: String = "",
    val title: String = "",
    val body: String = "",
    val heartZone: String = "",
    val details: String = "",
    val purpose: String = "",
)
