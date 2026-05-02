package com.krist.train.data.repository

import com.krist.train.data.local.plan.TrainingPlanDao
import com.krist.train.data.local.plan.TrainingPlanEntity
import com.krist.train.domain.model.TrainingPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class TrainingPlanRepository(
    private val trainingPlanDao: TrainingPlanDao,
    private val json: Json,
) {
    fun observeLatestPlan(): Flow<TrainingPlan?> = trainingPlanDao.observeLatestPlan().map { entity ->
        entity?.let { json.decodeFromString<TrainingPlan>(it.rawJson) }
    }

    suspend fun latestPlan(): TrainingPlan? = trainingPlanDao.latestPlan()
        ?.let { json.decodeFromString<TrainingPlan>(it.rawJson) }

    suspend fun savePlan(goalId: Long, rawJson: String): Long = trainingPlanDao.insert(
        TrainingPlanEntity(
            goalId = goalId,
            rawJson = rawJson,
            createdAtEpochMillis = System.currentTimeMillis(),
        ),
    )
}
