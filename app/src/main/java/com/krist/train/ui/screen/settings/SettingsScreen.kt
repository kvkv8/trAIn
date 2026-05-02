package com.krist.train.ui.screen.settings

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.browser.customtabs.CustomTabsIntent
import com.krist.train.core.config.ApiConfig
import com.krist.train.ui.component.ErrorMessage
import com.krist.train.ui.component.LoadingState

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Strava", style = MaterialTheme.typography.titleLarge)
                Text("Store your app credentials, authorize Strava, then sync new activities from here.")
                Text("Callback domain: localhost", style = MaterialTheme.typography.bodySmall)
                Text("Redirect URI: ${ApiConfig.STRAVA_REDIRECT_URI}", style = MaterialTheme.typography.bodySmall)
                SettingsTextField("Strava client ID", state.stravaClientId) { value -> viewModel.update { it.copy(stravaClientId = value) } }
                SettingsTextField("Strava client secret", state.stravaClientSecret) { value -> viewModel.update { it.copy(stravaClientSecret = value) } }
                Text("Connection: ${if (state.hasStravaToken) "Authorized" else "Not authorized"}")
                Button(
                    onClick = {
                        runCatching {
                            CustomTabsIntent.Builder()
                                .setShowTitle(true)
                                .build()
                                .launchUrl(context, Uri.parse(viewModel.buildAuthorizationUrl()))
                        }
                            .onFailure(viewModel::reportAuthorizationError)
                    },
                ) { Text("Authorize Strava") }
                Button(onClick = viewModel::syncStrava, enabled = !state.isSyncing) { Text("Sync Strava activities") }
                if (state.isSyncing) LoadingState("Syncing Strava activities...")
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("AI provider", style = MaterialTheme.typography.titleLarge)
                Text("Google AI is the first provider wired up. Load the models available to your API key, choose one, then test it before generating plans.")
                SettingsTextField("Google AI API key", state.geminiApiKey) { value -> viewModel.update { it.copy(geminiApiKey = value) } }
                SettingsTextField("Google AI model", state.geminiModel) { value -> viewModel.update { it.copy(geminiModel = value) } }
                Button(
                    onClick = viewModel::loadGeminiModels,
                    enabled = !state.isLoadingGeminiModels,
                ) { Text("Load available models") }
                if (state.isLoadingGeminiModels) LoadingState("Loading Google AI models...")
                state.availableGeminiModels.forEach { model ->
                    TextButton(onClick = { viewModel.selectGeminiModel(model) }) {
                        Text("Use ${model.displayName} (${model.name})")
                    }
                }
                Button(
                    onClick = viewModel::testGeminiModel,
                    enabled = !state.isTestingGemini,
                ) { Text("Test selected model") }
                if (state.isTestingGemini) LoadingState("Testing Google AI model...")
            }
        }

        Button(onClick = viewModel::save) { Text("Save settings") }
        state.message?.let { Text(it) }
        state.error?.let { ErrorMessage(it) }
    }
}

@Composable
private fun SettingsTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}
