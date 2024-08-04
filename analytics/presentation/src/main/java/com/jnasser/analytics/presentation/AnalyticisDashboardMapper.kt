package com.jnasser.analytics.presentation

import com.jnasser.analytics.domain.AnalyticsValue
import com.jnasser.core.presentation.ui.formatted
import com.jnasser.core.presentation.ui.toFormattedKm
import com.jnasser.core.presentation.ui.toFormattedKmh
import com.jnasser.core.presentation.ui.toFormattedMetters
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60

    return "${days}d ${hours}d ${minutes}m"
}

fun AnalyticsValue.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgDistance = (avgDistanceRun / 1000.0).toFormattedKm(),
        avgPace = avgPacePerRun.seconds.formatted()
    )
}