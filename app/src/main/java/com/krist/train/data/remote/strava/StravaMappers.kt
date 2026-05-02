package com.krist.train.data.remote.strava

import com.krist.train.domain.model.Activity
import java.time.Instant

fun StravaActivityDto.toDomain(): Activity = Activity(
    id = id,
    name = name,
    sportType = sportType ?: type ?: "Unknown",
    startDateEpochMillis = Instant.parse(startDate).toEpochMilli(),
    distanceMeters = distance,
    movingTimeSeconds = movingTime,
    elapsedTimeSeconds = elapsedTime,
    totalElevationGainMeters = totalElevationGain,
    averageHeartRate = averageHeartRate,
    maxHeartRate = maxHeartRate,
    relativeEffort = sufferScore?.toDouble(),
)
