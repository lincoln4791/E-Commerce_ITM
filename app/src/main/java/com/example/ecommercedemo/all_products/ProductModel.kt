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

/*    var id : Int? = null
    var image : String? = null
    var name : String? = null
    var brand : String? = null
    var price : Int? = null
    var THC : String? = null
    var CBC : String? = null
    var Description : String? = null

    constructor(id: Int) : this() {
        this.id = id
    }*/

}