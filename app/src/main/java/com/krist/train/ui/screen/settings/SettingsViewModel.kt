package com.krist.train.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krist.train.AppContainer
import com.krist.train.data.remote.ai.AiModel
import com.krist.train.data.repository.AuthStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val stravaClientId: String = "",
    val stravaClientSecret: String = "",
    val geminiApiKey: String = "",
    val geminiModel: String = "",
    val availableGeminiModels: List<AiModel> = emptyList(),
    val isLoadingGeminiModels: Boolean = false,
    val isTestingGemini: Boolean = false,
    val isSyncing: Boolean = false,
    val hasStravaToken: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class SettingsViewModel(
    private val container: AppContainer,
) : ViewModel() {
    private val _state = MutableStateFlow(
        SettingsUiState(
            stravaClientId = container.settingsStore.stravaClientId.orEmpty(),
            stravaClientSecret = container.settingsStore.stravaClientSecret.orEmpty(),
            geminiApiKey = container.settingsStore.geminiApiKey.orEmpty(),
            geminiModel = container.settingsStore.geminiModel.orEmpty(),
            hasStravaToken = container.authRepository.hasStravaToken,
        ),
    )
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            container.authRepository.authStatus.collect { status ->
                _state.value = when (status) {
                    AuthStatus.Idle -> _state.value.copy(hasStravaToken = container.authRepository.hasStravaToken)
                    AuthStatus.Exchanging -> _state.value.copy(
                        message = "Completing Strava authorization...",
                        error = null,
                    )
                    AuthStatus.Authorized -> _state.value.copy(
                        hasStravaToken = true,
                        message = "Strava authorized. You can sync activities now.",
                        error = null,
                    )
                    is AuthStatus.Failed -> _state.value.copy(
                        hasStravaToken = container.authRepository.hasStravaToken,
                        error = status.message,
                    )
                }
            }
        }
    }

    fun update(transform: (SettingsUiState) -> SettingsUiState) {
        _state.value = transform(_state.value).copy(message = null, error = null)
    }

    fun save() {
        val current = _state.value
        container.settingsStore.stravaClientId = current.stravaClientId
        container.settingsStore.stravaClientSecret = current.stravaClientSecret
        container.settingsStore.geminiApiKey = current.geminiApiKey
        container.settingsStore.geminiModel = current.geminiModel
        _state.value = current.copy(message = "Settings saved", error = null)
    }

    fun selectGeminiModel(model: AiModel) {
        update { it.copy(geminiModel = model.name) }
        save()
    }

    fun loadGeminiModels() {
        viewModelScope.launch {
            save()
            val apiKey = _state.value.geminiApiKey
            if (apiKey.isBlank()) {
                _state.value = _state.value.copy(error = "Set Google AI API key first")
                return@launch
            }

            _state.value = _state.value.copy(isLoadingGeminiModels = true, message = null, error = null)
            runCatching { container.geminiAiProvider.listAvailableModels(apiKey) }
                .onSuccess { models ->
                    _state.value = _state.value.copy(
                        availableGeminiModels = models,
                        isLoadingGeminiModels = false,
                        message = if (models.isEmpty()) {
                            "No Google AI models with generateContent access were returned for this key"
                        } else {
                            "Loaded ${models.size} Google AI models. Select one before generating a plan."
                        },
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoadingGeminiModels = false,
                        error = error.message ?: "Could not load Google AI models",
                    )
                }
        }
    }

    fun testGeminiModel() {
        viewModelScope.launch {
            save()
            val current = _state.value
            if (current.geminiApiKey.isBlank()) {
                _state.value = current.copy(error = "Set Google AI API key first")
                return@launch
            }
            if (current.geminiModel.isBlank()) {
                _state.value = current.copy(error = "Select or enter a Google AI model first")
                return@launch
            }

            _state.value = current.copy(isTestingGemini = true, message = null, error = null)
            runCatching {
                container.geminiAiProvider.testConnection(
                    apiKey = current.geminiApiKey,
                    model = current.geminiModel,
                )
            }.onSuccess {
                _state.value = _state.value.copy(
                    isTestingGemini = false,
                    message = "Google AI model works: ${current.geminiModel}",
                )
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isTestingGemini = false,
                    error = error.message ?: "Google AI model test failed",
                )
            }
        }
    }

    fun buildAuthorizationUrl(): String {
        save()
        _state.value = _state.value.copy(message = "Opening Strava authorization...", error = null)
        return container.authRepository.buildStravaAuthorizationUrl()
    }

    fun reportAuthorizationError(error: Throwable) {
        _state.value = _state.value.copy(
            message = null,
            error = error.message ?: "Could not open Strava authorization",
        )
    }

    fun syncStrava() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSyncing = true, message = null, error = null)
            runCatching { container.syncStravaActivities() }
                .onSuccess { count ->
                    _state.value = _state.value.copy(
                        isSyncing = false,
                        hasStravaToken = container.authRepository.hasStravaToken,
                        message = "Synced $count activities",
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isSyncing = false,
                        error = error.message ?: "Strava sync failed",
                    )
                }
        }
    }
}
