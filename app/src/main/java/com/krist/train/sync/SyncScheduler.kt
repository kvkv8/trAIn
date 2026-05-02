package com.krist.train.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SyncScheduler(
    private val context: Context,
) {
    fun scheduleDailyStravaSync() {
        val request = PeriodicWorkRequestBuilder<StravaSyncWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    fun cancelDailyStravaSync() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    private companion object {
        const val WORK_NAME = "strava_daily_sync"
    }
}
