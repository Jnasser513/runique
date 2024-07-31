@file:OptIn(ExperimentalMaterial3Api::class)

package com.jnasser.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jnasser.core.presentation.designsystem.RuniqueTheme
import com.jnasser.core.presentation.designsystem.StartIcon
import com.jnasser.core.presentation.designsystem.StopIcon
import com.jnasser.core.presentation.designsystem.components.RuniqueActionButton
import com.jnasser.core.presentation.designsystem.components.RuniqueDialog
import com.jnasser.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.jnasser.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.jnasser.core.presentation.designsystem.components.RuniqueScaffold
import com.jnasser.core.presentation.designsystem.components.RuniqueToolbar
import com.jnasser.run.presentation.R
import com.jnasser.run.presentation.active_run.components.RunDataCard
import com.jnasser.run.presentation.active_run.maps.TrackerMap
import com.jnasser.run.presentation.active_run.service.ActiveRunService
import com.jnasser.run.presentation.util.hasLocationPermission
import com.jnasser.run.presentation.util.hasNotificationPermission
import com.jnasser.run.presentation.util.shouldShowLocationPermissionRationale
import com.jnasser.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRot(
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunActions) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCoarseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission =
            if (Build.VERSION.SDK_INT >= 33) perms[Manifest.permission.POST_NOTIFICATIONS] == true
            else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunActions.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunActions.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationRationale = showNotificationRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunActions.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunActions.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale
            )
        )

        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuniquePermissions(context)
        }
    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if(state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    LaunchedEffect(key1 = state.shouldTrack) {
        if(context.hasLocationPermission() && state.shouldTrack && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }
    }

    RuniqueScaffold(withGradient = false, topAppBar = {
        RuniqueToolbar(
            showBackButton = true,
            title = stringResource(id = R.string.active_run),
            onBackClick = {
                onAction(ActiveRunActions.OnBackClick)
            })
    }, floatingActionButton = {
        RuniqueFloatingActionButton(
            icon = if (state.shouldTrack) StopIcon else StartIcon,
            onClick = {
                onAction(ActiveRunActions.OnToggleRunClick)
            },
            iconSize = 20.dp,
            contentDescription = if (state.shouldTrack) stringResource(id = R.string.pause_run)
            else stringResource(id = R.string.start_run)
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runDara.locations,
                onSnapshot = {},
                modifier = Modifier.fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runDara,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        RuniqueDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = {
                onAction(ActiveRunActions.OnResumeRunClick)
            },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RuniqueActionButton(
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunActions.OnResumeRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }, secondaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.finish),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunActions.OnFinishRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            })
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(title = stringResource(id = R.string.permission_required),
            onDismiss = { /* Normal dismissing not allowed for permissions */ },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> stringResource(id = R.string.location_notification_rationale)
                state.showLocationRationale -> stringResource(id = R.string.location_rationale)
                else -> stringResource(id = R.string.notification_rationale)
            },
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunActions.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context)
                    })
            })
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(context: Context) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> launch(locationPermission + notificationPermission)
        !hasLocationPermission -> launch(locationPermission)
        else -> launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onServiceToggle = {},
            onAction = {}
        )
    }
}