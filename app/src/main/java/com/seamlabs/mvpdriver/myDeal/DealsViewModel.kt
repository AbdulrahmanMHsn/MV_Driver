package com.seamlabs.mvpdriver.myDeal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.Load
import com.seamlabs.mvpdriver.common.utility.Navigate
import com.seamlabs.mvpdriver.common.utility.SomethingWentWrong
import com.seamlabs.mvpdriver.common.utility.StopLoading
import com.seamlabs.mvpdriver.models.ResponseDealsAPIResult
import com.seamlabs.mvpdriver.models.TripModel
import kotlinx.coroutines.launch


class DealsViewModel(app: Application) : BaseViewModel(app) {

    private val dealsMutableLiveData= MutableLiveData<ResponseDealsAPIResult>()
    val dealsLiveData:LiveData<ResponseDealsAPIResult> = dealsMutableLiveData

    var upComingDealsList = emptyList<TripModel>()
    var pastDealsList = emptyList<TripModel>()

    fun getMarketRequests(context: Context) = viewModelScope.launch {
        enqueueSignal(Load)
        try {
            val response = apiServices.getDeals()
            if (response.isSuccessful) {
                enqueueSignal(StopLoading)
                dealsMutableLiveData.postValue(response.body())
                upComingDealsList = response.body()?.deals!!.upcomingDeals
                pastDealsList = response.body()?.deals!!.pastDeals
            } else {
                Log.i("TAGTAGTAG", "getMarketRequests: ${response.message()}")
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        } catch (ex: Throwable) {
            errorMessage = context.getString(R.string.server_error)
            enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
        }
    }


}