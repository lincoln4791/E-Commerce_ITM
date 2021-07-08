package com.example.ecommercedemo.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ecommercedemo.databinding.ActivityRegisterBinding
import com.example.ecommercedemo.login.Login
import com.example.ecommercedemo.networkUtil.RetrofitAuthClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    var name :String = ""
    var email :String = ""
    var password :String = ""
    var cPassword :String = ""

    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener{
            validateData()
        }

        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this,Login::class.java)).apply {  }
        }

    }




    private fun validateData() {
         name = binding.etName.text.toString()
         email = binding.etEmail.text.toString()
         password = binding.etPassword.text.toString()
         cPassword = binding.etCpassword.text.toString()
        if(name.isEmpty()){
            binding.etName.setError("name")
        }
        else if(email.isEmpty()){
            binding.etEmail.setError("email")
        }

        else if (password.isEmpty()){
            binding.etPassword.setError("password")
        }

        else if (password.isEmpty()){
            binding.etCpassword.setError("confirm password")
        }

        else if (!password.equals(cPassword)){
            binding.etCpassword.setError("Password didn't match")
        }

        else{
           registerUser()
        }
    }







    private fun registerUser() {
        Log.d("tag","registering user")
        var registerModel = RegisterModel(email,password,cPassword,name)
        RetrofitAuthClient.getRetrofitAuthClient().registerNewUser(registerModel)
            .enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code()==200){
                        Log.d("tag","Registration Success ${response.code()}")
                        Toast.makeText(this@Register,"Registration Success",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Log.d("tag","Registration Failed ${response.code()}")
                        Toast.makeText(this@Register,"Registration Failed with error ${response.code()}",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("tag","Registration Failed2 ${t.message}")
                    Toast.makeText(this@Register,"Registration Failed with error ${t.message}",Toast.LENGTH_SHORT).show()
                }

            })

    }
}