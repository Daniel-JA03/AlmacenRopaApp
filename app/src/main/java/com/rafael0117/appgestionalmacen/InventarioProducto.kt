package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.adaptador.ProductoAdapter
import com.rafael0117.appgestionalmacen.controller.ProductoController
import com.rafael0117.appgestionalmacen.entidad.Producto

class InventarioProducto : AppCompatActivity() {

    private lateinit var edtBuscarProducto: EditText
    private lateinit var btnBuscarProducto: Button
    private lateinit var rgEstadoProducto: RadioGroup
    private lateinit var rbTodos: RadioButton
    private lateinit var rbActivos: RadioButton
    private lateinit var rbInactivos: RadioButton
    private lateinit var rvInventarioProductos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventario_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtBuscarProducto = findViewById(R.id.edtBuscarProducto)
        btnBuscarProducto = findViewById(R.id.btnBuscarProducto)
        rgEstadoProducto = findViewById(R.id.rgEstadoProducto)
        rbTodos = findViewById(R.id.rbTodosEstado)
        rbActivos = findViewById(R.id.rbActivos)
        rbInactivos = findViewById(R.id.rbInactivos)
        rvInventarioProductos = findViewById(R.id.rvInventarioProductos)

        btnBuscarProducto.setOnClickListener {
            cargarProductos()
        }

        rgEstadoProducto.setOnCheckedChangeListener { _, _ ->
            cargarProductos()
        }

        cargarProductos() // carga inicial
    }

    private fun cargarProductos() {
        val textoBusqueda = edtBuscarProducto.text.toString().trim()
        val estadoSeleccionado = when (rgEstadoProducto.checkedRadioButtonId) {
            R.id.rbActivos -> 1
            R.id.rbInactivos -> 0
            else -> -1 // Todos
        }

        Log.d("FiltroProductos", "Texto búsqueda: '$textoBusqueda'")
        Log.d("FiltroProductos", "Estado seleccionado: $estadoSeleccionado")

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

        rvInventarioProductos.adapter = ProductoAdapter(ArrayList(filtrados))
        rvInventarioProductos.layoutManager = LinearLayoutManager(this)
    }
}
