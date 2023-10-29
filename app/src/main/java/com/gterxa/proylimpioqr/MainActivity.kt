package com.gterxa.proylimpioqr

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        val btnScan: Button = findViewById(R.id.scanprod)
        val btnVer: Button = findViewById(R.id.prodregistrado)
        val btnDownload: Button = findViewById((R.id.download))

        btnScan.setOnClickListener {
            val intent = Intent(this, EscanearProducto::class.java)
            startActivity(intent)
        }

        btnVer.setOnClickListener {
            val intent2 = Intent(this, VerProduc::class.java)
            startActivity(intent2)
        }

        btnDownload.setOnClickListener {
            descargar()
        }

    }
    private fun descargar(){
        val dbHelper = BasedeDatos(this, "Productos", null, 2)
        val db = dbHelper.readableDatabase

        val cursor = db.query("products", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            val csvFile = StringBuilder()

            val columnNames = cursor.columnNames
            csvFile.append(columnNames.joinToString(",")).append("\n")

            do {
                val rowData = mutableListOf<String>()
                for (columnName in columnNames) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    rowData.add(value)
                }
                csvFile.append(rowData.joinToString(",")).append("\n")
            } while (cursor.moveToNext())

            cursor.close()
            db.close()

            val csvFileName = "productos.csv"
            val csvFilePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), csvFileName)

            val csvOutputStream = FileOutputStream(csvFilePath)
            csvOutputStream.write(csvFile.toString().toByteArray())
            csvOutputStream.close()

            Toast.makeText(this, "Archivo CSV guardado en: ${csvFilePath.absolutePath}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "La base de datos está vacía.", Toast.LENGTH_SHORT).show()
        }
    }

}
