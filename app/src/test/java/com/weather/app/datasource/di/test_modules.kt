/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.datasource.di


import com.weather.app.datasource.util.TestSchedulerProvider
import com.weather.app.shared.koin.appModule
import com.weather.app.ui.navhostactivity.favourite.favouriteFragVmModule
import com.weather.app.ui.navhostactivity.post.weatherFragVmModule
import com.weather.app.ui.navhostactivity.postdetails.postDetailsFragVmModule

import org.koin.dsl.module


val testRxModule = module { single { TestSchedulerProvider() } }

val testApp = appModule
val postDetailsFragVmTest = postDetailsFragVmModule
val postFragVmModuleTest = weatherFragVmModule
val favFragVmModuleTest = favouriteFragVmModule