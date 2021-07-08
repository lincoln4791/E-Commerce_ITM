package com.example.ecommercedemo.myCart

import java.io.Serializable

class MyCartModel (var id:Int ,var image : String, var name : String, var brand :String , var price :Int, var THC : String, var CBC : String, var Description :String , var quantity :Int) : Serializable {
}