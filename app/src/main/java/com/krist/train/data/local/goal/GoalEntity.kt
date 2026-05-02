package com.krist.train.data.local.goal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krist.train.domain.model.Goal
import com.krist.train.domain.model.RiskPreference

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sportType: String,
    val targetDateEpochMillis: Long?,
    val targetDistanceMeters: Double?,
    val targetTimeSeconds: Int?,
    val availableDaysPerWeek: Int,
    val preferredLongWorkoutDay: String,
    val riskPreference: String,
    val notes: String,
    val createdAtEpochMillis: Long,
)

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    name = name,
    sportType = sportType,
    targetDateEpochMillis = targetDateEpochMillis,
    targetDistanceMeters = targetDistanceMeters,
    targetTimeSeconds = targetTimeSeconds,
    availableDaysPerWeek = availableDaysPerWeek,
    preferredLongWorkoutDay = preferredLongWorkoutDay,
    riskPreference = runCatching { RiskPreference.valueOf(riskPreference) }.getOrDefault(RiskPreference.Balanced),
    notes = notes,
)

fun Goal.toEntity(createdAtEpochMillis: Long): GoalEntity = GoalEntity(
    id = id,
    name = name,
    sportType = sportType,
    targetDateEpochMillis = targetDateEpochMillis,
    targetDistanceMeters = targetDistanceMeters,
    targetTimeSeconds = targetTimeSeconds,
    availableDaysPerWeek = availableDaysPerWeek,
    preferredLongWorkoutDay = preferredLongWorkoutDay,
    riskPreference = riskPreference.name,
    notes = notes,
    createdAtEpochMillis = createdAtEpochMillis,
)
