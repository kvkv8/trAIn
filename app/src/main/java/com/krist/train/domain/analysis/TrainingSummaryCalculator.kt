package com.krist.train.domain.analysis

import com.krist.train.domain.model.Activity
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

class TrainingSummaryCalculator(
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) {
    fun calculate(activities: List<Activity>, nowEpochMillis: Long = System.currentTimeMillis()): TrainingSummary {
        val sorted = activities.sortedBy { it.startDateEpochMillis }
        val now = Instant.ofEpochMilli(nowEpochMillis).atZone(zoneId).toLocalDate()
        val eightWeeksAgo = now.minusWeeks(8).atStartOfDay(zoneId).toInstant().toEpochMilli()
        val fourWeeksAgo = now.minusWeeks(4).atStartOfDay(zoneId).toInstant().toEpochMilli()
        val recentEightWeeks = sorted.filter { it.startDateEpochMillis >= eightWeeksAgo }
        val recentFourWeeks = sorted.filter { it.startDateEpochMillis >= fourWeeksAgo }
        val weeklySummaries = recentEightWeeks
            .groupBy { weekStartMillis(it.startDateEpochMillis) }
            .map { (weekStart, weekActivities) ->
                WeeklySummary(
                    weekStartEpochMillis = weekStart,
                    activityCount = weekActivities.size,
                    distanceMeters = weekActivities.sumOf { it.distanceMeters },
                    movingTimeSeconds = weekActivities.sumOf { it.movingTimeSeconds },
                    elevationGainMeters = weekActivities.sumOf { it.totalElevationGainMeters },
                )
            }
            .sortedBy { it.weekStartEpochMillis }

        val longest = recentEightWeeks.maxByOrNull { it.distanceMeters }
        val heartRates = recentEightWeeks.mapNotNull { it.averageHeartRate }
        val estimatedThresholdPace = estimateThresholdPaceSecondsPerKm(recentEightWeeks)

        return TrainingSummary(
            activityCount = recentEightWeeks.size,
            sportTypes = recentEightWeeks.map { it.sportType }.distinct().sorted(),
            lastFourWeeksDistanceMeters = recentFourWeeks.sumOf { it.distanceMeters },
            lastEightWeeksDistanceMeters = recentEightWeeks.sumOf { it.distanceMeters },
            averageWeeklyDistanceMeters = recentEightWeeks.sumOf { it.distanceMeters } / 8.0,
            averageWeeklyMovingTimeSeconds = recentEightWeeks.sumOf { it.movingTimeSeconds } / 8,
            longestActivityMeters = longest?.distanceMeters ?: 0.0,
            longestActivityMovingTimeSeconds = longest?.movingTimeSeconds ?: 0,
            averageActivitiesPerWeek = recentEightWeeks.size / 8.0,
            averageHeartRate = heartRates.takeIf { it.isNotEmpty() }?.average(),
            estimatedThresholdPaceSecondsPerKm = estimatedThresholdPace,
            estimatedThresholdSpeedMetersPerSecond = estimatedThresholdPace?.let { 1000.0 / it },
            weeklySummaries = weeklySummaries,
        )
    }

    private fun estimateThresholdPaceSecondsPerKm(activities: List<Activity>): Int? {
        return activities
            .asSequence()
            .filter { it.sportType.contains("run", ignoreCase = true) }
            .filter { it.distanceMeters >= 3_000.0 }
            .filter { it.movingTimeSeconds in 20 * 60..75 * 60 }
            .map { activity ->
                val paceSecondsPerKm = activity.movingTimeSeconds / (activity.distanceMeters / 1000.0)
                if (activity.movingTimeSeconds < 30 * 60) paceSecondsPerKm * 1.05 else paceSecondsPerKm
            }
            .minOrNull()
            ?.toInt()
    }

    private fun weekStartMillis(epochMillis: Long): Long {
        val localDate = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        return localDate
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zoneId)
            .truncatedTo(ChronoUnit.DAYS)
            .toInstant()
            .toEpochMilli()
    }
}
