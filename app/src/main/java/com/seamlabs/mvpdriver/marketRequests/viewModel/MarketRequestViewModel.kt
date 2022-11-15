package com.seamlabs.mvpdriver.marketRequests.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.Load
import com.seamlabs.mvpdriver.common.utility.Navigate
import com.seamlabs.mvpdriver.common.utility.SomethingWentWrong
import com.seamlabs.mvpdriver.common.utility.StopLoading
import com.seamlabs.mvpdriver.models.TripModel
import kotlinx.coroutines.launch


class MarketRequestViewModel(app: Application) : BaseViewModel(app) {

    private val marketRequestsMutableLiveData= MutableLiveData<List<TripModel>>()
    val marketRequestsLiveData:LiveData<List<TripModel>> = marketRequestsMutableLiveData


    fun getMarketRequests(context: Context) = viewModelScope.launch {
        enqueueSignal(Load)
        try {
            val response = apiServices.getMarketRequests()
            if (response.isSuccessful) {
                enqueueSignal(StopLoading)
                marketRequestsMutableLiveData.postValue(response.body())
            } else {
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        } catch (ex: Throwable) {
            errorMessage = context.getString(R.string.server_error)
            enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
        }
    }


    fun submitOffer(context: Context,tripId:Int,price:Int,driverComment:String) = viewModelScope.launch {
        enqueueSignal(Load)
        try {
            val response = apiServices.submitOffer(tripId, price, driverComment)
            if (response.isSuccessful) {
                enqueueSignal(StopLoading, Navigate.OnSuccessSubmitOffer)
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