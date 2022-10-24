package com.seamlabs.mvpdriver.models


import com.google.gson.annotations.SerializedName


data class TripModel(
    @SerializedName("back_trip_time")
    val backTripTime: String?,
    @SerializedName("blueride_satisfaction")
    val blueRideSatisfaction: String?,
    @SerializedName("boys_count")
    val boysCount: Int,
    @SerializedName("cancel_reason")
    val cancelReason: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("custom_end_date")
    val customEndDate: String,
    @SerializedName("custom_start_date")
    val customStartDate: String,
    @SerializedName("driver_type")
    val driverType: String,
    @SerializedName("dropoff_lat")
    val dropOffLat: String,
    @SerializedName("dropoff_lng")
    val dropOffLng: String,
    @SerializedName("girls_count")
    val girlsCount: Int,
    @SerializedName("go_trip_time")
    val goTripTime: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("note")
    val note: String?,
    @SerializedName("offers")
    val offers: List<Offer> = emptyList(),
    @SerializedName("pickup_lat")
    val pickupLat: String,
    @SerializedName("pickup_lng")
    val pickupLng: String,
    @SerializedName("request_satisfaction")
    val requestSatisfaction: String?,
    @SerializedName("requester_id")
    val requesterId: Int,
    @SerializedName("schedule_type")
    val scheduleType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("trip_period")
    val tripPeriod: String,
    @SerializedName("trip_type")
    val tripType: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("vehicle_type")
    val vehicleType: String,
)

data class Offer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("trip_request_id")
    val tripRequestId: Int,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("driver_comment")
    val driverComment: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
)
