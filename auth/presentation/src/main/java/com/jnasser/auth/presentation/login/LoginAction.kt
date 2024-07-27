package com.jnasser.auth.presentation.login

interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnLogInClick: LoginAction
    data object OnRegisterClick: LoginAction
}