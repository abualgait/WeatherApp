/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.ui.frag

import android.content.BroadcastReceiver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.weather.app.shared.ui.activity.BaseActivity
import com.weather.app.shared.ui.view.BaseView
import com.weather.app.shared.util.FlashbarUtil
import com.weather.app.shared.vm.BaseViewModel
import kotlinx.android.synthetic.main.app_loading_screen.*
import kotlinx.android.synthetic.main.app_no_internet_connection.*
import kotlinx.android.synthetic.main.app_no_result_found.*
import kotlinx.android.synthetic.main.app_server_error.*
import kotlinx.android.synthetic.main.frag_main_weather.*
import kotlinx.android.synthetic.main.fragment_weather.*
import java.util.*

abstract class BaseFrag<VM : BaseViewModel, B : ViewDataBinding> : Fragment(), BaseView<VM, B> {

    final override lateinit var binding: B
    lateinit var view: BaseView<*, *>
    protected open fun setupUi() {}
    protected open fun setupFont() {}
    protected open fun doOnViewCreated() {}
    protected open fun doOnCreateView(savedInstanceState: Bundle?) {}
    protected fun doOnResume() {}

    open var hasBackNavigation = false
    open var hasSwipeRefresh = false


    /*Recycler View Data*/

    protected var mList: ArrayList<Any>? = null
    protected var bmList: List<Any>? = null

    protected var loadMore: Boolean = false
    protected var offset: Int? = 0
    protected var total: Int? = 0
    protected var last_page: Int? = 0


    /*Search passing text handling */
    open var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    open var text: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            view = this
        } catch (e: Exception) {

        }

    }

    fun showErrorMessage(message: String?) {
        FlashbarUtil.show(activity = activity()!!, message = message!!)

        hideLoading()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        try {
            doOnCreateView(savedInstanceState)

        } catch (e: Exception) {

        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            setupUi()
            setupFont()
            doOnViewCreated()
            setupSwipeRefresh()

        } catch (e: Exception) {

        }

    }

    /**
     * Designed to be overridden in Fragments that implement [HasSwipeRefresh]
     */
    protected open fun onSwipeRefresh() {}

    protected open fun onRetryClicked() {}

    override fun onResume() {
        super.onResume()
        try {
            doOnResume()
        } catch (e: Exception) {

        }
    }

    override fun activity(): BaseActivity<*, *>? {
        return activity as? BaseActivity<*, *>
    }


    override fun baseViewModel(): BaseViewModel? {
        return vm
    }


    private fun setupSwipeRefresh() {
        if (hasSwipeRefresh) {
            if (mSwipeRefresh == null)
                throw IllegalStateException("The fragment implements `HasSwipeRefresh` but no SwipeRefreshLayout found")
            mSwipeRefresh.setOnRefreshListener { this.onSwipeRefresh() }
        } else {
            if (mSwipeRefresh == null) return
            mSwipeRefresh.isEnabled = false
        }
    }

    fun showLoader() {

        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(relLoadingScreen)
    }

    fun showMainLayout() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(main_layout_display)
    }

    fun showServerError() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(linServerError)
    }

    fun showOfflineMode() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(linOfflineScreen)
        btnRetry.setOnClickListener { this.onRetryClicked() }
    }

    fun showEmptyData() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(linEmptyData)
    }
    abstract fun onPermissionGranted(granted: Boolean)

    protected fun grantPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= 23) {
            var permissionAdded = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activity()!!,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionAdded = false
                }
            }
            if (!permissionAdded) {
                requestPermissions(permissions, PERMISSION_CODE)
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
        private val TAG = BaseFrag::class.java.name
        private var PERMISSION_CODE = 123
    }

}