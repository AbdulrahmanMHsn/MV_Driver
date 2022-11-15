package com.seamlabs.mvpdriver.models

import com.google.gson.annotations.SerializedName

data class ProfileResult(
    @SerializedName("profile")
    val driver: Driver,
)
