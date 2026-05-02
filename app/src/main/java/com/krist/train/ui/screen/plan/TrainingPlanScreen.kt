package com.krist.train.ui.screen.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.ui.component.WorkoutCard

@Composable
fun TrainingPlanScreen(viewModel: TrainingPlanViewModel) {
    val plan by viewModel.plan.collectAsState(initial = null)
    val refreshState by viewModel.refreshState.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { Text("Training plan", style = MaterialTheme.typography.headlineMedium) }
        item { Button(onClick = viewModel::refreshPlan) { Text("Regenerate latest goal") } }
        refreshState?.let { item { Text(it) } }
        if (plan == null) {
            item { Text("No plan generated yet.") }
        } else {
            item { Text(plan!!.overview) }
            if (plan!!.assumptions.isNotEmpty()) {
                item { Text("Assumptions", style = MaterialTheme.typography.titleMedium) }
                items(plan!!.assumptions) { Text("- $it") }
            }
            items(plan!!.weeks, key = { it.week }) { week ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Week ${week.week}: ${week.focus}", style = MaterialTheme.typography.titleLarge)
                    week.workouts.forEach { workout -> WorkoutCard(workout) }
                }
            }
            if (plan!!.recoveryGuidance.isNotEmpty()) {
                item { Text("Recovery", style = MaterialTheme.typography.titleMedium) }
                items(plan!!.recoveryGuidance) { Text("- $it") }
            }
            if (plan!!.warningSigns.isNotEmpty()) {
                item { Text("Warning signs", style = MaterialTheme.typography.titleMedium) }
                items(plan!!.warningSigns) { Text("- $it") }
            }
        }
    }
}
