package com.example.ecommercedemo.all_products

class ProductData {


    companion object{
        fun getProducts() : MutableList<ProductModel>{
            lateinit var productList : MutableList<ProductModel>
            var product1 = ProductModel(1,"","America","brand1",11,"1%","1%","I am the demo description of product1")
            var product2 = ProductModel(2,"","Banladesh2","brand2",12,"2%","2%","I am the demo description of product2")
            var product3 = ProductModel(3,"","Canada3","brand3",13,"3%","3%","I am the demo description of product3")
            var product4 = ProductModel(4,"","Denmark4","brand4",14,"4%","4%","I am the demo description of product4")
            var product5 = ProductModel(5,"","Finland5","brand5",15,"5%","5%","I am the demo description of product5")
            var product6 = ProductModel(6,"","Ghana6","brand6",16,"6%","6%","I am the demo description of product6")
            var product7 = ProductModel(7,"","Hungery7","brand7",17,"7%","7%","I am the demo description of product7")
            var product8 = ProductModel(8,"","India8","brand8",18,"8%","8%","I am the demo description of product8")
            var product9 = ProductModel(9,"","Japan9","brand9",19,"9%","9%","I am the demo description of product9")
            var product10 = ProductModel(10,"","Kwet10","brand10",20,"10%","10%","I am the demo description of product10")
            productList = mutableListOf(product1,product2,product3,product4,product5,product6,product7,product8,product9,product10)
            return productList
        }
    }


}