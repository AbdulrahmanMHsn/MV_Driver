package com.seamlabs.mvpdriver.models

import com.google.gson.annotations.SerializedName


data class Deals(
    @SerializedName("past_deals")
    val pastDeals: List<TripModel>,
    @SerializedName("upcoming_deals")
    val upcomingDeals: List<TripModel>
)

data class ResponseDealsAPIResult(
    @SerializedName("deals")
    val deals: Deals,
    @SerializedName("past_deals_count")
    val pastDealsCount: Int,
    @SerializedName("upcoming_deals_count")
    val upcomingDealsCount: Int
)