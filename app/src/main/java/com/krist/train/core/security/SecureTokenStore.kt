package com.krist.train.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureTokenStore(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_tokens",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    var stravaAccessToken: String?
        get() = prefs.getString(KEY_STRAVA_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putNullableString(KEY_STRAVA_ACCESS_TOKEN, value).apply()

    var stravaRefreshToken: String?
        get() = prefs.getString(KEY_STRAVA_REFRESH_TOKEN, null)
        set(value) = prefs.edit().putNullableString(KEY_STRAVA_REFRESH_TOKEN, value).apply()

    var stravaTokenExpiresAtEpochSeconds: Long
        get() = prefs.getLong(KEY_STRAVA_EXPIRES_AT, 0L)
        set(value) = prefs.edit().putLong(KEY_STRAVA_EXPIRES_AT, value).apply()

    fun clearStravaTokens() {
        prefs.edit()
            .remove(KEY_STRAVA_ACCESS_TOKEN)
            .remove(KEY_STRAVA_REFRESH_TOKEN)
            .remove(KEY_STRAVA_EXPIRES_AT)
            .apply()
    }

    private fun android.content.SharedPreferences.Editor.putNullableString(key: String, value: String?) = apply {
        if (value == null) remove(key) else putString(key, value)
    }

    private companion object {
        const val KEY_STRAVA_ACCESS_TOKEN = "strava_access_token"
        const val KEY_STRAVA_REFRESH_TOKEN = "strava_refresh_token"
        const val KEY_STRAVA_EXPIRES_AT = "strava_expires_at"
    }
}
