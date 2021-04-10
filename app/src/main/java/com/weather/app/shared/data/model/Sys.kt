/*
 * Created by  Muhammad Sayed  on 4/10/21 3:38 PM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "country")
    var country: String? = "",
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "sunrise")
    var sunrise: Int? = 0,
    @Json(name = "sunset")
    var sunset: Int? = 0,
    @Json(name = "type")
    var type: Int? = 0
)