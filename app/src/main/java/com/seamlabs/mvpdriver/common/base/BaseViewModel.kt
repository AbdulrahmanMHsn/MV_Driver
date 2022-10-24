package com.seamlabs.mvpdriver.common.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import artifact.signals_bus.SignalsEmitter
import artifact.signals_bus.contract.Emitter
import com.seamlabs.mvpdriver.common.utility.Signal
import com.seamlabs.mvpdriver.network.ApiClient
import com.seamlabs.mvpdriver.network.ApiServices

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), Emitter<Signal> {
    override val signalsEmitter: SignalsEmitter<Signal> = SignalsEmitter(this::class.java.name)
    val apiServices: ApiServices = ApiClient.getClient(app).create(ApiServices::class.java)

    companion object{
        var errorMessage = ""
    }
}