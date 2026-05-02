package com.krist.train.ui.screen.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.ui.component.ErrorMessage
import com.krist.train.ui.component.LoadingState

@Composable
fun GoalSetupScreen(viewModel: GoalSetupViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Goal", style = MaterialTheme.typography.headlineMedium)
        GoalTextField("Goal name", state.name) { value -> viewModel.update { it.copy(name = value) } }
        GoalTextField("Sport", state.sportType) { value -> viewModel.update { it.copy(sportType = value) } }
        GoalTextField("Target distance km", state.targetDistanceKm) { value -> viewModel.update { it.copy(targetDistanceKm = value) } }
        GoalTextField("Target time, e.g. 1:35 or 95", state.targetTime) { value -> viewModel.update { it.copy(targetTime = value) } }
        GoalTextField("Race date, e.g. 2026-09-13", state.targetDate) { value -> viewModel.update { it.copy(targetDate = value) } }
        GoalTextField("Training days per week", state.availableDaysPerWeek) { value -> viewModel.update { it.copy(availableDaysPerWeek = value) } }
        GoalTextField("Long workout day", state.preferredLongWorkoutDay) { value -> viewModel.update { it.copy(preferredLongWorkoutDay = value) } }
        Button(onClick = viewModel::cycleRiskPreference) { Text("Risk preference: ${state.riskPreference}") }
        GoalTextField("Notes", state.notes) { value -> viewModel.update { it.copy(notes = value) } }
        Button(onClick = viewModel::generatePlan, enabled = !state.isGenerating) { Text("Generate JSON plan") }
        if (state.isGenerating) LoadingState("Generating plan...")
        state.message?.let { Text(it) }
        state.error?.let { ErrorMessage(it) }
    }
}

@Composable
private fun GoalTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}
