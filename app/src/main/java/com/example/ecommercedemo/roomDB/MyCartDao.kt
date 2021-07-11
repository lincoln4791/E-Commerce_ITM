package com.example.ecommercedemo.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.myCart.MyCartModel

@Dao
interface MyCartDao {
    @Query("SELECT * FROM MyCartModel")
     fun getAll(): MutableList<MyCartModel>

    @Query("UPDATE MyCartModel SET quantity= :amount WHERE id=:id")
    fun updateByID(amount : Int, id: Int)

    @Query("Select * From MyCartModel WHERE id=:id")
    fun loadByID(id: Int) : MutableList<MyCartModel>

    @Query("delete FROM MyCartModel ")
    fun deleteAll()


    @Insert
     fun insertAll(vararg myCartModel: MyCartModel)

    @Delete
    fun delete(user: ProductModel)

    @Query("DELETE FROM MyCartModel where id = :id ")
    fun deleteByID(id : Int )


}