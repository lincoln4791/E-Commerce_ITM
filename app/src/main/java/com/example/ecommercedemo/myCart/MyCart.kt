package com.example.ecommercedemo.myCart

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercedemo.databinding.ActivityMyCartBinding
import com.example.ecommercedemo.roomDB.AppDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.*

class MyCart : AppCompatActivity() {
    val importFlag = 1
    val exportFlag = 2

    lateinit var productAdapter: AdapterMyCart
    var list: MutableList<MyCartModel>? = null

    lateinit var binding: ActivityMyCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        fetchData()
        //setTotalCost()


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvClearCart.setOnClickListener{
           clearMyCart()
        }


        binding.btnExp.setOnClickListener {
            chectWritePermission(exportFlag)

        }


        binding.btnImport.setOnClickListener {
            chectWritePermission(importFlag)
        }


    }

    private fun chectWritePermission(flag : Int) {
        Dexter.withContext(this@MyCart).withPermission(Manifest.permission
            .WRITE_EXTERNAL_STORAGE).withListener(object : PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                if(flag == importFlag){
                    importDB()
                }
                else if(flag == exportFlag){
                    exportDB()
                }
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@MyCart,"Permission Denied",Toast.LENGTH_LONG).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?,
            ) {
                p1!!.continuePermissionRequest()
            }

        }).onSameThread().check()
    }

    private fun importDB() {
        var databaseName = "roomdbb"
        var inFileName = "mydb.txt"
        var folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var inFile = File(folderName,inFileName)
        var outFilePath = this@MyCart.getDatabasePath(databaseName).toString()
        var fos = FileOutputStream(outFilePath)
        var fis: FileInputStream = FileInputStream(inFile)
        var bufferReader : BufferedReader = BufferedReader(InputStreamReader(fis))

        var lines = bufferReader.readLines()

        for (line in lines){
            fos.write(line.toByteArray())
        }

        fos.close()
        fis.close()
        Toast.makeText(this@MyCart,"Success Import",Toast.LENGTH_LONG).show()
    }


    private fun exportDB() {

        var databaseName = "roomdbb"
        var outFileName = "mydb.txt"
        var folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var outFile = File(folderName,outFileName)
        var fos = FileOutputStream(outFile)
        var inFileName = this@MyCart.getDatabasePath(databaseName).toString()
        var fis: FileInputStream = FileInputStream(inFileName)
        var bufferReader : BufferedReader = BufferedReader(InputStreamReader(fis))

        var lines = bufferReader.readLines()

        for (line in lines){
            fos.write(line.toByteArray())
        }

        fos.close()
        fis.close()
        Toast.makeText(this@MyCart,"Success",Toast.LENGTH_LONG).show()
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
        for (i in 0 until list!!.size) {
            totalCost += (list!![i].price * list!![i].quantity)
        }

        binding.totalPriceValue.text = "$" + totalCost.toString()
    }


    private fun fetchData() {
        CoroutineScope(IO).launch {
            val async: Deferred<MutableList<MyCartModel>> = async {
                AppDatabase.getInstance(this@MyCart).myCartDao()!!.getAll()

            }

            list = async.await()

            launch(Dispatchers.Main) {
                initRecycleView()
                setTotalCost()
            }



        }

    }

    private fun initRecycleView() {
        //Paper.init(this@MyCart)
        productAdapter = AdapterMyCart(this@MyCart, list!!)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }


    private suspend fun setDataInMainThread() {
        withContext(Main) {
            Log.d("tag", "data is : $list")
        }
    }



    private fun clearMyCart(){
        CoroutineScope(IO).launch {
            val launch = async {
                AppDatabase.getInstance(this@MyCart).myCartDao()!!.deleteAll()
            }

            launch.await()
            launch(Dispatchers.Main) {
                fetchData()
            }

        }


    }

}