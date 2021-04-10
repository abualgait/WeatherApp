/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.shared.koin


import androidx.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.weather.app.BuildConfig
import com.weather.app.shared.data.DataManager
import com.weather.app.shared.network.ApiInterface
import com.weather.app.shared.network.ApiRepository
import com.weather.app.shared.network.NetworkHelper
import com.weather.app.shared.rx.SchedulerProvider
import com.weather.app.shared.rx.SchedulerProviderImpl
import com.weather.app.shared.util.BuildUtil
import com.weather.app.shared.util.SharedPref
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {

    single {
        DataManager(
            get(),
            get(),
            get()
        )
    }


    single { ApiRepository(get()) }
    single { NetworkHelper(androidContext()) }
    single { SharedPref(get()) }
    // single { HeadersInterceptor(get()) }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single<SchedulerProvider> { SchedulerProviderImpl() }




    // OkHttpClient
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val builder = OkHttpClient.Builder().apply {

            //addInterceptor(get<HeadersInterceptor>())
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            if (BuildUtil.isDebug()) {
                addInterceptor(loggingInterceptor)
            }
        }

        builder.build()
    }

    // ApiInterface
    single {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiInterface::class.java)
    }




}