package com.krist.train.data.remote.strava

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaTokenResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_at") val expiresAtEpochSeconds: Long,
)

@Serializable
data class StravaActivityDto(
    val id: Long,
    val name: String = "Untitled activity",
    @SerialName("sport_type") val sportType: String? = null,
    val type: String? = null,
    @SerialName("start_date") val startDate: String,
    val distance: Double = 0.0,
    @SerialName("moving_time") val movingTime: Int = 0,
    @SerialName("elapsed_time") val elapsedTime: Int = 0,
    @SerialName("total_elevation_gain") val totalElevationGain: Double = 0.0,
    @SerialName("average_heartrate") val averageHeartRate: Double? = null,
    @SerialName("max_heartrate") val maxHeartRate: Double? = null,
    @SerialName("suffer_score") val sufferScore: Int? = null,
)
