package com.seamlabs.mvpdriver.models


import com.google.gson.annotations.SerializedName

data class Json(
    @SerializedName("errors")
    val errors: Errors?,
    @SerializedName("message")
    val message: String?
)

data class Errors(
    @SerializedName("phone_number")
    val phoneNumber: List<String?>?
)