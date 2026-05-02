package com.krist.train.domain.model

data class Activity(
    val id: Long,
    val name: String,
    val sportType: String,
    val startDateEpochMillis: Long,
    val distanceMeters: Double,
    val movingTimeSeconds: Int,
    val elapsedTimeSeconds: Int,
    val totalElevationGainMeters: Double,
    val averageHeartRate: Double?,
    val maxHeartRate: Double?,
    val relativeEffort: Double?,
)
