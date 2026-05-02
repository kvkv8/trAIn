package com.krist.train.data.repository

import android.net.Uri
import com.krist.train.core.config.ApiConfig
import com.krist.train.core.security.SecureSettingsStore
import com.krist.train.core.security.SecureTokenStore
import com.krist.train.data.remote.strava.StravaAuthApi
import com.krist.train.data.remote.strava.StravaTokenResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(
    private val authApi: StravaAuthApi,
    private val tokenStore: SecureTokenStore,
    private val settingsStore: SecureSettingsStore,
) {
    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val authStatus: StateFlow<AuthStatus> = _authStatus.asStateFlow()

    val hasStravaToken: Boolean
        get() = tokenStore.stravaAccessToken != null && tokenStore.stravaRefreshToken != null

    fun buildStravaAuthorizationUrl(): String {
        val clientId = settingsStore.stravaClientId.orEmpty()
        require(clientId.isNotBlank()) { "Set Strava client ID in Settings first" }

        _authStatus.value = AuthStatus.Idle

        return Uri.parse("https://www.strava.com/oauth/mobile/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", ApiConfig.STRAVA_REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "force")
            .appendQueryParameter("scope", "read,activity:read_all")
            .build()
            .toString()
    }

    suspend fun exchangeCode(code: String) {
        _authStatus.value = AuthStatus.Exchanging
        runCatching {
            val clientId = settingsStore.stravaClientId.orEmpty()
            val clientSecret = settingsStore.stravaClientSecret.orEmpty()
            require(clientId.isNotBlank() && clientSecret.isNotBlank()) {
                "Set Strava client ID and secret in Settings first"
            }

            authApi.exchangeCode(clientId = clientId, clientSecret = clientSecret, code = code)
        }.onSuccess { response ->
            saveTokens(response)
            _authStatus.value = AuthStatus.Authorized
        }.onFailure { error ->
            _authStatus.value = AuthStatus.Failed(error.message ?: "Strava authorization failed")
        }.getOrThrow()
    }

    fun reportAuthorizationDenied(message: String) {
        _authStatus.value = AuthStatus.Failed(message)
    }

    suspend fun validAccessToken(): String {
        val accessToken = tokenStore.stravaAccessToken
        val refreshToken = tokenStore.stravaRefreshToken
        require(!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) { "Connect Strava first" }

        val expiresSoon = tokenStore.stravaTokenExpiresAtEpochSeconds <= (System.currentTimeMillis() / 1000L) + 60L
        if (!expiresSoon) return accessToken

        val clientId = settingsStore.stravaClientId.orEmpty()
        val clientSecret = settingsStore.stravaClientSecret.orEmpty()
        require(clientId.isNotBlank() && clientSecret.isNotBlank()) {
            "Set Strava client ID and secret in Settings first"
        }

        val refreshed = authApi.refreshToken(
            clientId = clientId,
            clientSecret = clientSecret,
            refreshToken = refreshToken,
        )
        saveTokens(refreshed)
        return refreshed.accessToken
    }

    fun clearStravaTokens() = tokenStore.clearStravaTokens()

    private fun saveTokens(response: StravaTokenResponseDto) {
        tokenStore.stravaAccessToken = response.accessToken
        tokenStore.stravaRefreshToken = response.refreshToken
        tokenStore.stravaTokenExpiresAtEpochSeconds = response.expiresAtEpochSeconds
    }
}

sealed interface AuthStatus {
    data object Idle : AuthStatus
    data object Exchanging : AuthStatus
    data object Authorized : AuthStatus
    data class Failed(val message: String) : AuthStatus
}
