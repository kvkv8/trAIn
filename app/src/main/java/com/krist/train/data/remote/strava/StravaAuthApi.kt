package com.krist.train.data.remote.strava

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface StravaAuthApi {
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun exchangeCode(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code",
    ): StravaTokenResponseDto

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
    ): StravaTokenResponseDto
}
