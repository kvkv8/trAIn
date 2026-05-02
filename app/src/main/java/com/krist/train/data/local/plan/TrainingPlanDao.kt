package com.krist.train.data.local.plan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingPlanDao {
    @Query("SELECT * FROM training_plans ORDER BY createdAtEpochMillis DESC LIMIT 1")
    fun observeLatestPlan(): Flow<TrainingPlanEntity?>

    @Query("SELECT * FROM training_plans ORDER BY createdAtEpochMillis DESC LIMIT 1")
    suspend fun latestPlan(): TrainingPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: TrainingPlanEntity): Long
}
