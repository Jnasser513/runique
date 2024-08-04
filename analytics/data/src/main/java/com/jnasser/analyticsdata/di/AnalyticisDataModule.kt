package com.jnasser.analyticsdata.di

import com.jnasser.analytics.domain.AnalyticsRepository
import com.jnasser.analyticsdata.RoomAnalyticsRepository
import com.jnasser.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single { get<RunDatabase>().analyticsDao }
}