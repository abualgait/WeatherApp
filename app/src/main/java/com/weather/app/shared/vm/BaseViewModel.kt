/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.vm


import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.weather.app.shared.data.DataManager
import com.weather.app.shared.network.ApiRepository
import com.weather.app.shared.network.NetworkHelper
import com.weather.app.shared.rx.SchedulerProvider
import com.weather.app.shared.util.SharedPref
import com.weather.app.shared.util.io.app.MyApp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(dm: DataManager,val networkHelper: NetworkHelper) : ViewModel() {


    var pref: SharedPref = dm.pref
    var api: ApiRepository = dm.api
    var scheduler: SchedulerProvider = dm.schedulerProvider


    val disposables = CompositeDisposable()

    fun isConnected() = networkHelper.isNetworkConnected()



    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


    protected fun getString(@StringRes res: Int): String {
        return MyApp.context.getString(res)
    }


}
