package com.krist.train.ui.screen.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krist.train.AppContainer
import com.krist.train.domain.model.Goal
import com.krist.train.domain.model.GoalTimeUtils
import com.krist.train.domain.model.RiskPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GoalSetupUiState(
    val name: String = "Half marathon build",
    val sportType: String = "Run",
    val targetDistanceKm: String = "21.1",
    val targetTime: String = "",
    val targetDate: String = "",
    val availableDaysPerWeek: String = "4",
    val preferredLongWorkoutDay: String = "Sunday",
    val riskPreference: RiskPreference = RiskPreference.Aggressive,
    val notes: String = "",
    val isGenerating: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class GoalSetupViewModel(
    private val container: AppContainer,
) : ViewModel() {
    private val _state = MutableStateFlow(GoalSetupUiState())
    val state: StateFlow<GoalSetupUiState> = _state.asStateFlow()

    fun update(transform: (GoalSetupUiState) -> GoalSetupUiState) {
        _state.value = transform(_state.value)
    }

    fun cycleRiskPreference() {
        update { current ->
            val next = when (current.riskPreference) {
                RiskPreference.Conservative -> RiskPreference.Balanced
                RiskPreference.Balanced -> RiskPreference.Aggressive
                RiskPreference.Aggressive -> RiskPreference.Conservative
            }
            current.copy(riskPreference = next)
        }
    }

    fun generatePlan() {
        viewModelScope.launch {
            val current = _state.value
            _state.value = current.copy(isGenerating = true, message = null, error = null)
            runCatching {
                container.generateTrainingPlan(current.toGoal())
            }.onSuccess {
                _state.value = _state.value.copy(isGenerating = false, message = "Plan generated", error = null)
            }.onFailure { error ->
                _state.value = _state.value.copy(isGenerating = false, error = error.message ?: "Plan generation failed")
            }
        }
    }

    private fun GoalSetupUiState.toGoal(): Goal = Goal(
        name = name.ifBlank { "Training goal" },
        sportType = sportType.ifBlank { "Run" },
        targetDateEpochMillis = GoalTimeUtils.parseRaceDateEpochMillis(targetDate),
        targetDistanceMeters = targetDistanceKm.toDoubleOrNull()?.times(1000.0),
        targetTimeSeconds = GoalTimeUtils.parseTargetTimeSeconds(targetTime),
        availableDaysPerWeek = availableDaysPerWeek.toIntOrNull()?.coerceIn(1, 7) ?: 4,
        preferredLongWorkoutDay = preferredLongWorkoutDay.ifBlank { "Sunday" },
        riskPreference = riskPreference,
        notes = notes,
    )
}
