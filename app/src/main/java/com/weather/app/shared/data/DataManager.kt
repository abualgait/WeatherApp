/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data


import com.weather.app.shared.network.ApiRepository
import com.weather.app.shared.rx.SchedulerProvider
import com.weather.app.shared.util.SharedPref

open class DataManager(
    val pref: SharedPref,
    val api: ApiRepository,
    val schedulerProvider: SchedulerProvider
)
