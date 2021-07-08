package com.example.ecommercedemo.all_products.searchProducts

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercedemo.all_products.ProductData
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.databinding.ActivitySearchProductsBinding

class SearchProducts : AppCompatActivity() {
    lateinit var productAdapter : AdapterSearch
    lateinit var list : MutableList<ProductModel>

    lateinit var binding : ActivitySearchProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        loadALlProducts()
        textChangeListenerForSearchEditText()



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



    private fun loadALlProducts() {
        list = ProductData.getProducts()
        productAdapter = AdapterSearch(this,list)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }



    private fun textChangeListenerForSearchEditText() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                productAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}