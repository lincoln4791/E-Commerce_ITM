package com.example.ecommercedemo.produckDetails

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.ecommercedemo.R
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.common.Constants
import com.example.ecommercedemo.common.SavedData
import com.example.ecommercedemo.databinding.ActivityProductDetailsBinding
import com.example.ecommercedemo.myCart.MyCartModel
import com.example.ecommercedemo.produckDetails.product_slider.ProductSliderAdapter
import com.example.ecommercedemo.produckDetails.product_slider.ProductSliderModel
import com.example.ecommercedemo.roomDB.AppDatabase
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.random.Random

class ProductDetails : AppCompatActivity() {
    lateinit var productDetails : ProductModel
    lateinit var imagesList : MutableList<ProductSliderModel>
    lateinit var productSliderAdapter : ProductSliderAdapter

    var orderCount = 1


    lateinit var binding : ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        getAndSetDataFromIntent()
        startSlider()



        binding.ivPlus.setOnClickListener {
            orderCount++
            binding.tvQuantity.text = orderCount.toString()
            binding.tvTotalPrice.text = (productDetails.price*orderCount).toString()
        }

        binding.ivMinus.setOnClickListener {
            orderCount--
            binding.tvQuantity.text = orderCount.toString()
            binding.tvTotalPrice.text = (productDetails.price*orderCount).toString()
        }


        binding.btnAddToCart.setOnClickListener {
            addProductToCard(productDetails)
        }

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





    private fun getAndSetDataFromIntent() {

        productDetails = intent.getSerializableExtra(Constants.INTENT_DATA) as ProductModel

        binding.productName.text = productDetails.name
        binding.tvBrand.text = productDetails.brand
        binding.tvPrice.text = "$"+productDetails.price.toString()
        binding.tvProductDescription.text = productDetails.Description
        binding.tvThcValue.text = productDetails.THC
        binding.tvCbdValue.text = productDetails.CBC
        binding.tvTotalPrice.text = productDetails.price.toString()

    }









    private fun startSlider() {
        var imageModel1 = ProductSliderModel(R.drawable.product,"First")
        var imageModel2 = ProductSliderModel(R.drawable.product2,"Second")
        var imageModel3 = ProductSliderModel(R.drawable.product3,"Third")
        var imageModel4 = ProductSliderModel(R.drawable.product4,"Fourth")

        imagesList = mutableListOf(imageModel1,imageModel2,imageModel3,imageModel4)
        productSliderAdapter = ProductSliderAdapter(this,imagesList)
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.sliderView.startAutoCycle();
        binding.sliderView.scrollTimeInSec = 4
        binding.sliderView.setSliderAdapter(productSliderAdapter)

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
                AppDatabase.getInstance(this@ProductDetails).myCartDao()!!.loadByID(myCartData.id)
            }
            var existsData = async.await()
            if(existsData.isEmpty()){
                AppDatabase.getInstance(this@ProductDetails).myCartDao()!!.insertAll(myCartData)
            }
            else{
                myCartData.quantity = myCartData.quantity+existsData[0].quantity
                AppDatabase.getInstance(this@ProductDetails).myCartDao()!!.updateByID(myCartData.quantity,myCartData.id)
            }


        }
    }


}