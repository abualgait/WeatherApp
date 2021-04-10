/*
 * Created by  Muhammad Sayed  on 1/9/21 9:37 AM
 * Copyright (c) JustClean. All rights reserved.
 */

package com.weather.app.datasource.view


import androidx.lifecycle.Observer
import com.weather.app.datasource.BaseUnitTest
import com.weather.app.shared.data.model.Resource
import com.weather.app.shared.data.model.WeatherResponse
import com.weather.app.shared.network.ApiRepository
import com.weather.app.ui.navhostactivity.post.WeatherFragVm
import io.reactivex.Observable
import org.junit.Test
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

class WeatherViewModelTest : BaseUnitTest() {
    val repository by inject<ApiRepository>()
    private val postViewModel: WeatherFragVm by inject()

    @Mock
    lateinit var listObserver: Observer<Resource<WeatherResponse>>


    @Test
    fun testGetPosts() {

        repository.getCityWeather("cairo").blockingFirst()

        postViewModel.response.observeForever(listObserver)
        postViewModel.getCityWeather("cairo")
        Mockito.verify(listObserver).onChanged(Resource.loading(null))

        val value = postViewModel.response.value ?: error("No value for view myModel")
        assert(value.data?.name == "cairo")
        Mockito.verify(listObserver).onChanged(Resource.loading(null))

    }

    @Test
    fun testGetPostsFaild() {
        postViewModel.response.observeForever(listObserver)
        postViewModel.getCityWeather("cairo")

        Mockito.verify(listObserver).onChanged(Resource.loading(null))


        val error = IllegalStateException("Got an error !")
        Mockito.`when`(repository.getCityWeather("cairo")).thenReturn(Observable.error(error))

        Mockito.verify(listObserver).onChanged(Resource.error(error.message, null))


    }
}