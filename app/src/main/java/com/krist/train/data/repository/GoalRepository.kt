package com.krist.train.data.repository

import com.krist.train.data.local.goal.GoalDao
import com.krist.train.data.local.goal.toDomain
import com.krist.train.data.local.goal.toEntity
import com.krist.train.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepository(
    private val goalDao: GoalDao,
) {
    fun observeLatestGoal(): Flow<Goal?> = goalDao.observeLatestGoal().map { it?.toDomain() }

    suspend fun latestGoal(): Goal? = goalDao.latestGoal()?.toDomain()

    suspend fun saveGoal(goal: Goal): Long = goalDao.insert(goal.toEntity(System.currentTimeMillis()))
}
