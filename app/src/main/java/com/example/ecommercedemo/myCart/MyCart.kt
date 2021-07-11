package com.example.ecommercedemo.myCart

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.AdapterAllProducts
import com.example.ecommercedemo.all_products.ProductData
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.databinding.ActivityMyCartBinding
import com.example.ecommercedemo.roomDB.AppDatabase
import io.paperdb.Paper
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MyCart : AppCompatActivity() {
    lateinit var productAdapter: AdapterMyCart
    var list: MutableList<MyCartModel>? = null

    lateinit var binding: ActivityMyCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        fetchData()
        //setTotalCost()


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvClearCart.setOnClickListener{
           clearMyCart()
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


    private fun setTotalCost() {
        var totalCost = 0
        for (i in 0 until list!!.size) {
            totalCost += (list!![i].price * list!![i].quantity)
        }

        binding.totalPriceValue.text = "$" + totalCost.toString()
    }


    private fun fetchData() {
        CoroutineScope(IO).launch {
            val async: Deferred<MutableList<MyCartModel>> = async {
                AppDatabase.getInstance(this@MyCart).myCartDao()!!.getAll()

            }

            list = async.await()

            launch(Dispatchers.Main) {
                initRecycleView()
                setTotalCost()
            }



        }

    }

    private fun initRecycleView() {
        //Paper.init(this@MyCart)
        productAdapter = AdapterMyCart(this@MyCart, list!!)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }


    private suspend fun setDataInMainThread() {
        withContext(Main) {
            Log.d("tag", "data is : $list")
        }
    }



    private fun clearMyCart(){
        CoroutineScope(IO).launch {
            val launch = async {
                AppDatabase.getInstance(this@MyCart).myCartDao()!!.deleteAll()
            }

            launch.await()
            launch(Dispatchers.Main) {
                fetchData()
            }

        }


    }

}