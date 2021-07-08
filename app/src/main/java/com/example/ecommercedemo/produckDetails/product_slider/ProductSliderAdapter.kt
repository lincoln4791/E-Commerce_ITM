package com.example.ecommercedemo.produckDetails.product_slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.ecommercedemo.R
import com.smarteist.autoimageslider.SliderViewAdapter

class ProductSliderAdapter (var context : Context, var list : MutableList<ProductSliderModel> ) : SliderViewAdapter<ProductSliderAdapter.MyViewHolder>() {
    class MyViewHolder(view : View) : SliderViewAdapter.ViewHolder(view) {
        var iv_promotion  = itemView.findViewById<ImageView>(R.id.iv_product_slider)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): MyViewHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.slider_homefragment,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder?, position: Int) {
        viewHolder!!.iv_promotion.setImageResource(list[position].image)

        viewHolder.iv_promotion.setOnClickListener {
            Toast.makeText(context,list[position].name,Toast.LENGTH_SHORT).show()
        }
    }
}