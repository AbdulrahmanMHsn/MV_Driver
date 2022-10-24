package com.seamlabs.mvpdriver.authentication.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.Navigate
import com.seamlabs.mvpdriver.common.utility.SomethingWentWrong
import com.seamlabs.mvpdriver.common.utility.StopLoading
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class AuthViewModel(application: Application) : BaseViewModel(application) {


    fun login(context: Context, countryCode: String, phoneNumber: String, password: String) =
        viewModelScope.launch {
            try {
                val response = apiServices.login(countryCode, phoneNumber, password)
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.SuccessLoginNavigate)
                } else {
                    if (response.code() == 401) {
                        try {
                            val jsonObject = JSONObject(response.errorBody()!!.toString())
                            errorMessage = jsonObject.getString("message")
                            enqueueSignal(StopLoading, SomethingWentWrong.ErrorMessage)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        errorMessage = context.getString(R.string.server_error)
                        enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
                    }
                }
            } catch (ex: Throwable) {
                Log.i("TAGTAGTAG", "login: ${ex.message}")
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }


    fun register(context: Context, countryCode: String, phoneNumber: String, password: String) =
        viewModelScope.launch {
            try {
                val response = apiServices.register(countryCode, phoneNumber, password)
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.SuccessRegisterNavigate)
                } else {
                    if (response.code() == 401) {
                        try {
                            val jsonObject = JSONObject(response.errorBody()!!.toString())
                            errorMessage = jsonObject.getString("message")
                            enqueueSignal(StopLoading, SomethingWentWrong.ErrorMessage)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        errorMessage = context.getString(R.string.server_error)
                        enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
                    }
                }
            } catch (ex: Throwable) {
                Log.i("TAGTAGTAG", "login: ${ex.message}")
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }


}