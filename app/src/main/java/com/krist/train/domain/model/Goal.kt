package com.krist.train.domain.model

data class Goal(
    val id: Long = 0,
    val name: String,
    val sportType: String,
    val targetDateEpochMillis: Long?,
    val targetDistanceMeters: Double?,
    val targetTimeSeconds: Int?,
    val availableDaysPerWeek: Int,
    val preferredLongWorkoutDay: String,
    val riskPreference: RiskPreference,
    val notes: String,
)

enum class RiskPreference {
    Conservative,
    Balanced,
    Aggressive,
}
