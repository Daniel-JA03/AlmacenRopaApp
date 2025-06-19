package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.rafael0117.appgestionalmacen.adaptador.SedeAdapter
import com.rafael0117.appgestionalmacen.controller.SedeController
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
            startActivity(Intent(this, null))
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

        rvSedes.layoutManager = LinearLayoutManager(this)
        rvSedes.adapter = SedeAdapter(ArrayList(filtradas)) {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}