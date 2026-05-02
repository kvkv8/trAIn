package com.krist.train.data.repository

import com.krist.train.data.local.activity.ActivityDao
import com.krist.train.data.local.activity.toDomain
import com.krist.train.data.local.activity.toEntity
import com.krist.train.domain.model.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRepository(
    private val activityDao: ActivityDao,
) {
    fun observeActivities(): Flow<List<Activity>> = activityDao.observeActivities()
        .map { entities -> entities.map { it.toDomain() } }

    suspend fun getActivities(): List<Activity> = activityDao.getActivities().map { it.toDomain() }

    suspend fun latestActivityStartMillis(): Long? = activityDao.latestActivityStartMillis()

    suspend fun upsertActivities(activities: List<Activity>) {
        val syncedAt = System.currentTimeMillis()
        activityDao.upsertAll(activities.map { it.toEntity(syncedAt) })
    }
}
