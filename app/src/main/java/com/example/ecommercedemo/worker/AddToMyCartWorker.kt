package com.example.ecommercedemo.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ecommercedemo.myCart.MyCartModel
import com.example.ecommercedemo.roomDB.AppDatabase
import com.example.ecommercedemo.roomDB.MyCartDao
import java.lang.Exception

class AddToMyCartWorker(var appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        var data = inputData.keyValueMap.getValue("data") as MyCartModel
        Log.d("tag","data is ${data.name}")
        try {



            return Result.success()
        }
        catch (e:Exception){
            return Result.failure()
        }
    }
}