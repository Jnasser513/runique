package com.jnasser.auth.data.di

import com.jnasser.auth.data.AuthRepositoryImpl
import com.jnasser.auth.data.EmailPatternValidator
import com.jnasser.auth.domain.AuthRepository
import com.jnasser.auth.domain.PatternValidator
import com.jnasser.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}