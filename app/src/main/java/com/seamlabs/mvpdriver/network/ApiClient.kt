package com.seamlabs.mvpdriver.network

import android.content.Context

import com.seamlabs.mvpdriver.common.utility.UserPreferences.getUserLanguage
import com.seamlabs.mvpdriver.common.utility.UserPreferences.getToken
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import android.text.TextUtils
import com.seamlabs.mvpdriver.BuildConfig
import okhttp3.Request
import java.util.concurrent.TimeUnit


object ApiClient {

    private var retrofit: Retrofit? = null
    private const val REQUEST_TIMEOUT = 60
    private var okHttpClient: OkHttpClient? = null


    fun getClient(context: Context): Retrofit {
        initOkHttp(context)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient!!)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }


    private fun initOkHttp(context: Context) {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Language", getUserLanguage(context))

            // Adding Authorization token (API Key)
            // Requests will be denied without API key
            if (!TextUtils.isEmpty(getToken(context))) {
                requestBuilder.addHeader("Authorization", "Bearer " + getToken(context))
            }
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        })
        okHttpClient = httpClient.build()
    }

}