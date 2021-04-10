/*
 * Created by  Muhammad Sayed  on 4/10/21 3:38 PM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "feels_like")
    var feelsLike: Double? = 0.0,
    @Json(name = "humidity")
    var humidity: Int? = 0,
    @Json(name = "pressure")
    var pressure: Int? = 0,
    @Json(name = "temp")
    var temp: Double? = 0.0,
    @Json(name = "temp_max")
    var tempMax: Double? = 0.0,
    @Json(name = "temp_min")
    var tempMin: Double? = 0.0
)