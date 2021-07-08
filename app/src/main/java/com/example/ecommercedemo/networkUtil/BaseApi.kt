package com.example.ecommercedemo.networkUtil

import com.example.ecommercedemo.login.LoginModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface BaseApi {
    @POST("api/v1/logout")
    fun logout(@Body id: String) : Call<Void>
}