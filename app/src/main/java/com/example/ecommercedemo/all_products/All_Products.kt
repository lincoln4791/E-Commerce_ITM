package com.example.ecommercedemo.all_products

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.searchProducts.SearchProducts
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityAllproductsBinding
import com.example.ecommercedemo.myCart.MyCartModel
import com.example.ecommercedemo.roomDB.AppDatabase
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class All_Products : AppCompatActivity() {
    lateinit var productAdapter: AdapterAllProducts
    lateinit var list : MutableList<ProductModel>

    lateinit var binding: ActivityAllproductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllproductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        initRecycleView()

        //loadDataFromRoomBD()



        binding.etSearch.setOnClickListener {
            startActivity(Intent(this@All_Products, SearchProducts::class.java)).apply { }
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


    }



    private fun initRecycleView() {
        list = ProductData.getProducts()
        productAdapter = AdapterAllProducts(this@All_Products, list)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }


    fun addProductToCard(productModel: ProductModel) {
        var orderQuantity: Int = 1
        val dialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialogue_add_to_cart, null)
        var tv_quantity = view.findViewById<TextView>(R.id.tv_quantity_dialogue_add_to_cart)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<View>(R.id.iv_plus__dialogue_add_to_cart)
            .setOnClickListener {
                orderQuantity++
                tv_quantity.text = orderQuantity.toString()

            }

        view.findViewById<View>(R.id.iv_minus_dialogue_add_to_cart)
            .setOnClickListener {
                orderQuantity--
                tv_quantity.text = orderQuantity.toString()
            }

        view.findViewById<View>(R.id.btn_add_dialogue_add_to_cart)
            .setOnClickListener {
                val myCartModel: MyCartModel = MyCartModel(
                    productModel.id,
                    productModel.image,
                    productModel.name,
                    productModel.brand,
                    productModel.price,
                    productModel.THC,
                    productModel.CBC,
                    productModel.Description,
                    orderQuantity
                )
                SavedData.my_cart!!.add(myCartModel)
                dialog.dismiss()
                Toast.makeText(this, "Product Added To Cart", Toast.LENGTH_SHORT).show()
                saveDataToLocalDatabase()

            }
    }


    private fun saveDataToLocalDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            Paper.init(this@All_Products)
            Paper.book().write(Constants.MY_CART, SavedData.my_cart)
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