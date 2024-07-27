package com.jnasser.auth.presentation.login

import com.jnasser.core.presentation.ui.UiText

interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class Error(val error: UiText): LoginEvent
}