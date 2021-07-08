package com.example.ecommercedemo.all_products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class ProductModel(@PrimaryKey() var id:Int,
                   @ColumnInfo(name = "image") var image : String,
                   @ColumnInfo(name = "name")  var name : String,
                   @ColumnInfo(name = "brand") var brand :String,
                   @ColumnInfo(name = "price")  var price :Int,
                   @ColumnInfo(name = "THC") var THC : String,
                   @ColumnInfo(name = "CBC") var CBC : String,
                   @ColumnInfo(name = "Description") var Description :String ) :Serializable {
}