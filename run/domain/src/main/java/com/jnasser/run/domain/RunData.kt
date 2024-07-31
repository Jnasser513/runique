package com.jnasser.run.domain

import com.jnasser.core.domain.location.Location
import com.jnasser.core.domain.location.LocationTimeStamp
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LocationTimeStamp>> = emptyList()
)
