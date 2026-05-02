package com.krist.train.ui.screen.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.core.util.metersToKmString
import com.krist.train.core.util.secondsToHoursMinutes
import com.krist.train.domain.analysis.TrainingSummary
import com.krist.train.domain.model.GoalTimeUtils
import com.krist.train.ui.component.ErrorMessage
import com.krist.train.ui.component.LoadingState
import com.krist.train.ui.component.WeeklySummaryChart
import com.krist.train.ui.screen.insights.InsightsViewModel

@Composable
fun CoachScreen(
    insightsViewModel: InsightsViewModel,
    goalViewModel: GoalSetupViewModel,
) {
    val summary by insightsViewModel.summary.collectAsState(initial = emptySummary())
    val goalState by goalViewModel.state.collectAsState()
    val raceDateMillis = GoalTimeUtils.parseRaceDateEpochMillis(goalState.targetDate)
    val raceCountdown = GoalTimeUtils.raceCountdown(raceDateMillis)?.display()

    LazyColumn(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Coach", style = MaterialTheme.typography.headlineMedium)
                Text("Review your recent load, then ask for a plan that fits the data.")
            }
        }

        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Training snapshot", style = MaterialTheme.typography.titleLarge)
                    Text("Last 8 weeks: ${summary.lastEightWeeksDistanceMeters.metersToKmString()} km")
                    Text("Last 4 weeks: ${summary.lastFourWeeksDistanceMeters.metersToKmString()} km")
                    Text("Average week: ${summary.averageWeeklyDistanceMeters.metersToKmString()} km · ${summary.averageWeeklyMovingTimeSeconds.secondsToHoursMinutes()}")
                    Text("Longest recent activity: ${summary.longestActivityMeters.metersToKmString()} km")
                    Text("Estimated threshold pace: ${summary.estimatedThresholdPaceSecondsPerKm?.toPaceString() ?: "unknown"}")
                    WeeklySummaryChart(summary.weeklySummaries)
                }
            }
        }

        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Goal setup", style = MaterialTheme.typography.titleLarge)
                    CoachTextField("Goal name", goalState.name) { value -> goalViewModel.update { it.copy(name = value) } }
                    CoachTextField("Sport", goalState.sportType) { value -> goalViewModel.update { it.copy(sportType = value) } }
                    CoachTextField("Target distance km", goalState.targetDistanceKm) { value -> goalViewModel.update { it.copy(targetDistanceKm = value) } }
                    CoachTextField("Target time, e.g. 1:35 or 95", goalState.targetTime) { value -> goalViewModel.update { it.copy(targetTime = value) } }
                    CoachTextField("Race date, e.g. 2026-09-13", goalState.targetDate) { value -> goalViewModel.update { it.copy(targetDate = value) } }
                    raceCountdown?.let { Text("Time until race: $it") }
                    CoachTextField("Training days per week", goalState.availableDaysPerWeek) { value -> goalViewModel.update { it.copy(availableDaysPerWeek = value) } }
                    CoachTextField("Long workout day", goalState.preferredLongWorkoutDay) { value -> goalViewModel.update { it.copy(preferredLongWorkoutDay = value) } }
                    Button(onClick = goalViewModel::cycleRiskPreference) { Text("Risk preference: ${goalState.riskPreference}") }
                    CoachTextField("Notes", goalState.notes) { value -> goalViewModel.update { it.copy(notes = value) } }
                    Button(onClick = goalViewModel::generatePlan, enabled = !goalState.isGenerating) { Text("Generate JSON plan") }
                    if (goalState.isGenerating) LoadingState("Generating plan...")
                    goalState.message?.let { Text(it) }
                    goalState.error?.let { ErrorMessage(it) }
                }
            }
        }
    }
}

@Composable
private fun CoachTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun emptySummary() = TrainingSummary(
    activityCount = 0,
    sportTypes = emptyList(),
    lastFourWeeksDistanceMeters = 0.0,
    lastEightWeeksDistanceMeters = 0.0,
    averageWeeklyDistanceMeters = 0.0,
    averageWeeklyMovingTimeSeconds = 0,
    longestActivityMeters = 0.0,
    longestActivityMovingTimeSeconds = 0,
    averageActivitiesPerWeek = 0.0,
    averageHeartRate = null,
    estimatedThresholdPaceSecondsPerKm = null,
    estimatedThresholdSpeedMetersPerSecond = null,
    weeklySummaries = emptyList(),
)

private fun Int.toPaceString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%d:%02d/km".format(minutes, seconds)
}
