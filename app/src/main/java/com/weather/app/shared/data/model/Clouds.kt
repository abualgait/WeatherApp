/*
 * Created by  Muhammad Sayed  on 4/10/21 3:38 PM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all")
    var all: Int? = 0
)