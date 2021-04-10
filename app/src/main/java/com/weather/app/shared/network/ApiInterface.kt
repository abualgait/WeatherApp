/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.network

import com.weather.app.shared.data.model.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.*


@JvmSuppressWildcards
interface ApiInterface {

    @GET("weather")
    fun getCityWeather(
        @Query("q") q: String,
        @Query("appid") appid: String = "d1b31699da5320271befb90cb56d5624"
    ): Observable<WeatherResponse>


    @GET("weather")
    fun getCityWeatherByLatLng(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = "d1b31699da5320271befb90cb56d5624"
    ): Observable<WeatherResponse>
}