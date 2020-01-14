package com.voidx.github

import android.app.Application
import com.voidx.github.data.dataModule
import com.voidx.github.feature.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GithubApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                dataModule +
                        featureModule
            )
        }
    }
}