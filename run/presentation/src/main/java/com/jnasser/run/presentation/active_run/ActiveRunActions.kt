package com.jnasser.run.presentation.active_run

sealed interface ActiveRunActions {
    data object OnToggleRunClick: ActiveRunActions
    data object OnFinishRunClick: ActiveRunActions
    data object OnResumeRunClick: ActiveRunActions
    data object OnBackClick: ActiveRunActions
    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): ActiveRunActions
    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean
    ): ActiveRunActions
    data object DismissRationaleDialog: ActiveRunActions
    class OnRunProcess(val mapPictureBytes: ByteArray): ActiveRunActions
}