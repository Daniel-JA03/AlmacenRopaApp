package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.rafael0117.appgestionalmacen.adaptador.SedeAdapter
import com.rafael0117.appgestionalmacen.controller.SedeController
import com.rafael0117.appgestionalmacen.entidad.SedeConCant
import com.rafael0117.appgestionalmacen.entidad.Ubicacion
import java.util.ArrayList

class Sedes : AppCompatActivity() {
    private lateinit var btnBuscarPorId: Button
    private lateinit var edtBuscar: TextInputEditText
    private lateinit var rgEstado: RadioGroup
    private lateinit var rbTodas: RadioButton
    private lateinit var rbActivas: RadioButton
    private lateinit var rbDesactivadas: RadioButton
    private lateinit var rvSedes: RecyclerView
    private lateinit var fabAgregarSede: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sedes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = "Sedes"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // inicializar vista
        btnBuscarPorId = findViewById(R.id.btnBuscarPorId)
        edtBuscar = findViewById(R.id.edtBuscar)
        rgEstado = findViewById(R.id.rgEstado)
        rbTodas = findViewById(R.id.rbTodas)
        rbActivas = findViewById(R.id.rbActivas)
        rbDesactivadas = findViewById(R.id.rbDesactivadas)
        rvSedes = findViewById(R.id.rvSedes)
        fabAgregarSede = findViewById(R.id.fabAgregarSede)

        // listeners
        fabAgregarSede.setOnClickListener {
            startActivity(Intent(this, NuevaSede::class.java))
        }

        btnBuscarPorId.setOnClickListener {
            cargarSedes()
        }

        rgEstado.setOnCheckedChangeListener { _, _ ->
            cargarSedes()
        }

