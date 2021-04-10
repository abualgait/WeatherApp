/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.network

import com.weather.app.shared.data.model.WeatherResponse
import io.reactivex.Observable


class ApiRepository(private val api: ApiInterface) {

    fun getCityWeather(city: String): Observable<WeatherResponse> =
        api.getCityWeather(city)

    fun getCityWeatherByLatLng(lat:Double,lng:Double): Observable<WeatherResponse> =
        api.getCityWeatherByLatLng(lat,lng)

}
