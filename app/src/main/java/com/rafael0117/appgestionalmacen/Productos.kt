package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.rafael0117.appgestionalmacen.adaptador.ProductoAdapter
import com.rafael0117.appgestionalmacen.controller.ProductoController

class Productos : AppCompatActivity() {

    private lateinit var edtBuscarProducto: TextInputEditText
    private lateinit var btnBuscarProducto: Button
    private lateinit var fabAgregarProducto: FloatingActionButton
    private lateinit var rgEstadoProducto: RadioGroup
    private lateinit var rbTodosEstado: RadioButton
    private lateinit var rbActivos: RadioButton
    private lateinit var rbInactivos: RadioButton

    private lateinit var rvProductos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_productos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val actionBar = supportActionBar
        actionBar?.title = "Productos"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        edtBuscarProducto = findViewById(R.id.edtBuscarProducto)
        btnBuscarProducto = findViewById(R.id.btnBuscarProducto)
        fabAgregarProducto = findViewById(R.id.fabAgregarProductos)

        rgEstadoProducto = findViewById(R.id.rgEstadoProducto)
        rbTodosEstado = findViewById(R.id.rbTodosEstado)
        rbActivos = findViewById(R.id.rbActivos)
        rbInactivos = findViewById(R.id.rbInactivos)

        rvProductos = findViewById(R.id.rvProductos)

        fabAgregarProducto.setOnClickListener(){
            startActivity(Intent(this,NuevoProducto::class.java))
        }
        btnBuscarProducto.setOnClickListener {
            cargarProductos()
        }

        rgEstadoProducto.setOnCheckedChangeListener { _, _ ->
            cargarProductos()
        }
        cargarProductos()

    }
    private fun cargarProductos() {
        val textoBusqueda = edtBuscarProducto.text.toString().trim()
        val estadoSeleccionado = when (rgEstadoProducto.checkedRadioButtonId) {
            R.id.rbActivos -> 1
            R.id.rbInactivos -> 0
            else -> -1 // Todos
        }

        val todos = ProductoController().findAll()

        val filtrados = todos.filter {
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

        // Agrega este bloque para mostrar los productos filtrados en el Logcat
        Log.d("Productos", "Productos a mostrar (${filtrados.size}):")
        filtrados.forEach { producto ->
            Log.d("Productos", producto.toString())
        }

        rvProductos.adapter = ProductoAdapter(ArrayList(filtrados)) { producto ->
            val intent = Intent(this, EditarProducto::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
        }
        rvProductos.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}