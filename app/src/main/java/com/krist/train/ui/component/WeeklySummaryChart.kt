package com.krist.train.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.krist.train.core.util.metersToKmString
import com.krist.train.domain.analysis.WeeklySummary

@Composable
fun WeeklySummaryChart(weeks: List<WeeklySummary>, modifier: Modifier = Modifier) {
    Column(modifier) {
        weeks.forEachIndexed { index, week ->
            Text("Week ${index + 1}: ${week.distanceMeters.metersToKmString()} km across ${week.activityCount} activities")
        }
    }
}
