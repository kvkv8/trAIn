package com.krist.train.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krist.train.data.local.activity.ActivityDao
import com.krist.train.data.local.activity.ActivityEntity
import com.krist.train.data.local.goal.GoalDao
import com.krist.train.data.local.goal.GoalEntity
import com.krist.train.data.local.plan.TrainingPlanDao
import com.krist.train.data.local.plan.TrainingPlanEntity

@Database(
    entities = [ActivityEntity::class, GoalEntity::class, TrainingPlanEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun goalDao(): GoalDao
    abstract fun trainingPlanDao(): TrainingPlanDao
}
