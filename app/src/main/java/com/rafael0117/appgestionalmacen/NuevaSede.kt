package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.rafael0117.appgestionalmacen.controller.SedeController
import com.rafael0117.appgestionalmacen.entidad.Sede

class NuevaSede : AppCompatActivity() {
    private lateinit var txtDistrito: TextInputEditText
    private lateinit var txtLatitud: TextInputEditText
    private lateinit var txtLongitud: TextInputEditText
    private lateinit var txtDescripcion: TextInputEditText
    private lateinit var btnRegistrarSede: Button
    private lateinit var atvSedes: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_sede)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            title="Nueva Sede"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }



        txtDistrito = findViewById(R.id.txtDistrito)
        txtLatitud = findViewById(R.id.txtLatitud)
        txtLongitud = findViewById(R.id.txtLongitud)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        btnRegistrarSede = findViewById(R.id.btnRegistrarSede)
        atvSedes = findViewById(R.id.atvSedes)

        cargarSedes()

        // Escuchar cambios en txtDistrito
        txtDistrito.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    // Si el usuario escribe en distrito, limpiar y bloquear atvSedes
                    atvSedes.setText("")
                    atvSedes.isEnabled = false
                    atvSedes.alpha = 0.5f
                } else {
                    // Si está vacío, reactivar atvSedes
                    atvSedes.isEnabled = true
                    atvSedes.alpha = 1.0f
                }
            }
        })

// Escuchar selección en atvSedes
        atvSedes.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = atvSedes.adapter.getItem(position) as String
            Log.d("NuevaSede", "Sede seleccionada: $selectedItem")

            // Limpiar y bloquear txtDistrito si se selecciona una sede
            txtDistrito.setText("")
            txtDistrito.isEnabled = false
            txtDistrito.isFocusable = false
            txtDistrito.alpha = 0.5f
        }

// Escuchar borrado manual en atvSedes
        atvSedes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    // Reactivar txtDistrito si se limpia la selección
                    txtDistrito.isEnabled = true
                    txtDistrito.isFocusable = true
                    txtDistrito.alpha = 1.0f
                }
            }
        })

        btnRegistrarSede.setOnClickListener {
            grabar()
        }
    }

    private fun cargarSedes() {
        val listaSedes = SedeController().findAll()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
            listaSedes.map { "${it.codigo} - ${it.nomDis}" })
        atvSedes.setAdapter(adapter)
    }

    fun grabar() {
        val inputSede = atvSedes.text.toString().trim()
        val distrito = txtDistrito.text.toString().trim()
        val latitudStr = txtLatitud.text.toString().trim()
        val longitudStr = txtLongitud.text.toString().trim()
        val descripcion = txtDescripcion.text.toString().trim()

        if (inputSede.isEmpty()) {
            // Si no hay sede seleccionada, crear una nueva
            if (distrito.isEmpty() || latitudStr.isEmpty() || longitudStr.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return
            }

            val latitud = latitudStr.toDouble()
            val longitud = longitudStr.toDouble()

            val sedeBean = Sede(
                foto = "localizacion_sedes",
                nomDis = distrito,
                estado = 1
            )

            val sedeCodigo = SedeController().registrarSede(sedeBean)
            SedeController().registrarUbicacion(sedeCodigo.toInt(), latitud, longitud, descripcion)

            Toast.makeText(this, "Sede registrada exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            // Si hay sede seleccionada, solo agregar nueva ubicación
            if (latitudStr.isEmpty() || longitudStr.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Completa los campos de ubicación", Toast.LENGTH_SHORT).show()
                return
            }

            val sedeCodigo = inputSede.split(" - ")[0].toInt()
            val latitud = latitudStr.toDouble()
            val longitud = longitudStr.toDouble()

            SedeController().registrarUbicacion(sedeCodigo, latitud, longitud, descripcion)
            Toast.makeText(this, "Ubicación registrada exitosamente", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}