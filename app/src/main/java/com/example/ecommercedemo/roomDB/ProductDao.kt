package com.example.ecommercedemo.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommercedemo.all_products.ProductModel

@Dao
interface ProductDao {
    @Query("SELECT * FROM ProductModel")
     fun getAll(): MutableList<ProductModel>


    @Query("delete  FROM productmodel")
    fun deleteAll()


    @Insert
     fun insertAll(vararg products: ProductModel)

    @Delete
    fun delete(user: ProductModel)
}