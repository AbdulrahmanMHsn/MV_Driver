package com.seamlabs.mvpdriver.models

import com.google.gson.annotations.SerializedName

data class VehicleModel(
    @SerializedName("id")
    val id:Int,
    @SerializedName("vehicle_type")
    val vehicleType:String,
    @SerializedName("car_brand")
    val carBrand:String,
    @SerializedName("car_model")
    val carModel:String,
    @SerializedName("manufacturing_year")
    val manufacturingYear:String,
    @SerializedName("driver_id")
    val driverId:Int = -1,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
)