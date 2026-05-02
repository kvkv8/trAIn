package com.krist.train.data.local.activity

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY startDateEpochMillis DESC")
    fun observeActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities ORDER BY startDateEpochMillis DESC")
    suspend fun getActivities(): List<ActivityEntity>

    @Query("SELECT MAX(startDateEpochMillis) FROM activities")
    suspend fun latestActivityStartMillis(): Long?

    @Upsert
    suspend fun upsertAll(activities: List<ActivityEntity>)
}
