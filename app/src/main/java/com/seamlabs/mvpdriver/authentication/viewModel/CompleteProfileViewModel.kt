package com.seamlabs.mvpdriver.authentication.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.models.VehicleModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class CompleteProfileViewModel(application: Application) : BaseViewModel(application) {

    private var preferredArea: LatLng? = null

    fun setPreferredArea(latLng: LatLng) {
        preferredArea = latLng
    }

    fun getPreferredArea(): LatLng? {
        return preferredArea
    }


//    fun completeCompanyProfile(
//        context: Context,
//        companyName: String,
//        email: String,
//        vehicleCount: Int,
//        vehicleType: String,
//        lat: String,
//        lng: String,
//        radius: Int = 7,
//    ) {
//        enqueueSignal(Load)
//
//        viewModelScope.launch {
//            try {
//                val response = apiServices.completeCompanyProfile(companyName,
//                    email,
//                    vehicleCount,
//                    vehicleType,
//                    lat,
//                    lng,
//                    radius)
//                if (response.isSuccessful) {
//                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteCompanyProfile)
//                } else {
//                    errorMessage = context.getString(R.string.server_error)
//                    enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
//                }
//            } catch (ex: Throwable) {
//                errorMessage = context.getString(R.string.server_error)
//                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
//            }
//        }
//    }

    fun completeCompanyProfile(
        context: Context,
        companyName: String,
        email: String,
        vehicleCount: Int,
        vehicleType: String,
        lat: String,
        lng: String,
        image: String = "",
        radius: Int = 1000,
    ) {
        enqueueSignal(Load)

        viewModelScope.launch {
            try {
                val response = apiServices.completeCompanyProfile(getImageMultiPartCompany(
                    image, companyName, email, vehicleCount, vehicleType, lat, lng, radius
                ))
                if (response.isSuccessful) {
                    UserPreferences.setLoginState(context, true)
                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteCompanyProfile)
                } else {
                    errorMessage = context.getString(R.string.server_error)
                    enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
                }
            } catch (ex: Throwable) {
//                errorMessage = ex.message.toString()
                errorMessage = context.getString(R.string.server_error)
                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
            }
        }
    }


    private fun getImageMultiPartCompany(
        filePath: String?,
        companyName: String,
        email: String,
        vehicleCount: Int,
        vehicleType: String,
        lat: String,
        lng: String,
        radius: Int ,
    ): Array<MultipartBody.Part>? {

        //pass it like this
        return if (filePath != null) {
            val file = File(filePath)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            // MultipartBody.Part is used to send also the actual file name
            if (filePath.isNotEmpty()) {
                arrayOf(
                    MultipartBody.Part.createFormData("company_name", companyName),
                    MultipartBody.Part.createFormData("email", email),
                    MultipartBody.Part.createFormData("vehicle_count", vehicleCount.toString()),
                    MultipartBody.Part.createFormData("vehicle_type", vehicleType),
                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
                    MultipartBody.Part.createFormData("radius", radius.toString()),
                    MultipartBody.Part.createFormData("profile_image", file.name, requestFile),
                )
            } else {
                arrayOf(
                    MultipartBody.Part.createFormData("company_name", companyName),
                    MultipartBody.Part.createFormData("email", email),
                    MultipartBody.Part.createFormData("vehicle_count", vehicleCount.toString()),
                    MultipartBody.Part.createFormData("vehicle_type", vehicleType),
                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
                    MultipartBody.Part.createFormData("radius", radius.toString()),
                )
            }
        } else {
            null
        }
    }


