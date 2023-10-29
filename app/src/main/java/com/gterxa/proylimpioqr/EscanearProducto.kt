package com.gterxa.proylimpioqr

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.content.pm.PackageManager

class EscanearProducto : AppCompatActivity() {

    private lateinit var idText: TextView
    private lateinit var scaner: Button
    private lateinit var id: EditText
    private lateinit var hora: EditText
    private lateinit var fecha: EditText
    private lateinit var zonaGroup: RadioGroup
    private val CAMERA_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_element)

        idText = findViewById(R.id.id_escaner)
        scaner = findViewById(R.id.scaner)
        id = findViewById(R.id.identifier)
        hora = findViewById(R.id.hora)
        fecha = findViewById(R.id.fecha)
        zonaGroup = findViewById(R.id.rgzona) // Cambiado a rgzona aquí

        scaner.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@EscanearProducto)
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.setPrompt("Scan a QR Code")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.initiateScan()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido. Puedes iniciar el escáner QR.
                } else {
                    Toast.makeText(this, "Se necesita permiso de cámara para escanear", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val scannedId = intentResult.contents
            if (scannedId != null) {
                id.setText(scannedId) // Establece el contenido escaneado como el ID.
            } else {
                Toast.makeText(this, "Error en el escaneo", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun insertar(view: View) { // Supongo que llamarás esta función desde un botón en tu XML con android:onClick="insertar"
        val con = BasedeDatos(this, "Productos", null, 2)
        val baseDatos = con.writableDatabase

        val _id = id.text.toString()
        val fechaYHoraActual = LocalDateTime.now()
        val _fecha = fechaYHoraActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val _hora = fechaYHoraActual.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        val selectedZoneId = zonaGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedZoneId)
        val zonaSeleccionada = selectedRadioButton.text.toString()

        if (_id.isNotEmpty() && _hora.isNotEmpty() && _fecha.isNotEmpty()) {
            val register = ContentValues().apply {
                put("id", _id)
                put("hora", _hora)
                put("fecha", _fecha)
                put("zona", zonaSeleccionada)
            }

            baseDatos.insert("products", null, register)

            id.setText("")
            hora.setText("")
            fecha.setText("")
            zonaGroup.clearCheck()

            Toast.makeText(this, "Se registro correctamente", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "No se ha grabado correctamente", Toast.LENGTH_LONG).show()
        }
    }
}

















