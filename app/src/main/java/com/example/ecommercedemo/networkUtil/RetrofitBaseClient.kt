package com.example.ecommercedemo.networkUtil


import com.example.ecommercedemo.common.SavedData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBaseClient {

    // companion object{
    private const val hostAddress = "http://sandbox.emdexapi.com/"
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .connectTimeout(90, TimeUnit.SECONDS)
        .addInterceptor(MyInterceptor())
        .build()
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(hostAddress)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
    private var baseApi: BaseApi = retrofit!!.create(BaseApi::class.java)


    fun getRetrofitBaseClient(): BaseApi {
        return baseApi
    }
    //  }

}


class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder().header("Accept","application/json").header("Authorization","Bearer "+
                SavedData.token!!
            )
                .build()
        return chain.proceed(request)
    }

}