package com.gterxa.proylimpioqr

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BasedeDatos(
    context: Context,
    name: String = "Productos",
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 2
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val CREATE_TABLE_PRODUCTS = "CREATE TABLE products(id_root INTEGER PRIMARY KEY,id INTEGER, zona TEXT, hora TEXT, fecha TEXT)"
        private const val DROP_TABLE_PRODUCTS = "DROP TABLE IF EXISTS products"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PRODUCTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE_PRODUCTS)
        onCreate(db)
    }
}
