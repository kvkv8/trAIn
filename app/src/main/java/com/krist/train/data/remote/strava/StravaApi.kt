package com.krist.train.data.remote.strava

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StravaApi {
    @GET("api/v3/athlete/activities")
    suspend fun getAthleteActivities(
        @Header("Authorization") authorization: String,
        @Query("after") afterEpochSeconds: Long? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
    ): List<StravaActivityDto>
}
