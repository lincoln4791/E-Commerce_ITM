package com.example.ecommercedemo.all_products

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.searchProducts.SearchProducts
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityAllproductsBinding
import com.example.ecommercedemo.myCart.MyCartModel
import com.example.ecommercedemo.roomDB.AppDatabase
import com.example.ecommercedemo.worker.AddToMyCartWorker
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.MutableMap as MutableMap1

class All_Products : AppCompatActivity() {
    lateinit var productAdapter: AdapterAllProducts
    lateinit var list : MutableList<ProductModel>

    lateinit var binding: ActivityAllproductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllproductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        checkIfProducktsAlreadyLoaded()


        binding.etSearch.setOnClickListener {
            startActivity(Intent(this@All_Products, SearchProducts::class.java)).apply { }
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun checkIfProducktsAlreadyLoaded() {
        Paper.init(this@All_Products)
        if(Paper.book().read<Boolean>("isLoaded") == true){
            Log.d("tag","data Exists")
            loadDataFromSQL()
        }
        else{
            Log.d("tag","data Empty")
            saveDataToLocalDatabase()
        }

    }


    private fun initRecycleView() {
        list = ProductData.getProducts()
        productAdapter = AdapterAllProducts(this@All_Products, list)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }
    private fun loadDataFromSQL() {
        CoroutineScope(IO).launch {

            val async = async {
                AppDatabase.getInstance(this@All_Products).productDao()!!.getAll()
            }

            list =  async.await()
            launch (Main) {
                initRecycleView()
            }
        }
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
                saveDataToLocalDatabase(myCartModel)

            }
    }


    private fun saveDataToLocalDatabase(myCartData : MyCartModel) {

        CoroutineScope(Dispatchers.IO).launch {

            val async = async {
                    AppDatabase.getInstance(this@All_Products).myCartDao()!!.loadByID(myCartData.id)
            }
            var existsData = async.await()
            if(existsData.isEmpty()){
                AppDatabase.getInstance(this@All_Products).myCartDao()!!.insertAll(myCartData)
            }
            else{
                myCartData.quantity = myCartData.quantity+existsData[0].quantity
                AppDatabase.getInstance(this@All_Products).myCartDao()!!.updateByID(myCartData.quantity,myCartData.id)
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


    private  fun saveDataToLocalDatabase(){
        CoroutineScope(IO).launch {
            val async = async {
                var client = AppDatabase.getInstance(this@All_Products).productDao()
                var list = ProductData.getProducts()
                for (i in 0 until list.size) {
                    client!!.insertAll(list[i])
                }
                Paper.init(this@All_Products)
                Paper.book().write("isLoaded",true)
            }

            async.await()
            launch(Main) {
                loadDataFromSQL()
            }

        }
    }


}