package com.jnasser.wear.run.presentation

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
}