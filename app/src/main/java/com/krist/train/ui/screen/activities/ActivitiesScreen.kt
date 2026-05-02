package com.krist.train.ui.screen.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.ui.component.ActivityCard

@Composable
fun ActivitiesScreen(viewModel: ActivitiesViewModel) {
    val activities by viewModel.activities.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text("Activities", style = MaterialTheme.typography.headlineMedium) }
        if (activities.isEmpty()) {
            item { Text("No activities synced yet.") }
        }
        items(activities, key = { it.id }) { activity -> ActivityCard(activity) }
    }
}
