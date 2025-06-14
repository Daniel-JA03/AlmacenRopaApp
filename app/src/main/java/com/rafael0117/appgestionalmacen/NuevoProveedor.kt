package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rafael0117.appgestionalmacen.controller.ProveedorController
import com.rafael0117.appgestionalmacen.entidad.Proveedor

class NuevoProveedor : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtDireccion: EditText
    private lateinit var edtContacto: EditText
    private lateinit var btnGuardarProveedor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_proveedor)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = "Nuevo Proveedor"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Enlazar vistas
        edtNombre = findViewById(R.id.edtNombreProveedor)
        edtTelefono = findViewById(R.id.edtTelefonoProveedor)
        edtCorreo = findViewById(R.id.edtCorreoProveedor)
        edtDireccion = findViewById(R.id.edtDireccionProveedor)
        edtContacto = findViewById(R.id.edtContactoProveedor)
        btnGuardarProveedor = findViewById(R.id.btnGuardarProveedor)

        btnGuardarProveedor.setOnClickListener {
            grabarProveedor()
        }
    }

    private fun grabarProveedor() {
        val nombre = edtNombre.text.toString()
        val telefono = edtTelefono.text.toString()
        val correo = edtCorreo.text.toString()
        val direccion = edtDireccion.text.toString()
        val contacto = edtContacto.text.toString()

        // Validaciones básicas
        if (nombre.isBlank() || telefono.isBlank() || correo.isBlank() ||
            direccion.isBlank() || contacto.isBlank()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        val proveedor = Proveedor(0, nombre, telefono, correo, direccion, contacto, 1)
        val salida = ProveedorController().adicionarProveedor(proveedor)

        if (salida == -1) {
            Toast.makeText(this, "Error en el registro", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Proveedor registrado", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
