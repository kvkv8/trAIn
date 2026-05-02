package com.krist.train

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.krist.train.ui.navigation.AppNavHost
import com.krist.train.ui.theme.TrAInTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleStravaCallback()

        setContent {
            TrAInTheme {
                AppNavHost(container = (application as TrAInApp).container)
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleStravaCallback()
    }

    private fun handleStravaCallback() {
        val callbackUri = intent?.data ?: return
        val error = callbackUri.getQueryParameter("error")
        if (error != null) {
            (application as TrAInApp).container.authRepository.reportAuthorizationDenied("Strava authorization denied: $error")
            setIntent(Intent())
            return
        }

        val code = callbackUri.getQueryParameter("code") ?: return
        lifecycleScope.launch {
            runCatching { (application as TrAInApp).container.authRepository.exchangeCode(code) }
            setIntent(Intent())
        }
    }
}
