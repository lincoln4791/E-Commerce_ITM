package com.example.ecommercedemo.register

import java.io.Serializable

class RegisterModel(var email: String, var password : String, var c_password : String , var name : String) : Serializable {
}