package com.example.ecommercedemo.common

import android.media.session.MediaSession
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.login.Token
import com.example.ecommercedemo.myCart.MyCartModel
import retrofit2.Response

class SavedData {
    companion object{
        var token : String? = null
        const val id : String = "200"
        var my_cart : MutableList<MyCartModel>? = mutableListOf()
        var products_list : MutableList<ProductModel>? = mutableListOf()
        var RESPONSE_LOGIN :Response<Token>? = null
        var RESPONSE_LOGOUT :Response<Void>? = null
        var RESPONSE_ADD_TO_MY_CART :Response<Void>? = null
        var RESPONSE_CLEAR_MY_CART :Response<Void>? = null

    }
}