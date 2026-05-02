package com.krist.train.domain.usecase

import com.krist.train.data.remote.strava.StravaApi
import com.krist.train.data.remote.strava.toDomain
import com.krist.train.data.repository.ActivityRepository
import com.krist.train.data.repository.AuthRepository
import kotlin.math.max

class SyncStravaActivitiesUseCase(
    private val authRepository: AuthRepository,
    private val stravaApi: StravaApi,
    private val activityRepository: ActivityRepository,
) {
    suspend operator fun invoke(): Int {
        val accessToken = authRepository.validAccessToken()
        val latestMillis = activityRepository.latestActivityStartMillis()
        val afterEpochSeconds = latestMillis?.let { max(0L, (it / 1000L) - SEVEN_DAYS_SECONDS) }
        val synced = mutableListOf<com.krist.train.domain.model.Activity>()

        var page = 1
        do {
            val batch = stravaApi.getAthleteActivities(
                authorization = "Bearer $accessToken",
                afterEpochSeconds = afterEpochSeconds,
                page = page,
                perPage = PAGE_SIZE,
            ).map { it.toDomain() }
            synced += batch
            page += 1
        } while (batch.size == PAGE_SIZE)

        activityRepository.upsertActivities(synced)
        return synced.size
    }

    private companion object {
        const val PAGE_SIZE = 100
        const val SEVEN_DAYS_SECONDS = 7L * 24L * 60L * 60L
    }
}
