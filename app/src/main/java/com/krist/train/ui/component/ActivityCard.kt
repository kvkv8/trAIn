package com.krist.train.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krist.train.core.time.DateTimeUtils
import com.krist.train.core.util.metersToKmString
import com.krist.train.core.util.secondsToHoursMinutes
import com.krist.train.domain.model.Activity

@Composable
fun ActivityCard(activity: Activity, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(activity.name, style = MaterialTheme.typography.titleMedium)
            Text("${activity.sportType} · ${DateTimeUtils.formatDate(activity.startDateEpochMillis)}")
            Text("${activity.distanceMeters.metersToKmString()} km · ${activity.movingTimeSeconds.secondsToHoursMinutes()}")
        }
    }
}
