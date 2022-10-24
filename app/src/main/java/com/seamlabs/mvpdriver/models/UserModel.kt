package com.seamlabs.mvpdriver.models


import com.google.gson.annotations.SerializedName


data class UserModel(
    @SerializedName("driver")
    val driver: Driver,
    @SerializedName("token")
    val token: String
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
    val updatedAt: String
)