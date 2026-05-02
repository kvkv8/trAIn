package com.krist.train.ui.screen.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.core.util.metersToKmString
import com.krist.train.core.util.secondsToHoursMinutes
import com.krist.train.domain.analysis.TrainingSummary
import com.krist.train.ui.component.WeeklySummaryChart

@Composable
fun InsightsScreen(viewModel: InsightsViewModel) {
    val summary by viewModel.summary.collectAsState(initial = emptySummary())

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Insights", style = MaterialTheme.typography.headlineMedium)
        Text("Last 8 weeks: ${summary.lastEightWeeksDistanceMeters.metersToKmString()} km")
        Text("Last 4 weeks: ${summary.lastFourWeeksDistanceMeters.metersToKmString()} km")
        Text("Average week: ${summary.averageWeeklyDistanceMeters.metersToKmString()} km · ${summary.averageWeeklyMovingTimeSeconds.secondsToHoursMinutes()}")
        Text("Longest recent activity: ${summary.longestActivityMeters.metersToKmString()} km")
        Text("Estimated threshold pace: ${summary.estimatedThresholdPaceSecondsPerKm?.toPaceString() ?: "unknown"}")
        WeeklySummaryChart(summary.weeklySummaries)
    }
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
