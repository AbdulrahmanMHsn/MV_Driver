package com.seamlabs.mvpdriver.authentication.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.Load
import com.seamlabs.mvpdriver.common.utility.Navigate
import com.seamlabs.mvpdriver.common.utility.SomethingWentWrong
import com.seamlabs.mvpdriver.common.utility.StopLoading
import com.seamlabs.mvpdriver.models.VehicleModel
import kotlinx.coroutines.launch


class CompleteProfileViewModel(application: Application) : BaseViewModel(application) {

    private var preferredArea: LatLng? = null

    fun setPreferredArea(latLng: LatLng) {
        preferredArea = latLng
    }

    fun getPreferredArea(): LatLng? {
        return preferredArea
    }


    fun completeCompanyProfile(
        context: Context,
        companyName: String,
        email: String,
        vehicleCount: Int,
        vehicleType: String,
        lat: String,
        lng: String,
        radius: Int = 7,
    ) {
        enqueueSignal(Load)

        viewModelScope.launch {
            try {
                val response = apiServices.completeCompanyProfile(companyName,
                    email,
                    vehicleCount,
                    vehicleType,
                    lat,
                    lng,
                    radius)
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteCompanyProfile)
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

    fun completeIndividualProfile(
        context: Context,
        fullName: String,
        email: String,
        gender: String,
        listOfVehicles: List<VehicleModel>,
        lat: String,
        lng: String,
        radius: Int = 100000,
    ) {

        val vehiclesMap: HashMap<String, Any> = HashMap()

        for ((index, item) in listOfVehicles.withIndex()) {
            vehiclesMap["vehicles[${index}][id]"] = item.id
            vehiclesMap["vehicles[${index}][vehicle_type]"] = item.vehicleType
            vehiclesMap["vehicles[${index}][car_brand]"] = item.carBrand
            vehiclesMap["vehicles[${index}][car_model]"] = item.carModel
            vehiclesMap["vehicles[${index}][manufacturing_year]"] = item.manufacturingYear
        }

        enqueueSignal(Load)

        viewModelScope.launch {
            try {
                val response = apiServices.completeIndividualProfile(fullName,
                    email,
                    gender,
                    listOfVehicles,
                    lat,
                    lng,
                    radius)
                if (response.isSuccessful) {
                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteCompanyProfile)
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
}