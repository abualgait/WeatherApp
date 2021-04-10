/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.ui.navhostactivity.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.util.Log
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.jakewharton.rxbinding2.widget.RxTextView
import com.weather.app.R
import com.weather.app.databinding.FragmentWeatherBinding
import com.weather.app.shared.data.model.Status
import com.weather.app.shared.ext.hide
import com.weather.app.shared.ext.onClicked
import com.weather.app.shared.ext.show
import com.weather.app.shared.interfaces.ILocationCallbacks
import com.weather.app.shared.ui.frag.BaseFrag
import com.weather.app.shared.util.LocationProvider
import com.weather.app.shared.util.ThreadUtil
import com.weather.app.shared.util.ext.with
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class WeatherFrag : BaseFrag<WeatherFragVm, FragmentWeatherBinding>(), ILocationCallbacks {

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var locationProvider: LocationProvider? = null
    private var rae: ResolvableApiException? = null
    override val vm: WeatherFragVm by viewModel()
    override var layoutId: Int = R.layout.fragment_weather

    @SuppressLint("CheckResult")
    override fun doOnViewCreated() {
        super.doOnViewCreated()
        hasSwipeRefresh = true
        binding.apply {
            viewmodel = vm
        }

        grantPermissions(permission)
        handleObservers()
        binding.btnClear.onClicked {
            binding.edtSearch.setText("")
        }
        RxTextView.textChanges(binding.edtSearch)
            .filter { text -> text.length >= 2 || text.isEmpty() }
            .skip(1)
            .debounce(250, TimeUnit.MILLISECONDS)
            .with(vm.scheduler)
            .subscribe {
                if (it.toString().trim { it <= ' ' }.isEmpty()) {
                    binding.btnClear.hide()
                    vm.getCityWeather(it.toString())
                } else {
                    vm.getCityWeather(it.toString())
                    binding.btnClear.show()
                }
            }

    }


    private fun handleObservers() {
        view.showLoading()

        vm.response.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.COMPLETE -> hideLoading()
                Status.SUCCESS -> {
                    view.hideLoading()
                    vm.setData(resource.data)
                }
                Status.ERROR -> {
                    view.hideLoading()
                }
                Status.LOADING -> showLoading()
                Status.OFFLINE -> {
                }
            }
        })
    }


    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        vm.getCityWeather("")
    }

    override fun onRetryClicked() {
        super.onRetryClicked()
        vm.getCityWeather("")
    }

    override fun showLoading() {
        super.showLoading()
        showLoader()
        ThreadUtil.runOnUiThread { binding.mSwipeRefresh.isRefreshing = true }
    }

    override fun hideLoading() {
        showMainLayout()
        ThreadUtil.runOnUiThread { binding.mSwipeRefresh.isRefreshing = false }
    }

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(granted: Boolean) {
        if (granted) {
            locationProvider = LocationProvider(activity()!!, 0, this)
            locationProvider!!.startLocationUpdates()
        } else {
            grantPermissions(permission)
        }
    }


    override fun checkLocationSettings(rae: ResolvableApiException) {
        this.rae = rae
        startGpsResolution()
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(currentLocation: Location) {
        vm.getCityWeatherByLatLng(currentLocation.latitude, currentLocation.longitude)
    }


    private fun startGpsResolution() {
        //Show the gps enable dialog by calling startResolutionForResult(), and check the
        //result in onActivityResult().
        try {
            rae!!.startResolutionForResult(activity(), LOCATION_PERMISSION_REQUEST_CODE)
        } catch (sie: IntentSender.SendIntentException) {
            Log.i("PendingIntent", "PendingIntent unable to execute request.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            LOCATION_PERMISSION_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(
                        "PendingIntent",
                        "User agreed to make required location settings changes."
                    )
                    locationProvider!!.startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    //if user cancels gps enabling
                    //show the enabling popup again
                    Log.i(
                        "PendingIntent",
                        "User chose not to make required location settings changes."
                    )
                    startGpsResolution()
                }
            }


        }


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}


