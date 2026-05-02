package com.krist.train.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

data class RaceCountdown(
    val days: Long,
) {
    val weeks: Long = days / 7
    val remainingDays: Long = days % 7
    val planWeeks: Long = ((days + 6) / 7).coerceAtLeast(1)

    fun display(): String = when {
        days < 0 -> "race date has passed"
        days == 0L -> "race day is today"
        weeks == 0L -> days.plural("day")
        remainingDays == 0L -> weeks.plural("week")
        else -> "${weeks.plural("week")} and ${remainingDays.plural("day")}"
    }

    private fun Long.plural(unit: String): String = "$this $unit${if (this == 1L) "" else "s"}"
}

object GoalTimeUtils {
    fun parseTargetTimeSeconds(input: String): Int? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null

        return if (":" in trimmed) {
            val parts = trimmed.split(":").map { it.toIntOrNull() ?: return null }
            when (parts.size) {
                2 -> parts[0] * 3600 + parts[1] * 60
                3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
                else -> null
            }
        } else {
            trimmed.toIntOrNull()?.times(60)
        }?.takeIf { it > 0 }
    }

    fun parseRaceDateEpochMillis(input: String, zoneId: ZoneId = ZoneId.systemDefault()): Long? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null
        return try {
            LocalDate.parse(trimmed)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()
        } catch (_: DateTimeParseException) {
            null
        }
    }

    fun raceCountdown(
        targetDateEpochMillis: Long?,
        nowEpochMillis: Long = System.currentTimeMillis(),
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): RaceCountdown? {
        if (targetDateEpochMillis == null) return null
        val today = Instant.ofEpochMilli(nowEpochMillis).atZone(zoneId).toLocalDate()
        val raceDate = Instant.ofEpochMilli(targetDateEpochMillis).atZone(zoneId).toLocalDate()
        return RaceCountdown(ChronoUnit.DAYS.between(today, raceDate))
    }

    fun formatRaceDate(targetDateEpochMillis: Long?, zoneId: ZoneId = ZoneId.systemDefault()): String? =
        targetDateEpochMillis?.let { Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate().toString() }
}
