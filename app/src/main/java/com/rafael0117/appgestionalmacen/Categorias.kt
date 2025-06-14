package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafael0117.appgestionalmacen.adaptador.CategoriaAdapter
import com.rafael0117.appgestionalmacen.controller.CategoriaController
import com.rafael0117.appgestionalmacen.entidad.Categoria

class Categorias : AppCompatActivity() {
    private lateinit var btnBuscarPorId: Button
    private lateinit var edtBuscar: EditText
    private lateinit var rvProductos: RecyclerView
    private lateinit var fabAgregarCategoria: FloatingActionButton
    private lateinit var rgEstado: RadioGroup
    private lateinit var rbTodas: RadioButton
    private lateinit var rbActivas: RadioButton
    private lateinit var rbDesactivadas: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = "Categorías"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Inicializar vistas
        btnBuscarPorId = findViewById(R.id.btnBuscarPorId)
        edtBuscar = findViewById(R.id.edtBuscar)
        rvProductos = findViewById(R.id.rvProductos)
        fabAgregarCategoria = findViewById(R.id.fabAgregarCategoria)
        rgEstado = findViewById(R.id.rgEstado)
        rbTodas = findViewById(R.id.rbTodas)
        rbActivas = findViewById(R.id.rbActivas)
        rbDesactivadas = findViewById(R.id.rbDesactivadas)

        // Listeners
        fabAgregarCategoria.setOnClickListener {
            startActivity(Intent(this, NuevaCategoria::class.java))
        }

        btnBuscarPorId.setOnClickListener {
            cargarCategorias()
        }

        rgEstado.setOnCheckedChangeListener { _, _ ->
            cargarCategorias()
        }

        cargarCategorias()
    }

    private fun cargarCategorias() {
        val textoBusqueda = edtBuscar.text.toString().trim()
        val estadoSeleccionado = when (rgEstado.checkedRadioButtonId) {
            R.id.rbActivas -> 1
            R.id.rbDesactivadas -> 0
            else -> -1 // Todas
        }

        val todas = CategoriaController().findAll()

        val filtradas = todas.filter {
            val coincideBusqueda = textoBusqueda.isEmpty() ||
                    it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    it.codigo.toString() == textoBusqueda

            val coincideEstado = when (estadoSeleccionado) {
                1 -> it.estado == 1
                0 -> it.estado == 0
                else -> true
            }

            coincideBusqueda && coincideEstado
        }

        rvProductos.adapter = CategoriaAdapter(ArrayList(filtradas)) {
            mostrarDialogoEditarCategoria(it)
        }
        rvProductos.layoutManager = LinearLayoutManager(this)
    }

    private fun mostrarDialogoEditarCategoria(categoria: Categoria) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputNombre = EditText(this).apply {
            hint = "Nombre"
            setText(categoria.nombre)
        }

        val inputDescripcion = EditText(this).apply {
            hint = "Descripción"
            setText(categoria.descripcion)
        }

        val spinnerEstado = Spinner(this)
        val opcionesEstado = arrayOf("Activo", "Desactivado")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstado)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstado
        spinnerEstado.setSelection(if (categoria.estado == 1) 0 else 1)

        layout.addView(inputNombre)
        layout.addView(inputDescripcion)
        layout.addView(spinnerEstado)

        AlertDialog.Builder(this)
            .setTitle("Editar Categoría")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                categoria.nombre = inputNombre.text.toString()
                categoria.descripcion = inputDescripcion.text.toString()
                categoria.estado = if (spinnerEstado.selectedItem == "Activo") 1 else 0

                val salida = CategoriaController().editarCategoria(categoria)
                if (salida > 0) {
                    Toast.makeText(this, "Categoría actualizada", Toast.LENGTH_SHORT).show()
                    cargarCategorias()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Eliminar") { _, _ ->
                val salida = CategoriaController().eliminarCategoria(categoria.codigo)
                if (salida > 0) {
                    Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show()
                    cargarCategorias()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
