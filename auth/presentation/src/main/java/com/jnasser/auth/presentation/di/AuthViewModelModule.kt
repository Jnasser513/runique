package com.jnasser.auth.presentation.di

import androidx.compose.foundation.ExperimentalFoundationApi
import com.jnasser.auth.presentation.login.LoginViewModel
import com.jnasser.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalFoundationApi::class)
val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}