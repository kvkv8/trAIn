package com.krist.train.ui.screen.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krist.train.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainingPlanViewModel(
    private val container: AppContainer,
) : ViewModel() {
    val plan = container.trainingPlanRepository.observeLatestPlan()

    private val _refreshState = MutableStateFlow<String?>(null)
    val refreshState: StateFlow<String?> = _refreshState.asStateFlow()

    fun refreshPlan() {
        viewModelScope.launch {
            _refreshState.value = "Refreshing plan..."
            runCatching { container.refreshPlan() }
                .onSuccess { _refreshState.value = "Plan refreshed" }
                .onFailure { _refreshState.value = it.message ?: "Refresh failed" }
        }
    }
}
