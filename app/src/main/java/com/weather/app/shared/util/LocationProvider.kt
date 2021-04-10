package com.weather.app.shared.util

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.weather.app.BuildConfig
import com.weather.app.shared.interfaces.ILocationCallbacks

class LocationProvider(
    private val activity: Activity,
    updateIntervalInMilliSeconds: Long,
    private val iLocationCallbacks: ILocationCallbacks
) {
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private var UPDATE_INTERVAL_IN_MILLISECONDS: Long = 0

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private var FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
        UPDATE_INTERVAL_IN_MILLISECONDS / 2

    /**
     * Handle location updates using this variable because some times location updates are not disabled
     * even if we get success in locationProvider.removeUpdates()
     */
    private var mRequestingLocationUpdates = false

    /**
     * Provides access to the Fused Location Provider API.
     */
    private val mFusedLocationClient: FusedLocationProviderClient

    /**
     * Provides access to the Location Settings API.
     */
    private val mSettingsClient: SettingsClient

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (mRequestingLocationUpdates) {
                    if (BuildConfig.DEBUG) {
//                        Utils.INSTANCE.showToast("Lat: " + locationResult.getLastLocation().getLatitude() + " Long: " + locationResult.getLastLocation().getLongitude());
                    }
                    iLocationCallbacks.getCurrentLocation(locationResult.getLastLocation())
                }
            }
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        //mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        //mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    /**
     * Uses a [com.google.android.gms.location.LocationSettingsRequest.Builder] to build
     * a [com.google.android.gms.location.LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    fun startLocationUpdates() {
        //Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(
                activity,
                object : OnSuccessListener<LocationSettingsResponse?>  {
                    @SuppressLint("MissingPermission")
                    override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {
                        Log.i(
                            TAG,
                            "All location settings are satisfied."
                        )
                        mRequestingLocationUpdates = true
                        mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                    }
                })
            .addOnFailureListener(activity, OnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        val rae = e as ResolvableApiException
                        iLocationCallbacks.checkLocationSettings(rae)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        mRequestingLocationUpdates = false
                        Log.e(
                            TAG,
                            "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        )
                    }
                }
            })
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(
                TAG,
                "stopLocationUpdates: updates never requested, no-op."
            )
            return
        }
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(activity, OnCompleteListener<Void?> {
                mRequestingLocationUpdates = false
                Log.d(
                    TAG,
                    "Location Updates Removed"
                )
            })
    }

    companion object {
        private val TAG =
            LocationProvider::class.java.simpleName
    }

    init {
        UPDATE_INTERVAL_IN_MILLISECONDS = updateIntervalInMilliSeconds
        FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mSettingsClient = LocationServices.getSettingsClient(activity)

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
    }
}