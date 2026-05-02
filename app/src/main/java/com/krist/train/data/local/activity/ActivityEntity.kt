package com.krist.train.data.local.activity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krist.train.domain.model.Activity

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val id: Long,
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
    val syncedAtEpochMillis: Long,
)

fun ActivityEntity.toDomain(): Activity = Activity(
    id = id,
    name = name,
    sportType = sportType,
    startDateEpochMillis = startDateEpochMillis,
    distanceMeters = distanceMeters,
    movingTimeSeconds = movingTimeSeconds,
    elapsedTimeSeconds = elapsedTimeSeconds,
    totalElevationGainMeters = totalElevationGainMeters,
    averageHeartRate = averageHeartRate,
    maxHeartRate = maxHeartRate,
    relativeEffort = relativeEffort,
)

fun Activity.toEntity(syncedAtEpochMillis: Long): ActivityEntity = ActivityEntity(
    id = id,
    name = name,
    sportType = sportType,
    startDateEpochMillis = startDateEpochMillis,
    distanceMeters = distanceMeters,
    movingTimeSeconds = movingTimeSeconds,
    elapsedTimeSeconds = elapsedTimeSeconds,
    totalElevationGainMeters = totalElevationGainMeters,
    averageHeartRate = averageHeartRate,
    maxHeartRate = maxHeartRate,
    relativeEffort = relativeEffort,
    syncedAtEpochMillis = syncedAtEpochMillis,
)
