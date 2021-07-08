package com.example.ecommercedemo.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecommercedemo.all_products.ProductModel

@Database(entities = [ProductModel::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun productDao(): ProductDao?

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "roomdbb"
                ).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}