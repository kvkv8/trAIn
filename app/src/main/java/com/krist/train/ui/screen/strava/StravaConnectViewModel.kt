package com.krist.train.ui.screen.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krist.train.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StravaConnectUiState(
    val isSyncing: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class StravaConnectViewModel(
    private val container: AppContainer,
) : ViewModel() {
    private val _state = MutableStateFlow(StravaConnectUiState())
    val state: StateFlow<StravaConnectUiState> = _state.asStateFlow()

    fun buildAuthorizationUrl(): String = container.authRepository.buildStravaAuthorizationUrl()

    fun sync() {
        viewModelScope.launch {
            _state.value = StravaConnectUiState(isSyncing = true)
            runCatching { container.syncStravaActivities() }
                .onSuccess { count -> _state.value = StravaConnectUiState(message = "Synced $count activities") }
                .onFailure { error -> _state.value = StravaConnectUiState(error = error.message ?: "Sync failed") }
        }
    }
}
