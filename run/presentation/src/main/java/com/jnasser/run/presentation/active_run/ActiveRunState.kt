package com.jnasser.run.presentation.active_run

import com.jnasser.core.domain.location.Location
import com.jnasser.run.domain.RunData
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val runDara: RunData = RunData(),
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false,
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false
)
