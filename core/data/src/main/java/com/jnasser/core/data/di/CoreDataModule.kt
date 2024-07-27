package com.jnasser.core.data.di

import com.jnasser.core.data.networking.HttpClientFactory
import com.jnasser.core.data.auth.EncryptedSessionStorage
import com.jnasser.core.domain.util.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}