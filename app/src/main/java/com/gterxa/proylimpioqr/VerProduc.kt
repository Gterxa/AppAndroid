package com.gterxa.proylimpioqr

import android.os.Bundle
import android.database.Cursor
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VerProduc : AppCompatActivity() {
    private lateinit var editTextId: EditText
    private lateinit var buttonSearch: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_produc)

        editTextId = findViewById(R.id.identifier)
        buttonSearch = findViewById(R.id.scaner)
        textViewResult = findViewById(R.id.id_escaner)

        buttonSearch.setOnClickListener {
            val id = editTextId.text.toString()
            val result = queryDatabase(id)
            textViewResult.text = result
        }
    }

    private fun queryDatabase(id: String): String {
        val db = BasedeDatos(this, "Productos", null, 2).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM products WHERE id=? ORDER BY id DESC LIMIT 8", arrayOf(id))

        val result = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val zona = cursor.getString(cursor.getColumnIndexOrThrow("zona"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val currentId = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                result.append("ID: $currentId\nZona: $zona\nHora: $hora\nFecha: $fecha\n\n")
            } while (cursor.moveToNext())

            cursor.close()
            return result.toString()
        } else {
            cursor.close()
            return "No se encontraron productos con el ID $id"
        }
    }


}
