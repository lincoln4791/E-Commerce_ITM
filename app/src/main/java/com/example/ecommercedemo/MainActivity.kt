package com.example.ecommercedemo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.ecommercedemo.databinding.ActivityMainBinding
import com.example.ecommercedemo.login.Login
import com.example.ecommercedemo.register.Register

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("tag","before set content view")

        hideSystemUI()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginn.setOnClickListener{
            startActivity(Intent(this@MainActivity,Login::class.java)).apply {  }
        }

        binding.join.setOnClickListener{
            startActivity(Intent(this@MainActivity,Register::class.java)).apply {  }
        }

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