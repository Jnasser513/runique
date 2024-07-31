package com.jnasser.run.domain

import com.jnasser.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observeLocation(internal: Long): Flow<LocationWithAltitude>
}