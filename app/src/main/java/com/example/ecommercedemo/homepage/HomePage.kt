package com.example.ecommercedemo.homepage

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.All_Products
import com.example.ecommercedemo.all_products.ProductData
import com.example.ecommercedemo.all_products.searchProducts.SearchProducts
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityHomePageBinding
import com.example.ecommercedemo.login.Login
import com.example.ecommercedemo.myCart.MyCart
import com.example.ecommercedemo.myCart.MyCartModel
import com.google.android.material.navigation.NavigationView
import com.example.ecommercedemo.networkUtil.RetrofitBaseClient
import com.example.ecommercedemo.roomDB.AppDatabase
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
        lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //******************************************* Inits*******************************
        hideSystemUI()
        loadMyCartDataFromSQL()



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

            })
    }


}