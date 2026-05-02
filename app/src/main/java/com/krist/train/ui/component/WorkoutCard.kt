package com.krist.train.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.domain.model.Workout

@Composable
fun WorkoutCard(workout: Workout, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(workout.day, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(workout.type, style = MaterialTheme.typography.labelLarge)
            }
            Text(workout.displayTitle(), style = MaterialTheme.typography.titleMedium)
            Text(workout.displayBody(), style = MaterialTheme.typography.bodyMedium)
            workout.heartZone.takeIf { it.isNotBlank() }?.let { zone ->
                AssistChip(onClick = {}, label = { Text(zone) })
            }
        }
    }
}

private fun Workout.displayTitle(): String = title.ifBlank {
    val compactDetails = details.substringBefore(". ").take(80)
    listOf(type, compactDetails).filter { it.isNotBlank() }.joinToString(": ").ifBlank { "Workout" }
}

private fun Workout.displayBody(): String = body.ifBlank {
    listOf(details, purpose.takeIf { it.isNotBlank() }?.let { "Purpose: $it" })
        .filterNotNull()
        .filter { it.isNotBlank() }
        .joinToString("\n")
}
