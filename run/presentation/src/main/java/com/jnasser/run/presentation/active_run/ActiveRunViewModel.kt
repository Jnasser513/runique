package com.jnasser.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnasser.run.domain.RunningTracker
import com.jnasser.run.presentation.active_run.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
): ViewModel() {

    var state by mutableStateOf(ActiveRunState(
        shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
        hasStartedRunning = ActiveRunService.isServiceActive
    ))
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)
    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack,
        hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        hasLocationPermission
            .onEach { hasPermission ->
                if(hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }
            .launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                runningTracker.setIsTracking(isTracking)
            }
            .launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach {
                state = state.copy(currentLocation = it?.location)
            }
            .launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach {
                state = state.copy(runDara = it)
            }
            .launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach {
                state = state.copy(elapsedTime = it)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ActiveRunActions) {
        when(action) {
            ActiveRunActions.OnFinishRunClick -> {

            }
            ActiveRunActions.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }
            ActiveRunActions.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }
            ActiveRunActions.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }
            is ActiveRunActions.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationRationale
                )
            }
            is ActiveRunActions.SubmitNotificationPermissionInfo -> {
                state = state.copy(
                    showNotificationRationale = action.showNotificationRationale
                )
            }
            ActiveRunActions.DismissRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }
            else -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()
        if(!ActiveRunService.isServiceActive) {
            runningTracker.stopObservingLocation()
        }
    }
}