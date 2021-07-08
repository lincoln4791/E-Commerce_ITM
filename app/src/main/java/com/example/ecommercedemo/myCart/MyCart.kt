package com.example.ecommercedemo.myCart

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.AdapterAllProducts
import com.example.ecommercedemo.all_products.ProductData
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.databinding.ActivityMyCartBinding
import io.paperdb.Paper

class MyCart : AppCompatActivity() {
    lateinit var productAdapter : AdapterMyCart
    lateinit var list : MutableList<MyCartModel>

    lateinit var binding : ActivityMyCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        initRecycleView()
        setTotalCost()


        binding.ivBack.setOnClickListener {
            onBackPressed()
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
        for(i in 0 until list.size){
            totalCost += (list[i].price * list[i].quantity)
        }

        binding.totalPriceValue.text = "$"+totalCost.toString()
    }

    private fun initRecycleView() {
        Paper.init(this@MyCart)
        list = Paper.book().read(Constants.MY_CART)
        productAdapter = AdapterMyCart(this@MyCart,list)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }
}