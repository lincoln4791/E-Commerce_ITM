package com.example.ecommercedemo.roomDB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommercedemo.all_products.ProductModel
import com.example.ecommercedemo.myCart.MyCartModel


@Database( version = 2, entities = [ProductModel::class,MyCartModel::class],)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun productDao(): ProductDao?
    abstract fun myCartDao(): MyCartDao?



    companion object {

/*        private val migrations = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
            }

        }*/

        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "roomdbb"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as AppDatabase
        }
    }
}