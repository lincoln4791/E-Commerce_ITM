package com.example.ecommercedemo.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.ecommercedemo.R
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityLoginBinding
import com.example.ecommercedemo.homepage.HomePage
import com.example.ecommercedemo.networkUtil.RetrofitAuthClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        RetrofitAuthClient.getRetrofitAuthClient().login(loginModel)
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

            })
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