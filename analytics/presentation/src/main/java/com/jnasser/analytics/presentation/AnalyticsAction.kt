package com.jnasser.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick: AnalyticsAction
}