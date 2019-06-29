package com.devtides.animals

import android.app.Application
import com.devtides.animals.di.PrefsModule
import com.devtides.animals.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}