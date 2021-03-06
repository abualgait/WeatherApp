/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.util

import com.weather.app.BuildConfig


class BuildUtil {

    interface Type {
        companion object {
            val RELEASE = "release"
            val DEBUG = "debug"

        }
    }

    companion object {
        fun isDebug() = BuildConfig.BUILD_TYPE == Type.DEBUG
    }
}