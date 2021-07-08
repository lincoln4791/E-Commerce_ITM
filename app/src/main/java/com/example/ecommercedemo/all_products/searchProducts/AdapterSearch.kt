package com.example.ecommercedemo.all_products.searchProducts

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.AdapterAllProducts
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.produckDetails.ProductDetails

class AdapterSearch(var context : Context, var list : MutableList<ProductModel>) :
    RecyclerView.Adapter<AdapterSearch.MyViewHolder>() , Filterable{
    private var filteredListNew : MutableList<ProductModel> = list

    class MyViewHolder (view: View) :RecyclerView.ViewHolder(view) {
        var tv_name = view.findViewById<TextView>(R.id.productname_sampleAllProducts)
        var tv_brand = view.findViewById<TextView>(R.id.tv_brand_sampleAllProducts)
        var tv_price = view.findViewById<TextView>(R.id.price_sampleAllProducts)
        var iv_image = view.findViewById<ImageView>(R.id.iv_productImage_sampleAllProducts)
        var cv_rootLayput = view.findViewById<CardView>(R.id.cv_rootLayout_sampleAllProducts)
        var iv_addProductsToCart = view.findViewById<ImageView>(R.id.iv_plus_sampleAllProducts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.sample_all_products,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_name.text = filteredListNew[position].name
        holder.tv_brand.text = filteredListNew[position].brand
        holder.tv_price.text = filteredListNew[position].price.toString()

        holder.cv_rootLayput.setOnClickListener {
            val productDetailsIntent = Intent(context, ProductDetails::class.java).apply {
                putExtra(Constants.INTENT_DATA,filteredListNew[position])
            }
            context.startActivity(productDetailsIntent)
        }
    }

    override fun getItemCount(): Int {
       return filteredListNew.size
    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredListNew = list as ArrayList<ProductModel>
                } else {
                    val resultList = ArrayList<ProductModel>()
                    for (row in list) {
                        if (row.name.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    filteredListNew = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredListNew
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredListNew = results?.values as ArrayList<ProductModel>
                notifyDataSetChanged()
            }
        }
    }
}