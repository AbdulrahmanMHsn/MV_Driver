package com.seamlabs.mvpdriver.models


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName


data class UserModel(
    @SerializedName("driver")
    val driver: Driver,
    @SerializedName("token")
    val token: String,
)

data class Driver(
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profileable_id")
    val profileableId: String?,
    @SerializedName("profileable_type")
    val profileableType: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @Nullable
    @SerializedName("profileable")
    val profileable: Profileable,
    @Nullable
    @SerializedName("image")
    val image: String?,
    @Nullable
    @SerializedName("vehicles")
    val vehicles: List<VehicleModel> = emptyList(),
)


data class Profileable(
    @SerializedName("id")
    val id: Int,
    @Nullable
    @SerializedName("company_name")
    val companyName: String,
    @Nullable
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @Nullable
    @SerializedName("gender")
    val gender: String,
    @Nullable
    @SerializedName("vehicle_type")
    val vehicleType: String,
    @Nullable
    @SerializedName("vehicle_count")
    val vehicleCount: String,
    @SerializedName("preferred_area_lat")
    val preferredAreaLat: String,
    @SerializedName("preferred_area_lng")
    val preferredAreaLng: String,
    @SerializedName("radius")
    val radius: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
)

