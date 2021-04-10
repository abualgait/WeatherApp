/*
 * Created by  Muhammad Sayed  on 9/5/20 4:05 PM
 * Copyright (c) GoodsMart EG. All rights reserved.
 */

package com.weather.app.ui.navhostactivity

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.weather.app.databinding.ActivityNavHostBinding
import com.weather.app.shared.ui.activity.BaseActivity

abstract class RunTimePermissionParentActivity :
    BaseActivity<NavHostActivityVm, ActivityNavHostBinding>() {

    abstract fun onPermissionGranted(granted: Boolean)

    protected fun grantPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= 23) {
            var permissionAdded = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionAdded = false
                }
            }
            if (!permissionAdded) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
            } else {
                onPermissionGranted(true)
            }
        } else {
            onPermissionGranted(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission: " + permissions[0] + " was " + grantResults[0])
                onPermissionGranted(true)
            } else {
                onPermissionGranted(false)
            }
        }
    }

    companion object {
        private val TAG = NavHostActivity::class.java.name
        private var PERMISSION_CODE = 123
    }


}