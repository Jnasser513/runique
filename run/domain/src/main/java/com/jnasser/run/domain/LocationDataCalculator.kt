package com.jnasser.run.domain

import com.jnasser.core.domain.location.LocationTimeStamp
import kotlin.math.roundToInt

object LocationDataCalculator {

    fun getTotalDistanceMeters(locations: List<List<LocationTimeStamp>>): Int {
        return locations
            .sumOf {  timestampPerLine ->
                timestampPerLine.zipWithNext { location1, location2 ->
                    location1.location.location.distanceTo(location2.location.location)
                }.sum().roundToInt()
            }
    }
}