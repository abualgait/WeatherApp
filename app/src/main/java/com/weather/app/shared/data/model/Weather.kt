/*
 * Created by  Muhammad Sayed  on 4/10/21 3:38 PM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "description")
    var description: String? = "",
    @Json(name = "icon")
    var icon: String? = "",
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "main")
    var main: String? = ""
)