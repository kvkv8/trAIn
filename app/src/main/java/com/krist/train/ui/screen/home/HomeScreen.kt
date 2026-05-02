package com.krist.train.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krist.train.core.time.DateTimeUtils
import com.krist.train.core.util.metersToKmString
import com.krist.train.core.util.secondsToHoursMinutes
import com.krist.train.domain.model.Activity
import com.krist.train.domain.model.TrainingPlan

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenCoach: () -> Unit,
    onOpenPlan: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val activities by viewModel.activities.collectAsState(initial = emptyList())
    val goal by viewModel.latestGoal.collectAsState(initial = null)
    val plan by viewModel.latestPlan.collectAsState(initial = null)
    val recentDistance = activities.take(12).sumOf { it.distanceMeters }
    val recentTime = activities.take(12).sumOf { it.movingTimeSeconds }
    val latestActivity = activities.maxByOrNull { it.startDateEpochMillis }

    LazyColumn(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroCard(
                goalName = goal?.name ?: "No goal selected",
                plan = plan,
                onOpenCoach = onOpenCoach,
                onOpenPlan = onOpenPlan,
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    label = "Synced",
                    value = activities.size.toString(),
                    caption = "activities",
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = "Recent load",
                    value = recentDistance.metersToKmString(),
                    caption = "km in last 12",
                    modifier = Modifier.weight(1f),
                )
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    label = "Time banked",
                    value = recentTime.secondsToHoursMinutes(),
                    caption = "last 12 activities",
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = "Plan",
                    value = plan?.weeks?.size?.toString() ?: "0",
                    caption = "weeks ready",
                    modifier = Modifier.weight(1f),
                )
            }
        }
        item {
            LatestActivityCard(activity = latestActivity, onOpenSettings = onOpenSettings)
        }
    }
}

@Composable
private fun HeroCard(
    goalName: String,
    plan: TrainingPlan?,
    onOpenCoach: () -> Unit,
    onOpenPlan: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF171717), Color(0xFF6F351E), Color(0xFFFF6B35)),
                ),
            )
            .padding(22.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("trAIn", color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
                Text("Your Strava data, turned into a coachable plan.", color = Color.White.copy(alpha = 0.82f))
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.14f)),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Current focus", color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.labelLarge)
                    Text(goalName, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (plan == null) "No plan yet. Build one from the Coach tab." else "${plan.weeks.size} weeks generated and saved locally.",
                        color = Color.White.copy(alpha = 0.82f),
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onOpenCoach, modifier = Modifier.weight(1f)) { Text("Open Coach") }
                OutlinedButton(onClick = onOpenPlan, enabled = plan != null, modifier = Modifier.weight(1f)) { Text("View Plan") }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, caption: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(caption, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun LatestActivityCard(activity: Activity?, onOpenSettings: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Latest signal", style = MaterialTheme.typography.titleLarge)
            if (activity == null) {
                Text("No Strava activities synced yet. Add credentials and sync from Settings.")
                Button(onClick = onOpenSettings) { Text("Open Settings") }
            } else {
                Text(activity.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${activity.sportType} · ${DateTimeUtils.formatDate(activity.startDateEpochMillis)}")
                Text("${activity.distanceMeters.metersToKmString()} km · ${activity.movingTimeSeconds.secondsToHoursMinutes()}")
            }
        }
    }
}