        cargarSedes()

    }

    private fun cargarSedes() {
        val textoBusqueda = edtBuscar.text.toString().trim()
        val estadosSeleccionado = when (rgEstado.checkedRadioButtonId) {
            R.id.rbActivas -> 1
            R.id.rbDesactivadas -> 0
            else -> -1 // Todos los item
        }

        //val todas = SedeController().findAll()
        val todas = SedeController().findAllConCant()


        val filtradas = todas.filter {
            val coincideBusqueda = textoBusqueda.isEmpty() ||
                    it.nomDis.contains(textoBusqueda, ignoreCase = true) ||
                    it.codigo.toString() == textoBusqueda

            val coincideEstado = when (estadosSeleccionado) {
                1 -> it.estado == 1
                0 -> it.estado == 0
                else -> true
            }
            coincideBusqueda && coincideEstado
        }

        rvSedes.adapter = SedeAdapter(ArrayList(filtradas)) {
            mostrarDialogoEditarSede(it)
        }

        rvSedes.layoutManager = LinearLayoutManager(this)

    }

    private fun mostrarDialogoEditarSede(sede: SedeConCant) {
        // Contenedor principal del diálogo
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        // Campo para editar el distrito
        val inputDistrito = EditText(this).apply {
            hint = "Nombre del Distrito"
            setText(sede.nomDis)
        }

        // Spinner para seleccionar estado (Activo/Desactivado)
        val spinnerEstado = Spinner(this)
        val opcionesEstado = arrayOf("Activo", "Desactivado")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstado).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEstado.adapter = adapterEstado
        spinnerEstado.setSelection(if (sede.estado == 1) 0 else 1)

        // TextView para mostrar las ubicaciones
        val txtUbicacionesTitle = TextView(this).apply {
            text = "Ubicaciones asociadas"
            textSize = 16f
            setPadding(0, 20, 0, 10)
            setTypeface(null, Typeface.BOLD)
        }

        val ubicaciones = SedeController().obtenerUbicacionesPorSede(sede.codigo)

        // Mostrar cada ubicación con opción de eliminar
        val ubicacionesLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        if (ubicaciones.isEmpty()) {
            val txtSinUbicaciones = TextView(this).apply {
                text = "No hay ubicaciones registradas."
                setTextColor(Color.parseColor("#888888"))
            }
            ubicacionesLayout.addView(txtSinUbicaciones)
        } else {
            for (ubicacion in ubicaciones) {
                val ubicacionContainer = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(0, 8, 0, 8)
                }

                val txtDescripcionUbicacion = TextView(this).apply {
                    text = "${ubicacion.descripcion} (${ubicacion.latitud}, ${ubicacion.longitud})"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    setPadding(0, 0, 16, 0)
                }

                // Botón compartir (solo ícono)
                val btnCompartirUbicacion = ImageButton(this).apply {
                    setImageResource(R.drawable.share_location)
                    setBackgroundColor(Color.TRANSPARENT)
                    setColorFilter(Color.parseColor("#4CAF50")) // verde
                    layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                        marginEnd = 16
                    }
                    setOnClickListener {
                        val mensaje = "Ubicación: ${ubicacion.descripcion}\n" +
                                "Latitud: ${ubicacion.latitud}\n" +
                                "Longitud: ${ubicacion.longitud}\n" +
                                "https://www.google.com/maps/search/?api=1&query=${ubicacion.latitud},${ubicacion.longitud}"

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Ubicación de la sede")
                            putExtra(Intent.EXTRA_TEXT, mensaje)
                        }
                        startActivity(Intent.createChooser(intent, "Compartir ubicación con..."))
                    }
                }


                // Botón editar (solo ícono)
                val btnEditarUbicacion = ImageButton(this).apply {
                    setImageResource(R.drawable.outline_edit_location_alt_24)
                    setBackgroundColor(Color.TRANSPARENT)
                    setColorFilter(Color.parseColor("#1E88E5")) // azul
                    layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                        marginEnd = 16
                    }
                    setOnClickListener {
                        mostrarDialogoEditarUbicacion(ubicacion)
                    }
                }

                val btnEliminarUbicacion = ImageButton(this).apply {
                    setImageResource(R.drawable.delete)
                    setBackgroundColor(Color.TRANSPARENT)
                    setColorFilter(Color.parseColor("#D32F2F")) // rojo
                    layoutParams = LinearLayout.LayoutParams(100, 100)
                    setOnClickListener {
                        AlertDialog.Builder(this@Sedes)
                            .setTitle("Eliminar Ubicación")
                            .setMessage("¿Estás seguro de eliminar esta ubicación?")
                            .setPositiveButton("Sí") { _, _ ->
                                val resultado = SedeController().eliminarUbicacion(ubicacion.codigo)
                                if (resultado > 0) {
                                    Toast.makeText(this@Sedes, "Ubicación eliminada", Toast.LENGTH_SHORT).show()
                                    mostrarDialogoEditarSede(sede)
                                } else {
                                    Toast.makeText(this@Sedes, "Error al eliminar la ubicación", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .setNegativeButton("No", null)
                            .show()
                    }
                }

                ubicacionContainer.addView(txtDescripcionUbicacion)
                ubicacionContainer.addView(btnEditarUbicacion)
                ubicacionContainer.addView(btnEliminarUbicacion)
                ubicacionContainer.addView(btnCompartirUbicacion)

                ubicacionesLayout.addView(ubicacionContainer)
            }
        }

        // Agregar elementos al layout del diálogo
        layout.addView(inputDistrito)
        layout.addView(spinnerEstado)
        layout.addView(txtUbicacionesTitle)
        layout.addView(ubicacionesLayout)

        // Mostrar diálogo
        AlertDialog.Builder(this)
            .setTitle("Editar Sede")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                sede.nomDis = inputDistrito.text.toString()
                sede.estado = if (spinnerEstado.selectedItem == "Activo") 1 else 0

                val salida = SedeController().editarSede(sede)
                if (salida > 0) {
                    Toast.makeText(this, "Sede actualizada", Toast.LENGTH_SHORT).show()
                    cargarSedes()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Eliminar Sede") { _, _ ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar Sede")
                    .setMessage("¿Estás seguro de eliminar esta sede y todas sus ubicaciones?")
                    .setPositiveButton("Sí") { _, _ ->
                        val resultado = SedeController().eliminarSede(sede.codigo)
                        if (resultado > 0) {
                            Toast.makeText(this@Sedes, "Sede eliminada exitosamente", Toast.LENGTH_SHORT).show()
                            cargarSedes()
                        } else {
                            Toast.makeText(this@Sedes, "Error al eliminar la sede", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditarUbicacion(ubicacion: Ubicacion) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val edtLatitud = EditText(this).apply {
            hint = "Latitud"
            setText(ubicacion.latitud.toString())
            //inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val edtLongitud = EditText(this).apply {
            hint = "Longitud"
            setText(ubicacion.longitud.toString())
            //inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val edtDescripcion = EditText(this).apply {
            hint = "Descripción"
            setText(ubicacion.descripcion)
        }

        layout.addView(edtLatitud)
        layout.addView(edtLongitud)
        layout.addView(edtDescripcion)

        AlertDialog.Builder(this)
            .setTitle("Editar Ubicación")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val latitud = edtLatitud.text.toString().toDoubleOrNull()
                val longitud = edtLongitud.text.toString().toDoubleOrNull()
                val descripcion = edtDescripcion.text.toString()

                if (latitud == null || longitud == null || descripcion.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val salida = SedeController().editarUbicacion(
                    ubicacion.codigo,
                    latitud,
                    longitud,
                    descripcion
                )

                if (salida > 0) {
                    Toast.makeText(this, "Ubicación actualizada", Toast.LENGTH_SHORT).show()
                    mostrarDialogoEditarSede(SedeConCant(
                        codigo = ubicacion.sedeCodigo,
                        foto = "",
                        nomDis = "",
                        estado = 1,
                        cantidadSedes = 0
                    )) // Recargar diálogo
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}