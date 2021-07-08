package com.example.ecommercedemo.common

import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.myCart.MyCartModel

class SavedData {
    companion object{
        var token : String? = null
        const val id : String = "200"
        var my_cart : MutableList<MyCartModel>? = mutableListOf()
        var products_list : MutableList<ProductModel>? = mutableListOf()
    }
}