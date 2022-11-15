package com.seamlabs.mvpdriver.models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class ErrorsModel(

    @field:SerializedName("message")
    val message: String? = null,

    @Nullable
    @field:SerializedName("errors")
    val errors: ErrorsItem? = null
)

data class ErrorsItem(

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("key")
    val key: String? = null
)