//    fun completeIndividualProfile(
//        context: Context,
//        fullName: String,
//        email: String,
//        gender: String,
//        listOfVehicles: List<VehicleModel>,
//        lat: String,
//        lng: String,
//        radius: Int = 100000,
//    ) {
//
//        val vehiclesMap: HashMap<String, Any> = HashMap()
//
//        for ((index, item) in listOfVehicles.withIndex()) {
//            vehiclesMap["vehicles[${index}][id]"] = item.id
//            vehiclesMap["vehicles[${index}][vehicle_type]"] = item.vehicleType
//            vehiclesMap["vehicles[${index}][car_brand]"] = item.carBrand
//            vehiclesMap["vehicles[${index}][car_model]"] = item.carModel
//            vehiclesMap["vehicles[${index}][manufacturing_year]"] = item.manufacturingYear
//        }
//
//        enqueueSignal(Load)
//
//        viewModelScope.launch {
//            try {
//                val response = apiServices.completeIndividualProfile(fullName,
//                    email,
//                    gender,
//                    listOfVehicles,
//                    lat,
//                    lng,
//                    radius)
//                if (response.isSuccessful) {
//                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteCompanyProfile)
//                } else {
//                    errorMessage = context.getString(R.string.server_error)
//                    enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
//                }
//            } catch (ex: Throwable) {
//                errorMessage = context.getString(R.string.server_error)
//                enqueueSignal(StopLoading, SomethingWentWrong.ConnectionFailure)
//            }
//        }
//
//    }

    fun completeIndividualProfile(
        context: Context,
        fullName: String,
        email: String,
        gender: String,
        listOfVehicles: List<VehicleModel>,
        lat: String,
        lng: String,
        image: String = "",
        radius: Int = 1000,
    ) {
        enqueueSignal(Load)

        val vehiclesMap: MutableMap<String, RequestBody> = HashMap()


        for ((index, item) in listOfVehicles.withIndex()) {

            vehiclesMap["vehicles[${index}][id]"] = createPartFromString("${item.id + 1}")
            vehiclesMap["vehicles[${index}][vehicle_type]"] = createPartFromString(item.vehicleType!!.toString())
            vehiclesMap["vehicles[${index}][car_brand]"] = createPartFromString(item.carBrand)
            vehiclesMap["vehicles[${index}][car_model]"] = createPartFromString(item.carModel)
            vehiclesMap["vehicles[${index}][manufacturing_year]"] = createPartFromString(item.manufacturingYear)
        }

        viewModelScope.launch {
            try {
                val response = apiServices.completeIndividualProfile(
                    getImageMultiPartIndividual(image, fullName, email, gender, lat, lng, radius),
                    vehiclesMap)
                if (response.isSuccessful) {
                    UserPreferences.setLoginState(context, true)
                    enqueueSignal(StopLoading, Navigate.OnSuccessCompleteIndividualProfile)
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

    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    private fun getImageMultiPartIndividual(
        filePath: String?,
        fullName: String,
        email: String,
        gender: String,
        lat: String,
        lng: String,
        radius: Int = 100000,
    ): Array<MultipartBody.Part>? {

        //pass it like this
        return if (filePath != null) {
            val file = File(filePath)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            // MultipartBody.Part is used to send also the actual file name
            if (filePath.isNotEmpty()) {
                arrayOf(
                    MultipartBody.Part.createFormData("full_name", fullName),
                    MultipartBody.Part.createFormData("email", email),
                    MultipartBody.Part.createFormData("gender", gender),
                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
                    MultipartBody.Part.createFormData("radius", radius.toString()),
                    MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
                )
            } else {
                arrayOf(
                    MultipartBody.Part.createFormData("full_name", fullName),
                    MultipartBody.Part.createFormData("email", email),
                    MultipartBody.Part.createFormData("gender", gender),
                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
                    MultipartBody.Part.createFormData("radius", radius.toString())
                )
            }
        } else {
            null
        }
    }

//    private fun getImageMultiPartVehicles(
//        listOfVehicles: List<VehicleModel>
//    ): Array<MultipartBody.Part>? {
//
//        //pass it like this
//        return if (filePath != null) {
//            val file = File(filePath)
//            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//            // MultipartBody.Part is used to send also the actual file name
//            if (filePath.isNotEmpty()) {
//                arrayOf(
//                    MultipartBody.Part.createFormData("full_name", fullName),
//                    MultipartBody.Part.createFormData("email", email),
//                    MultipartBody.Part.createFormData("gender", gender),
//                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
//                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
//                    MultipartBody.Part.createFormData("radius", radius.toString()),
//                    MultipartBody.Part.createFormData("image", file.name, requestFile)
//                )
//            } else {
//                arrayOf(
//                    MultipartBody.Part.createFormData("full_name", fullName),
//                    MultipartBody.Part.createFormData("email", email),
//                    MultipartBody.Part.createFormData("gender", gender),
//                    MultipartBody.Part.createFormData("preferred_area_lat", lat),
//                    MultipartBody.Part.createFormData("preferred_area_lng", lng),
//                    MultipartBody.Part.createFormData("radius", radius.toString())
//                )
//            }
//        } else {
//            null
//        }
//    }
}