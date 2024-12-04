package com.andyslab.geobud.utils

import java.util.Locale

fun Long.timeMillisToString(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d : %02d", minutes, seconds)
}
