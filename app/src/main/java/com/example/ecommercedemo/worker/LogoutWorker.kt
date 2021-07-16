package com.example.ecommercedemo.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.login.Login
import com.example.ecommercedemo.networkUtil.RetrofitBaseClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutWorker (var appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        return try {
            SavedData.RESPONSE_LOGOUT = RetrofitBaseClient.getRetrofitBaseClient().logout(SavedData.id)
                .execute()
            Result.success()
        } catch (e : Exception){
            Result.failure()
        }
    }
}