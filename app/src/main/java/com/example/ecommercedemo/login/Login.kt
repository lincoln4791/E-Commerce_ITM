package com.example.ecommercedemo.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityLoginBinding
import com.example.ecommercedemo.homepage.HomePage
import com.example.ecommercedemo.worker.LoginWorker
import java.util.*
import kotlin.collections.HashMap

class Login : AppCompatActivity() {
    var email = ""
    var password = ""

    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            validateData()
        }



    }

    private fun validateData() {
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()

        if(email.isEmpty()){
            binding.etEmail.error = "email"
        }
        else if(password.isEmpty()){
            binding.etPassword.error = "Password"
        }
        else{
            login()
        }
    }





    private fun login() {
        var loginModel = LoginModel(email,password)
        var map = HashMap<String,Objects>()

        val data : Data = Data.Builder().putString("email",loginModel.email).putString("pass",loginModel.password).build()


        val workRequest: WorkRequest =
            OneTimeWorkRequestBuilder<LoginWorker>()
                .setInputData(data)
                .build()

        val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueue(workRequest)

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this, Observer {
            if(it.state.isFinished){
                //val returnedData = it.outputData.getStringArray("code")
               // Log.d("tag","data is ${returnedData!![0]} in login")
                Log.d("tag","data is ${SavedData.RESPONSE_LOGIN!!.code()} in login")
                if (SavedData.RESPONSE_LOGIN!!.isSuccessful){
                    if (SavedData.RESPONSE_LOGIN!!.code() == 200){
                        SavedData.token = SavedData.RESPONSE_LOGIN!!.body()!!.success!!.token
                        Log.d("tag","Login Success ${SavedData.token}")
                        startActivity(Intent(this@Login,HomePage::class.java)).apply {  }
                    }

                    else if(SavedData.RESPONSE_LOGIN!!.code() == 401){
                        Toast.makeText(this@Login,"Incorrect user id or password",Toast.LENGTH_LONG).show()
                    }
                }

                else{
                    Toast.makeText(this@Login,"Request Failed ${SavedData.RESPONSE_LOGIN!!.message()}",Toast.LENGTH_LONG).show()
                }





               /* if(returnedData[0]=="200"){
                    SavedData.token = returnedData[1]!!
                    Log.d("tag","Login Success ${SavedData.token}")
                    startActivity(Intent(this@Login,HomePage::class.java)).apply {  }
                }
                else if(returnedData[0].toString().equals("error")){
                    Toast.makeText(this@Login,"Error ${returnedData[1]}",Toast.LENGTH_LONG).show()
                }

                else{
                    Toast.makeText(this@Login,"failed ${returnedData[0]}",Toast.LENGTH_LONG).show()
                }*/

            }

        })


        /*   RetrofitAuthClient.getRetrofitAuthClient().login(loginModel)
               .enqueue(object : Callback<Token>{
                   override fun onResponse(call: Call<Token>, response: Response<Token>) {
                       if(response.code()==200){
                           SavedData.token = response.body()!!.success!!.token
                           Log.d("tag","Login Success ${SavedData.token}")
                           startActivity(Intent(this@Login,HomePage::class.java)).apply {  }
                       }
                       else{
                           Log.d("tag","Login Failed ${response.code()}")
                           Toast.makeText(this@Login,"Wrong Crediential : use rupak@itmedicus.com as username and 1234 as password",Toast.LENGTH_LONG).show()
                       }
                   }

                   override fun onFailure(call: Call<Token>, t: Throwable) {
                       Log.d("tag","Login Failed ${t.message}")
                       Toast.makeText(this@Login,"Error ${t.message}",Toast.LENGTH_LONG).show()
                   }

               })*/
    }



    private fun hideSystemUI() {

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

            actionBar?.hide()
        }

    }



}