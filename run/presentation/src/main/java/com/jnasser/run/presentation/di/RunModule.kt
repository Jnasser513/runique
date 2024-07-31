package com.jnasser.run.presentation.di

import com.jnasser.run.domain.RunningTracker
import com.jnasser.run.presentation.active_run.ActiveRunViewModel
import com.jnasser.run.presentation.run_overview.RunOverViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverViewViewModel)
    viewModelOf(::ActiveRunViewModel)
}