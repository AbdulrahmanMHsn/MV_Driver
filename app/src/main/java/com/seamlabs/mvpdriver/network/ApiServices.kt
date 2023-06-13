package com.seamlabs.mvpdriver.network

import com.seamlabs.mvpdriver.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


//    @POST("complete_company_profile")
//    @FormUrlEncoded
//    suspend fun completeCompanyProfile(
//        @Field("company_name") companyName: String,
//        @Field("email") email: String,
//        @Field("vehicle_count") vehicleCount: Int,
//        @Field("vehicle_type") vehicleType: String,
//        @Field("preferred_area_lat") preferredAreaLat: String,
//        @Field("preferred_area_lng") preferredAreaLng: String,
//        @Field("radius") radius: Int,
//    ): Response<UserModel>

    @Multipart
    @POST("complete_company_profile")
    suspend fun completeCompanyProfile(
        @Part multiPart: Array<MultipartBody.Part>? = null
    ): Response<UserModel>


    @Multipart
    @POST("complete_individual_profile")
    suspend fun completeIndividualProfile(
        @Part multiPart: Array<MultipartBody.Part>? = null,
        @PartMap vehicles: MutableMap<String, RequestBody>,
    ): Response<UserModel>


//    @POST("complete_individual_profile")
//    @FormUrlEncoded
//    suspend fun completeIndividualProfile(
//        @Field("full_name") fullName: String,
//        @Field("email") email: String,
//        @Field("gender") gender: String,
//        @Field("vehicles") vehicles: List<VehicleModel>,
//        @Field("preferred_area_lat") preferredAreaLat: String,
//        @Field("preferred_area_lng") preferredAreaLng: String,
//        @Field("radius") radius: Int,
//    ): Response<UserModel>


    @GET("market")
    suspend fun getMarketRequests(): Response<List<TripModel>>


    @GET("deals")
    suspend fun getDeals(): Response<ResponseDealsAPIResult>


    @POST("trip_request/{id}/submit_offer")
    @FormUrlEncoded
    suspend fun submitOffer(
        @Path("id") tripId:Int,
        @Field("price") price: Int,
        @Field("driver_comment") driverComment:String = "test",
    ): Response<Void>

    @POST("trip_request/{id}/submit_offer")
    @FormUrlEncoded
    suspend fun submitOffer(
        @Path("id") tripId:Int,
        @Field("price") price: Int,
    ): Response<Void>


    @GET("profile")
    suspend fun getProfile(): Response<ProfileResult>


    @POST("logout")
    suspend fun logout(): Response<Void>
}