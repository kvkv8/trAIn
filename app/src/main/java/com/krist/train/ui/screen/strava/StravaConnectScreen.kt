package com.krist.train.ui.screen.strava

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.krist.train.ui.component.ErrorMessage
import com.krist.train.ui.component.LoadingState

@Composable
fun StravaConnectScreen(viewModel: StravaConnectViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Strava", style = MaterialTheme.typography.headlineMedium)
        Text("Add your Strava client ID and secret in Settings, authorize the app, then sync activities.")
        Button(
            onClick = {
                runCatching { viewModel.buildAuthorizationUrl() }
                    .onSuccess { url -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
            },
        ) { Text("Authorize Strava") }
        Button(onClick = viewModel::sync, enabled = !state.isSyncing) { Text("Sync activities") }
        if (state.isSyncing) LoadingState("Syncing Strava activities...")
        state.message?.let { Text(it) }
        state.error?.let { ErrorMessage(it) }
    }
}
