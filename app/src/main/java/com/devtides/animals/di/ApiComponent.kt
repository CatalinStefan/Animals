package com.devtides.animals.di

import com.devtides.animals.model.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: AnimalApiService)
}