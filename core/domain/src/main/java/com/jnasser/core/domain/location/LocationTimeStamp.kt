package com.jnasser.core.domain.location

import kotlin.time.Duration

data class LocationTimeStamp(
    val location: LocationWithAltitude,
    val durationTimestamp: Duration
)
