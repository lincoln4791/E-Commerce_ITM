package com.example.ecommercedemo.myCart

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.AdapterAllProducts
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.produckDetails.ProductDetails

class AdapterMyCart (var context: Context, var list : MutableList<MyCartModel> ) : RecyclerView.Adapter<AdapterMyCart.MyViewHolder>() {


    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var tv_name = view.findViewById<TextView>(R.id.productname_sampleMyCart)
        var tv_brand = view.findViewById<TextView>(R.id.tv_brand_sampleMyCart)
        var tv_price = view.findViewById<TextView>(R.id.price_sampleMyCart)
        var tv_quantity = view.findViewById<TextView>(R.id.Quantity_sampleMycart)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.sample_my_cart,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_name.text = list[position].name
        holder.tv_brand.text = list[position].brand
        holder.tv_price.text = "$"+list[position].price.toString()
        holder.tv_quantity.text = list[position].quantity.toString()
        
    }

    override fun getItemCount(): Int {
        return list.size
    }

}