package com.krist.train.domain.analysis

data class LoadAnalysis(
    val recentWeeklyIncreasePercent: Double?,
    val isSharpIncrease: Boolean,
)

class LoadAnalyzer {
    fun analyze(summary: TrainingSummary): LoadAnalysis {
        val weeks = summary.weeklySummaries.takeLast(4)
        if (weeks.size < 4) return LoadAnalysis(null, false)

        val previousAverage = weeks.take(3).map { it.distanceMeters }.average()
        val latest = weeks.last().distanceMeters
        if (previousAverage <= 0.0) return LoadAnalysis(null, false)

        val increase = ((latest - previousAverage) / previousAverage) * 100.0
        return LoadAnalysis(increase, increase > 20.0)
    }
}
