package com.krist.train.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureSettingsStore(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_settings",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    var stravaClientId: String?
        get() = prefs.getString(KEY_STRAVA_CLIENT_ID, null)
        set(value) = prefs.edit().putNullableString(KEY_STRAVA_CLIENT_ID, value).apply()

    var stravaClientSecret: String?
        get() = prefs.getString(KEY_STRAVA_CLIENT_SECRET, null)
        set(value) = prefs.edit().putNullableString(KEY_STRAVA_CLIENT_SECRET, value).apply()

    var geminiApiKey: String?
        get() = prefs.getString(KEY_GEMINI_API_KEY, null)
        set(value) = prefs.edit().putNullableString(KEY_GEMINI_API_KEY, value).apply()

    var geminiModel: String?
        get() = prefs.getString(KEY_GEMINI_MODEL, null)
        set(value) = prefs.edit().putNullableString(KEY_GEMINI_MODEL, value).apply()

    private fun android.content.SharedPreferences.Editor.putNullableString(key: String, value: String?) = apply {
        if (value.isNullOrBlank()) remove(key) else putString(key, value)
    }

    private companion object {
        const val KEY_STRAVA_CLIENT_ID = "strava_client_id"
        const val KEY_STRAVA_CLIENT_SECRET = "strava_client_secret"
        const val KEY_GEMINI_API_KEY = "gemini_api_key"
        const val KEY_GEMINI_MODEL = "gemini_model"
    }
}
