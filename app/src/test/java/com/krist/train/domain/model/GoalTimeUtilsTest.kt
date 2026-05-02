package com.krist.train.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class GoalTimeUtilsTest {
    private val zone = ZoneId.of("UTC")

    @Test
    fun parsesPlainMinutesAsTargetTime() {
        assertEquals(95 * 60, GoalTimeUtils.parseTargetTimeSeconds("95"))
    }

    @Test
    fun parsesHoursAndMinutesAsTargetTime() {
        assertEquals(95 * 60, GoalTimeUtils.parseTargetTimeSeconds("1:35"))
    }

    @Test
    fun parsesHoursMinutesAndSecondsAsTargetTime() {
        assertEquals(5_730, GoalTimeUtils.parseTargetTimeSeconds("1:35:30"))
    }

    @Test
    fun rejectsInvalidTargetTime() {
        assertNull(GoalTimeUtils.parseTargetTimeSeconds("fast"))
        assertNull(GoalTimeUtils.parseTargetTimeSeconds(""))
    }

    @Test
    fun parsesRaceDate() {
        assertEquals(millis("2026-09-13"), GoalTimeUtils.parseRaceDateEpochMillis("2026-09-13", zone))
    }

    @Test
    fun formatsRaceCountdown() {
        val race = millis("2026-09-13")
        val now = millis("2026-05-02")

        val countdown = GoalTimeUtils.raceCountdown(race, now, zone)

        assertEquals(134L, countdown?.days)
        assertEquals("19 weeks and 1 day", countdown?.display())
        assertEquals(20L, countdown?.planWeeks)
    }

    private fun millis(date: String): Long = LocalDate.parse(date)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()
}
