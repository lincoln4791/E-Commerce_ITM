package com.example.ecommercedemo.networkUtil;


import com.example.ecommercedemo.login.JWTToken
import com.example.ecommercedemo.login.LoginModel
import com.example.ecommercedemo.login.Token
import com.example.ecommercedemo.register.RegisterModel
import retrofit2.Call
import retrofit2.http.*

interface AuthApi {

    @POST("api/v1/register")
     fun registerNewUser(@Body registerModel: RegisterModel) : Call<Void>

    @POST("api/v1/login")
    fun login(@Body loginModel: LoginModel ) : Call<Token>







}
