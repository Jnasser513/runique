package com.jnasser.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnLogInClick: LoginAction
    data object OnRegisterClick: LoginAction
}