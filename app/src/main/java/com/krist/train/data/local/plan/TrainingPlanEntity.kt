package com.krist.train.data.local.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_plans")
data class TrainingPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val rawJson: String,
    val createdAtEpochMillis: Long,
)
