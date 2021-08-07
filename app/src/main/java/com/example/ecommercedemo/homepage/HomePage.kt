package com.example.ecommercedemo.homepage

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.All_Products
import com.example.ecommercedemo.all_products.searchProducts.SearchProducts
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityHomePageBinding
import com.example.ecommercedemo.login.Login
import com.example.ecommercedemo.myCart.MyCart
import com.example.ecommercedemo.myCart.MyCartModel
import com.google.android.material.navigation.NavigationView
import com.example.ecommercedemo.worker.LogoutWorker
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
        lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //******************************************* Inits*******************************
        hideSystemUI()
        loadMyCartDataFromSQL()
        print("this is test push")
        print("this is test push2")



        //sqliteHelper = SQLiteHelper(this)
        //saveProductsToSQLiteDatabase()


        //*****************************************OnClickListeners*******************************
        binding.menu.setOnClickListener {
            openCloseNavigationDrawer()
        }

        binding.ivPromotion.setOnClickListener {
            startActivity(Intent(this@HomePage, All_Products::class.java)).apply { }
        }

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(this@HomePage, SearchProducts::class.java)).apply { }
        }

        binding.cvVapes.setOnClickListener {
            startActivity(Intent(this@HomePage, All_Products::class.java)).apply { }
        }

        binding.cvMyCart.setOnClickListener {
            startActivity(Intent(this@HomePage, MyCart::class.java)).apply { }
        }

        binding.cvSearch.setOnClickListener {
            startActivity(Intent(this@HomePage, SearchProducts::class.java)).apply { }
        }

        binding.navView.setNavigationItemSelectedListener(this)

    }



    private fun loadMyCartDataFromSQL() {
        CoroutineScope(Dispatchers.Default).launch {
            Paper.init(this@HomePage)
            if (SavedData.my_cart == null) {
                SavedData.my_cart =
                    Paper.book().read<MutableList<MyCartModel>>(Constants.MY_CART, null)
            }
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


    fun openCloseNavigationDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }





    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_logout) {
            Log.d("tag", "Logout")
            logout()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun logout() {

        var logoutWorkRequest = OneTimeWorkRequestBuilder<LogoutWorker>().build()

        WorkManager.getInstance(this@HomePage).enqueue(logoutWorkRequest)

        WorkManager.getInstance(this@HomePage).getWorkInfoByIdLiveData(logoutWorkRequest.id).observe(this@HomePage,
            Observer {
                if(it.state.isFinished){
                    if(SavedData.RESPONSE_LOGOUT!!.code() == 200){
                        Log.d("tag", "Logout Success ${SavedData.RESPONSE_LOGOUT!!.code()}")
                        startActivity(Intent(this@HomePage, Login::class.java)).apply { }
                    }
                    else{
                        Toast.makeText(this@HomePage,"Logout Failed, try again later",Toast.LENGTH_LONG).show()
                    }
                }
            })

    /*
        RetrofitBaseClient.getRetrofitBaseClient().logout(SavedData.id)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        Log.d("tag", "Logout Success ${response.code()}")
                        startActivity(Intent(this@HomePage, Login::class.java)).apply { }
                    } else {
                        Log.d("tag", "Logout Failed ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("tag", "Logout Failed2 ${t.message}")
                }

            })*/
    }


}