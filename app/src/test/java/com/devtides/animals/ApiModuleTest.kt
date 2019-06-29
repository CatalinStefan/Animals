package com.devtides.animals

import com.devtides.animals.di.ApiModule
import com.devtides.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {
    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}