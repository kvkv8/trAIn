package com.krist.train.core.time

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    fun formatDate(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String =
        Instant.ofEpochMilli(epochMillis).atZone(zoneId).format(dateFormatter)
}
