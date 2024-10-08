package com.jnasser.core.database.di

import androidx.room.Room
import com.jnasser.core.database.RoomLocalRunDataSource
import com.jnasser.core.database.RunDatabase
import com.jnasser.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        ).build()
    }
    single { get<RunDatabase>().runDao }
    single { get<RunDatabase>().runPendingSyncDao }

    singleOf(::RoomLocalRunDataSource).bind<LocalRunDataSource>()
}