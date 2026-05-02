package com.krist.train.sync

data class SyncStatus(
    val isRunning: Boolean = false,
    val lastSyncedAtEpochMillis: Long? = null,
    val lastError: String? = null,
)
