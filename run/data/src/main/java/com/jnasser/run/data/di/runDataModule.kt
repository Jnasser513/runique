package com.jnasser.run.data.di

import com.jnasser.core.domain.run.SyncRunScheduler
import com.jnasser.run.data.CreateRunWorker
import com.jnasser.run.data.DeleteRunWorker
import com.jnasser.run.data.FetchRunsWorker
import com.jnasser.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}