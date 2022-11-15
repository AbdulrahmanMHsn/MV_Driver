package com.seamlabs.mvpdriver.authentication.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException


class AuthViewModel(application: Application) : BaseViewModel(application) {


    fun login(context: Context, countryCode: String, phoneNumber: String, password: String) =
        viewModelScope.launch {
            enqueueSignal(Load)
            try {
                val response = apiServices.login(countryCode, phoneNumber, password)

                if (response.isSuccessful) {
                    if (response.body()!!.driver.status != "UNCOMPLETED") {
                        UserPreferences.setLoginState(context, true)
                        UserPreferences.setLoginState(context, true)
                        enqueueSignal(StopLoading, Navigate.SuccessLoginNavigate)
                    } else {
                        enqueueSignal(StopLoading, Navigate.ProfileUncompleted)
                    }
                    UserPreferences.saveUserProfile(context,response.body()!!.driver)
                    UserPreferences.storeToken(context, response.body()!!.token)
                } else {
                    if (response.code() == 401) {
                        try {
                            enqueueSignal(StopLoading, SomethingWentWrong.BadCredentials,SomethingWentWrong.ConnectionFailure)
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
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }


    fun register(context: Context, countryCode: String, phoneNumber: String, password: String) =
        viewModelScope.launch {
            enqueueSignal(Load)
            try {
                val response = apiServices.register(countryCode, phoneNumber, password)
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.SuccessRegisterNavigate)
                } else {
                    if (response.code() == 422) {
                        try {
                            errorMessage = parseErrorToString(response.message())
                            enqueueSignal(StopLoading,SomethingWentWrong.ConnectionFailure)
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
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }


}