package com.krist.train.domain.analysis

data class TrainingSummary(
    val activityCount: Int,
    val sportTypes: List<String>,
    val lastFourWeeksDistanceMeters: Double,
    val lastEightWeeksDistanceMeters: Double,
    val averageWeeklyDistanceMeters: Double,
    val averageWeeklyMovingTimeSeconds: Int,
    val longestActivityMeters: Double,
    val longestActivityMovingTimeSeconds: Int,
    val averageActivitiesPerWeek: Double,
    val averageHeartRate: Double?,
    val estimatedThresholdPaceSecondsPerKm: Int?,
    val estimatedThresholdSpeedMetersPerSecond: Double?,
    val weeklySummaries: List<WeeklySummary>,
)

data class WeeklySummary(
    val weekStartEpochMillis: Long,
    val activityCount: Int,
    val distanceMeters: Double,
    val movingTimeSeconds: Int,
    val elevationGainMeters: Double,
)
