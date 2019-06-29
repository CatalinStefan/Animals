package com.devtides.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devtides.animals.di.AppModule
import com.devtides.animals.di.CONTEXT_APP
import com.devtides.animals.di.DaggerViewModelComponent
import com.devtides.animals.di.TypeOfContext
import com.devtides.animals.model.Animal
import com.devtides.animals.model.AnimalApiService
import com.devtides.animals.model.ApiKey
import com.devtides.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application): AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true): this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharedPreferencesHelper

    private var invalidApiKey = false
    private var injected = false

    fun inject() {
        if(!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        inject()
        loading.value = true
        invalidApiKey = false
        val key = prefs.getApiKey()
        if(key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    fun hardRefresh() {
        inject()
        loading.value = true
        getKey()
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if(key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }

                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if(!invalidApiKey) {
                            invalidApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loading.value = false
                            animals.value = null
                            loadError.value = true
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}