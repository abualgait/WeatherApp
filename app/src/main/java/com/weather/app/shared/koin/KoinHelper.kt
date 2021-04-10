/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.koin


import android.content.Context

import com.weather.app.ui.navhostactivity.navHostActivityModule
import com.weather.app.ui.navhostactivity.post.weatherFragVmModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinHelper {
    companion object {
        fun start(context: Context) {
            startKoin {
                androidContext(context)
                modules(
                    listOf(
                        appModule,
                        weatherFragVmModule,
                        navHostActivityModule

                    )
                )
            }
        }
    }
}