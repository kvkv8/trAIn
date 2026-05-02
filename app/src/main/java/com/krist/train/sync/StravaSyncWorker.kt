package com.krist.train.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.krist.train.TrAInApp

class StravaSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = try {
        val app = applicationContext as TrAInApp
        app.container.syncStravaActivities()
        Result.success()
    } catch (error: Throwable) {
        Result.retry()
    }
}
