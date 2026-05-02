package com.krist.train.data.local.goal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAtEpochMillis DESC LIMIT 1")
    fun observeLatestGoal(): Flow<GoalEntity?>

    @Query("SELECT * FROM goals ORDER BY createdAtEpochMillis DESC LIMIT 1")
    suspend fun latestGoal(): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity): Long
}
