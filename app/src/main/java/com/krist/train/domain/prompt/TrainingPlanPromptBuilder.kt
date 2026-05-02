package com.krist.train.domain.prompt

import com.krist.train.domain.analysis.TrainingSummary
import com.krist.train.domain.model.Goal
import com.krist.train.domain.model.GoalTimeUtils
import kotlin.math.roundToInt

class TrainingPlanPromptBuilder {
    fun build(summary: TrainingSummary, goal: Goal): String = buildString {
        val raceDate = GoalTimeUtils.formatRaceDate(goal.targetDateEpochMillis)
        val raceCountdown = GoalTimeUtils.raceCountdown(goal.targetDateEpochMillis)
        val targetRacePace = goal.targetRacePaceSecondsPerKm()?.toPaceString()

        appendLine("You are an experienced running coach creating a goal-oriented performance plan.")
        appendLine("Create an aggressive running-only training plan using the athlete data and goal below.")
        appendLine()
        appendLine("Rules:")
        appendLine("- Return only valid JSON. Do not include markdown fences or commentary.")
        appendLine("- Match the JSON shape exactly to the example schema.")
        appendLine("- assumptions, weeks, and workouts must always be arrays, even when they contain only one item.")
        appendLine("- Do not return a string where the schema shows an array.")
        appendLine("- Every workout must include day, type, title, body, and heartZone as strings.")
        appendLine("- Title must be short and prescription-focused, for example: '4 x 4 min, pace: 3:40/km' or '10 km, pace: 5:30/km'.")
        appendLine("- Body must describe how to execute the workout, including warmup/cooldown when relevant, recoveries, and the intended effort.")
        appendLine("- Running only: do not include strength training, cycling, swimming, gym work, mobility, cross-training, or alternative workouts.")
        appendLine("- Do not include recoveryGuidance or warningSigns keys in the JSON.")
        appendLine("- Every week must include at least one interval workout.")
        appendLine("- Interval workouts must be specific, for example: 4 x 4 min at 3:40/km with 2 min jog recovery, 5 x 2 km at 4:00/km with 3 min jog recovery, or 10 x 400 m at 3:30/km with 200 m jog recovery.")
        appendLine("- Do not write generic interval descriptions like 'do intervals' or 'speed session'.")
        appendLine("- Every workout must include exact proposed distance, target pace or pace range, and estimated duration inside details.")
        appendLine("- Estimated duration must be optimistic and calculated from the total distance and the faster end of the target pace range. Do not add excessive padding.")
        appendLine("- For interval sessions, estimate total duration from warmup, reps, recoveries, and cooldown, using realistic jog recovery pace.")
        appendLine("- Use the estimated threshold pace as the central anchor for threshold, tempo, interval, easy, long-run, and race-pace prescriptions.")
        appendLine("- The plan should be aggressive and goal-focused to maximize the chance of reaching the stated goal, while still respecting the athlete's recent training volume.")
        appendLine("- If the risk preference is Conservative, reduce the aggressiveness slightly; otherwise bias toward meaningful progression and race-specific work.")
        appendLine("- Rest days are allowed, but they must be listed as Rest with distance 0 km, pace n/a, and estimated duration 0 min.")
        appendLine("- Include a brief purpose for each workout.")
        appendLine("- If the goal appears unrealistic, state the concern in assumptions, then still provide the most goal-directed feasible plan.")
        appendLine("- The overview must state exactly how long until race day and how the plan is phased to reach the goal.")
        appendLine("- If race day is 20 weeks or less away, create one training week for every full or partial week until race day.")
        appendLine("- If race day is more than 20 weeks away, create a phased overview to race day and a detailed first 12-week block.")
        appendLine("- If no race date is provided, create a detailed 12-week plan.")
        appendLine()
        appendLine("Athlete summary:")
        appendLine("- Activities in last 8 weeks: ${summary.activityCount}")
        appendLine("- Sport types: ${summary.sportTypes.joinToString().ifBlank { "unknown" }}")
        appendLine("- Last 4 weeks distance: ${summary.lastFourWeeksDistanceMeters.toKm()} km")
        appendLine("- Last 8 weeks distance: ${summary.lastEightWeeksDistanceMeters.toKm()} km")
        appendLine("- Average weekly distance: ${summary.averageWeeklyDistanceMeters.toKm()} km")
        appendLine("- Average weekly moving time: ${summary.averageWeeklyMovingTimeSeconds / 60} minutes")
        appendLine("- Longest recent activity: ${summary.longestActivityMeters.toKm()} km")
        appendLine("- Average activities per week: ${"%.1f".format(summary.averageActivitiesPerWeek)}")
        appendLine("- Average heart rate: ${summary.averageHeartRate?.roundToInt()?.toString() ?: "unknown"}")
        appendLine("- Estimated threshold pace: ${summary.estimatedThresholdPaceSecondsPerKm?.toPaceString() ?: "unknown; infer from recent volume and goal"}")
        appendLine("- Estimated threshold speed: ${summary.estimatedThresholdSpeedMetersPerSecond?.let { "%.2f m/s".format(it) } ?: "unknown"}")
        appendLine()
        appendLine("Goal:")
        appendLine("- Name: ${goal.name}")
        appendLine("- Sport: ${goal.sportType}")
        appendLine("- Target distance: ${goal.targetDistanceMeters?.toKm()?.let { "$it km" } ?: "not specified"}")
        appendLine("- Target time: ${goal.targetTimeSeconds?.toDurationString() ?: "not specified"}")
        appendLine("- Target race pace: ${targetRacePace ?: "not specified"}")
        appendLine("- Race date: ${raceDate ?: "not specified"}")
        appendLine("- Time until race: ${raceCountdown?.display() ?: "not specified"}")
        appendLine("- Requested plan detail: ${raceCountdown.toPlanLengthInstruction()}")
        appendLine("- Available training days per week: ${goal.availableDaysPerWeek}")
        appendLine("- Preferred long workout day: ${goal.preferredLongWorkoutDay}")
        appendLine("- Risk preference: ${goal.riskPreference}")
        appendLine("- Notes: ${goal.notes.ifBlank { "none" }}")
        appendLine()
        appendLine("Workout detail format requirement:")
        appendLine("- Use this style for details: 'Distance: 10.0 km. Pace: 4:20-4:30/km for reps, 5:30-5:55/km warmup/cooldown. Estimated duration: 48-52 min. Structure: 2 km warmup, 6 x 800 m at 4:10/km with 400 m jog recovery, 2 km cooldown.'")
        appendLine("- For interval workouts, specify rep count, rep distance or duration, recovery distance or duration, rep pace, total workout distance, and optimistic estimated total duration.")
        appendLine("- For easy, tempo, long, and race-specific workouts, specify exact distance, pace range, and estimated duration.")
        appendLine()
        appendLine("Olympiatoppen intensity zones:")
        appendLine("- I-1: very easy aerobic running; relaxed conversational effort, often used for easy runs and warmup/cooldown.")
        appendLine("- I-2: steady aerobic running; controlled endurance effort, common for normal long runs.")
        appendLine("- I-3: moderate aerobic/threshold-adjacent effort; controlled but purposeful, often marathon/half-marathon support work.")
        appendLine("- I-4: threshold/high aerobic effort; hard but controlled intervals or tempo blocks.")
        appendLine("- I-5: high-intensity interval effort; VO2max/speed intervals with clear recoveries.")
        appendLine("- Put the planned zone or zone range in heartZone, e.g. 'I-1/I-2', 'I-4', or 'I-4/I-5'.")
        appendLine()
        appendLine("JSON schema example:")
        appendLine(TrainingPlanJsonSchema.schemaExample)
    }

    private fun Double.toKm(): String = "%.1f".format(this / 1000.0)

    private fun Int.toPaceString(): String {
        val minutes = this / 60
        val seconds = this % 60
        return "%d:%02d/km".format(minutes, seconds)
    }

    private fun Int.toDurationString(): String {
        val hours = this / 3600
        val minutes = (this % 3600) / 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }

    private fun Goal.targetRacePaceSecondsPerKm(): Int? {
        val seconds = targetTimeSeconds ?: return null
        val distanceKm = targetDistanceMeters?.div(1000.0)?.takeIf { it > 0.0 } ?: return null
        return (seconds / distanceKm).roundToInt()
    }

    private fun com.krist.train.domain.model.RaceCountdown?.toPlanLengthInstruction(): String = when {
        this == null -> "12 detailed weeks because no race date is specified"
        days < 0 -> "race date has passed; create an immediate 4-week reset plan"
        days <= 20L * 7L -> "${planWeeks} detailed weeks through race day"
        else -> "phased overview for ${display()} plus detailed first 12 weeks"
    }
}
