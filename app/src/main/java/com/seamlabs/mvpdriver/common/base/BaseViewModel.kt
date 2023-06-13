package com.seamlabs.mvpdriver.common.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import artifact.signals_bus.SignalsEmitter
import artifact.signals_bus.contract.Emitter
import com.google.gson.Gson
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.utility.Signal
import com.seamlabs.mvpdriver.models.ErrorsModel
import com.seamlabs.mvpdriver.models.Json
import com.seamlabs.mvpdriver.network.ApiClient
import com.seamlabs.mvpdriver.network.ApiServices
import org.json.JSONObject

abstract class BaseViewModel(val app: Application) : AndroidViewModel(app), Emitter<Signal> {

    override val signalsEmitter: SignalsEmitter<Signal> = SignalsEmitter(this::class.java.name)
    val apiServices: ApiServices = ApiClient.getClient(app).create(ApiServices::class.java)

    companion object{
        var errorMessage = ""
    }

    fun parseErrorToString(errorMessage: String): String {
        var error: String? = ""
        return try {
            val gson = Gson()
            val jsonString = JSONObject(errorMessage)
            val errorModel = gson.fromJson(jsonString.toString(), Json::class.java)
            error = ({ errorModel.errors })?.toString() ?: errorModel.message
            error ?: app.getString(R.string.someThing_went_wrong)
        } catch (e: Exception) {
            app.getString(R.string.someThing_went_wrong)
        }
    }

}