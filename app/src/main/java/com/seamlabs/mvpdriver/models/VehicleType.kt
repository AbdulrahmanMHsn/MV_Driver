package com.seamlabs.mvpdriver.models

enum class VehicleType {
    BUS,
    CAR,
    CARPOOLING
}

data class VehicleTypeModel(
    val id: Int, val type: String, val name: String,
)