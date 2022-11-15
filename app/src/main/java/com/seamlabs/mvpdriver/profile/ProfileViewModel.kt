package com.seamlabs.mvpdriver.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.models.Driver
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : BaseViewModel(app) {


    private val profileMLData = MutableLiveData<Driver>()
    val profileLData: LiveData<Driver> = profileMLData


    fun getProfile(context: Context) {
        enqueueSignal(Load)
        viewModelScope.launch {
            try {
                val response = apiServices.getProfile()
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading)
                    profileMLData.postValue(response.body()?.driver)
                } else {
                    errorMessage = context.getString(R.string.server_error)
                    enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
                }
            } catch (ex: Throwable) {
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }
    }


    fun logout(context: Context) {
        enqueueSignal(Load)
        viewModelScope.launch {
            try {
                val response = apiServices.logout()
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.OnSuccessLogout)
                    UserPreferences.getUserSharedPreferences(context).edit().clear().apply()
                } else {
                    errorMessage = parseErrorToString(response.message())
                    enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
                }
            } catch (ex: Throwable) {
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }
    }


}