package com.devtides.animals.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.devtides.animals.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    open fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(app)
    }

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivitySharedPreferences(activity: AppCompatActivity): SharedPreferencesHelper {
        return SharedPreferencesHelper(activity)
    }
}

const val CONTEXT_APP = "Application context"
const val CONTEXT_ACTIVITY = "Activity context"

@Qualifier
annotation class TypeOfContext(val type: String)

