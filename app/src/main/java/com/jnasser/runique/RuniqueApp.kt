package com.jnasser.runique

import android.app.Application
import com.jnasser.auth.data.di.authDataModule
import com.jnasser.auth.presentation.di.authViewModelModule
import com.jnasser.core.data.di.coreDataModule
import com.jnasser.run.location.di.locationModule
import com.jnasser.run.presentation.di.runModule
import com.jnasser.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runModule,
                locationModule
            )
        }
    }

}