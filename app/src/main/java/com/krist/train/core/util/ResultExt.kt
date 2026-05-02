package com.krist.train.core.util

fun Double.metersToKmString(decimals: Int = 1): String = "%.${decimals}f".format(this / 1000.0)

fun Int.secondsToHoursMinutes(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
