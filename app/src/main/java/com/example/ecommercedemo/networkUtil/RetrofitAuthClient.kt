package com.example.ecommercedemo.networkUtil;


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitAuthClient {

    private const val hostAddress: String = "http://sandbox.emdexapi.com/"
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .connectTimeout(90, TimeUnit.SECONDS)
        .build()

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(hostAddress)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
    private var authApi: AuthApi = retrofit.create(AuthApi::class.java)


    fun getRetrofitAuthClient(): AuthApi {
        return authApi
    }


}