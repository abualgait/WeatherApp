/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.ui.navhostactivity

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.weather.app.shared.data.DataManager
import com.weather.app.shared.data.model.Resource
import com.weather.app.shared.network.NetworkHelper
import com.weather.app.shared.util.SingleLiveEvent
import com.weather.app.shared.util.ext.withDB
import com.weather.app.shared.vm.BaseViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val navHostActivityModule = module {
    viewModel { NavHostActivityVm(get(), get()) }
}

class NavHostActivityVm(dataManager: DataManager, networkHelper: NetworkHelper) :
    BaseViewModel(dataManager, networkHelper) {

}