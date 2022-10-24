package com.seamlabs.mvpdriver.network

import com.seamlabs.mvpdriver.models.ResponseDealsAPIResult
import com.seamlabs.mvpdriver.models.TripModel
import com.seamlabs.mvpdriver.models.UserModel
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("sign_in")
    @FormUrlEncoded
    suspend fun login(
        @Field("country_code") countryCode: String,
        @Field("phone_number") phoneNumber: String,
        @Field("password") password: String,
    ): Response<UserModel>

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("country_code") countryCode: String,
        @Field("phone_number") phoneNumber: String,
        @Field("password") password: String,
    ): Response<UserModel>

    @GET("market")
    suspend fun getMarketRequests(): Response<List<TripModel>>


    @GET("deals")
    suspend fun getDeals(): Response<ResponseDealsAPIResult>


    @POST("trip_request/{id}/submit_offer")
    suspend fun submitOffer(
        @Path("id") tripId:Int,
        @Field("price") price: Int,
        @Field("driver_comment") driverComment:String,
    ): Response<Void>

}