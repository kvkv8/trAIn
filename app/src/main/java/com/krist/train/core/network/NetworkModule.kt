package com.krist.train.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.krist.train.core.config.ApiConfig
import com.krist.train.data.remote.ai.GeminiApi
import com.krist.train.data.remote.strava.StravaApi
import com.krist.train.data.remote.strava.StravaAuthApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class NetworkModule {
    val json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    private val converterFactory = json.asConverterFactory("application/json".toMediaType())

    private val stravaRetrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.STRAVA_BASE_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    private val geminiRetrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.GEMINI_BASE_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    val stravaApi: StravaApi = stravaRetrofit.create(StravaApi::class.java)
    val stravaAuthApi: StravaAuthApi = stravaRetrofit.create(StravaAuthApi::class.java)
    val geminiApi: GeminiApi = geminiRetrofit.create(GeminiApi::class.java)
}
