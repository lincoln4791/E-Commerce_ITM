package com.example.ecommercedemo.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.login.LoginModel
import com.example.ecommercedemo.login.Token
import com.example.ecommercedemo.networkUtil.RetrofitAuthClient
import retrofit2.Response

class LoginWorker (var appContext: Context, workerParams: WorkerParameters):
Worker(appContext, workerParams) {
    override fun doWork(): Result {
        var returnedData : Data? = null
        val email = inputData.getString("email")
        val pass = inputData.getString("pass")
        val loginModel = LoginModel(email!!, pass!!)
        Log.d("tag",loginModel.email+loginModel.password+Thread.currentThread().name)
        return try {
            SavedData.RESPONSE_LOGIN =   RetrofitAuthClient.getRetrofitAuthClient().login(loginModel).execute()
            Log.d("tag"," worker code is ${(SavedData.RESPONSE_LOGIN as Response<Token>).code()}")
            returnedData = Data.Builder().putStringArray("code", arrayOf(SavedData.RESPONSE_LOGIN!!.code().toString(),
                SavedData.RESPONSE_LOGIN!!.body()!!.success!!.token)).build()
            Result.success(returnedData)

        } catch (e: Exception){
            val data : Data = Data.Builder().putStringArray("code", arrayOf("error",e.message)).build()
            Result.failure(data)
        }


    }

}