/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.ui.navhostactivity.post


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.weather.app.shared.data.DataManager
import com.weather.app.shared.data.model.Resource
import com.weather.app.shared.data.model.WeatherResponse
import com.weather.app.shared.network.NetworkHelper
import com.weather.app.shared.util.SingleLiveEvent
import com.weather.app.shared.util.ext.with
import com.weather.app.shared.vm.BaseViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherFragVmModule = module {
    viewModel { WeatherFragVm(get(), get()) }
}

class WeatherFragVm(dataManager: DataManager, networkHelper: NetworkHelper) :
    BaseViewModel(dataManager, networkHelper) {

    private val _data = MutableLiveData<WeatherResponse>()
    val data: LiveData<WeatherResponse> = _data


    fun setData(result: WeatherResponse?) {
        _data.value = result
    }


    private val _response = SingleLiveEvent<Resource<WeatherResponse>>()
    val response: LiveData<Resource<WeatherResponse>> = _response


    @SuppressLint("CheckResult")
    fun getCityWeather(cityName: String) {
        _response.value = Resource.loading(null)
        //view.showLoading()
        api.getCityWeather(cityName).with(scheduler)
            .subscribe({ result ->
                _response.value = Resource.success(result)
                //view.hideLoading()
            }, { throwable ->
                _response.value = Resource.error(throwable.message, null)
                // view.hideLoading()
            })
    }


    @SuppressLint("CheckResult")
    fun getCityWeatherByLatLng(
        lat: Double,
        lng: Double
    ) {

        _response.value = Resource.loading(null)
        //view.showLoading()
        api.getCityWeatherByLatLng(lat, lng).with(scheduler)
            .subscribe({ result ->
                _response.value = Resource.success(result)
                //view.hideLoading()
            }, { throwable ->
                _response.value = Resource.error(throwable.message, null)
                // view.hideLoading()
            })
    }

}