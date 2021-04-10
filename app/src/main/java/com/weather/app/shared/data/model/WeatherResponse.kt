/*
 * Created by  Muhammad Sayed  on 4/10/21 3:38 PM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "base")
    var base: String? = "",
    @Json(name = "clouds")
    var clouds: Clouds? = Clouds(),
    @Json(name = "cod")
    var cod: Int? = 0,
    @Json(name = "coord")
    var coord: Coord? = Coord(),
    @Json(name = "dt")
    var dt: Int? = 0,
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "main")
    var main: Main? = Main(),
    @Json(name = "name")
    var name: String? = "",
    @Json(name = "sys")
    var sys: Sys? = Sys(),
    @Json(name = "timezone")
    var timezone: Int? = 0,
    @Json(name = "visibility")
    var visibility: Int? = 0,
    @Json(name = "weather")
    var weather: List<Weather>? = listOf(),
    @Json(name = "wind")
    var wind: Wind? = Wind()
)