package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafael0117.appgestionalmacen.adaptador.MovimientoAdapter
import com.rafael0117.appgestionalmacen.controller.MovimientoController
import androidx.appcompat.app.AlertDialog

class Movimiento : AppCompatActivity() {
    private lateinit var recyclerViewMovimientos: RecyclerView
    private lateinit var fabAgregarMovimiento: FloatingActionButton
    private lateinit var edtBuscarMov: EditText
    private lateinit var btnBuscarMov: Button

    private lateinit var movimientoController: MovimientoController
    private lateinit var listaMovimientos: List<com.rafael0117.appgestionalmacen.entidad.Movimiento>
    private lateinit var adapter: MovimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movimiento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            title = "Movimiento Producto"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        // Declaración y enlace de variables al estilo de NuevaCategoria
        recyclerViewMovimientos = findViewById(R.id.recyclerViewMovimientos)
        fabAgregarMovimiento = findViewById(R.id.fabAgregarMovimiento)
        edtBuscarMov = findViewById(R.id.edtBuscarMov)
        btnBuscarMov = findViewById(R.id.btnBuscarMov)

        movimientoController = MovimientoController()
        listaMovimientos = movimientoController.listarMovimientos()
        recyclerViewMovimientos.layoutManager = LinearLayoutManager(this)
        adapter = MovimientoAdapter(listaMovimientos) { movimiento ->
            mostrarDetallesDeMovimiento(movimiento.codigo)
        }
        recyclerViewMovimientos.adapter = adapter

        fabAgregarMovimiento.setOnClickListener {
            val intent = Intent(this, RegistrarMovimiento::class.java)
            startActivity(intent)
        }
        btnBuscarMov.setOnClickListener {
            buscarMovimiento()
        }
    }

    private fun buscarMovimiento() {
        val query = edtBuscarMov.text.toString().trim()
        val resultados = if (query.isEmpty()) {
            movimientoController.listarMovimientos()
        } else {
            movimientoController.listarMovimientos().filter {
                it.codigo.toString().contains(query, ignoreCase = true) ||
                        it.tipo.contains(query, ignoreCase = true)
            }
        }
        adapter = MovimientoAdapter(resultados) { movimiento ->
            mostrarDetallesDeMovimiento(movimiento.codigo)
        }
        recyclerViewMovimientos.adapter = adapter
    }

    private fun mostrarDetallesDeMovimiento(movimientoId: Int) {
        val detalles = movimientoController.listarDetallesDeMovimiento(movimientoId)
        if (detalles.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Detalles de Movimiento")
                .setMessage("Sin detalles para este movimiento")
                .setPositiveButton("Cerrar", null)
                .show()
            return
        }
        val detalleTexto = detalles.joinToString("\n") {
            "Producto: ${it.productoCodigo} - Cantidad: ${it.cantidad}"
        }
        AlertDialog.Builder(this)
            .setTitle("Detalles de Movimiento")
            .setMessage(detalleTexto)
            .setPositiveButton("Cerrar", null)
            .show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}