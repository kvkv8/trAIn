package com.krist.train.domain.analysis

import com.krist.train.domain.model.Activity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TrainingSummaryCalculatorTest {
    private val zone = ZoneId.of("UTC")
    private val calculator = TrainingSummaryCalculator(zone)

    @Test
    fun calculatesEightWeekSummary() {
        val now = millis("2026-05-01")
        val activities = listOf(
            activity(id = 1, date = "2026-04-28", distance = 10_000.0, seconds = 3_600),
            activity(id = 2, date = "2026-04-20", distance = 8_000.0, seconds = 2_800),
            activity(id = 3, date = "2026-03-10", distance = 20_000.0, seconds = 7_200),
            activity(id = 4, date = "2026-01-01", distance = 50_000.0, seconds = 10_000),
        )

        val summary = calculator.calculate(activities, now)

        assertEquals(3, summary.activityCount)
        assertEquals(38_000.0, summary.lastEightWeeksDistanceMeters, 0.01)
        assertEquals(18_000.0, summary.lastFourWeeksDistanceMeters, 0.01)
        assertEquals(4_750.0, summary.averageWeeklyDistanceMeters, 0.01)
        assertEquals(20_000.0, summary.longestActivityMeters, 0.01)
        assertEquals(350, summary.estimatedThresholdPaceSecondsPerKm)
        assertEquals(1000.0 / 350.0, summary.estimatedThresholdSpeedMetersPerSecond ?: 0.0, 0.01)
    }

    @Test
    fun estimatesThresholdFromFastestEligibleRunAndIgnoresNonRuns() {
        val now = millis("2026-05-01")
        val activities = listOf(
            activity(id = 1, date = "2026-04-28", distance = 5_000.0, seconds = 1_500),
            activity(id = 2, date = "2026-04-25", distance = 8_000.0, seconds = 2_880),
            activity(id = 3, date = "2026-04-20", distance = 20_000.0, seconds = 4_000, sportType = "Ride"),
        )

        val summary = calculator.calculate(activities, now)

        assertEquals(315, summary.estimatedThresholdPaceSecondsPerKm)
    }

    private fun activity(
        id: Long,
        date: String,
        distance: Double,
        seconds: Int,
        sportType: String = "Run",
    ): Activity = Activity(
        id = id,
        name = "Run $id",
        sportType = sportType,
        startDateEpochMillis = millis(date),
        distanceMeters = distance,
        movingTimeSeconds = seconds,
        elapsedTimeSeconds = seconds,
        totalElevationGainMeters = 0.0,
        averageHeartRate = null,
        maxHeartRate = null,
        relativeEffort = null,
    )

    private fun millis(date: String): Long = LocalDate.parse(date)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()
}
