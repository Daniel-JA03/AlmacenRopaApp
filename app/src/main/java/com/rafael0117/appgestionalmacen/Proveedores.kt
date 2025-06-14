package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafael0117.appgestionalmacen.adaptador.ProveedorAdapter
import com.rafael0117.appgestionalmacen.controller.ProveedorController
import com.rafael0117.appgestionalmacen.entidad.Proveedor

class Proveedores : AppCompatActivity() {
    private lateinit var btnBuscarProveedorPorId: Button
    private lateinit var edtBuscarProveedor: EditText
    private lateinit var rvProveedores: RecyclerView
    private lateinit var fabAgregarProveedor: FloatingActionButton
    private lateinit var rgEstado: RadioGroup
    private lateinit var rbTodas: RadioButton
    private lateinit var rbActivas: RadioButton
    private lateinit var rbDesactivadas: RadioButton
    private lateinit var btnMensaje: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = "Proveedores"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        btnBuscarProveedorPorId = findViewById(R.id.btnBuscarProveedor)
        edtBuscarProveedor = findViewById(R.id.edtBuscarProveedor)
        rvProveedores = findViewById(R.id.rvProveedores)
        fabAgregarProveedor = findViewById(R.id.fabAgregarProveedor)
        rgEstado = findViewById(R.id.rgEstadoProveedor)
        rbTodas = findViewById(R.id.rbTodosProveedores)
        rbActivas = findViewById(R.id.rbActivosProveedores)
        rbDesactivadas = findViewById(R.id.rbInactivosProveedores)


        fabAgregarProveedor.setOnClickListener {
            startActivity(Intent(this, NuevoProveedor::class.java))
        }

        btnBuscarProveedorPorId.setOnClickListener {
            cargarProveedores()
        }

        rgEstado.setOnCheckedChangeListener { _, _ ->
            cargarProveedores()
        }

        cargarProveedores()
    }

    private fun cargarProveedores() {
        val textoBusqueda = edtBuscarProveedor.text.toString().trim()
        val estadoSeleccionado = when (rgEstado.checkedRadioButtonId) {
            R.id.rbActivosProveedores -> 1
            R.id.rbInactivosProveedores -> 0
            else -> -1 // Todas
        }

        Log.d("FiltroProveedores", "Texto búsqueda: '$textoBusqueda'")
        Log.d("FiltroProveedores", "Estado seleccionado: $estadoSeleccionado")

        val todos = ProveedorController().findAll()

        // Mostrar todos los proveedores obtenidos
        for (p in todos) {
            Log.d("ProveedorController", "Proveedor -> Código: ${p.codigo}, Nombre: ${p.nombre}, Estado: ${p.estado}")
        }

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

        Log.d("FiltroProveedores", "Cantidad final después de filtro: ${filtrados.size}")

        rvProveedores.adapter = ProveedorAdapter(ArrayList(filtrados)) {
            mostrarDialogoEditarProveedor(it)
        }
        rvProveedores.layoutManager = LinearLayoutManager(this)
    }


    private fun mostrarDialogoEditarProveedor(proveedor: Proveedor) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputNombre = EditText(this).apply {
            hint = "Nombre"
            setText(proveedor.nombre)
        }

        val inputTelefono = EditText(this).apply {
            hint = "Teléfono"
            setText(proveedor.telefono)
        }

        val inputCorreo = EditText(this).apply {
            hint = "Correo"
            setText(proveedor.correo)
        }

        val inputDireccion = EditText(this).apply {
            hint = "Dirección"
            setText(proveedor.direccion)
        }

        val inputContacto = EditText(this).apply {
            hint = "Nombre del contacto"
            setText(proveedor.contacto)
        }

        val spinnerEstado = Spinner(this)
        val opcionesEstado = arrayOf("Activo", "Desactivado")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstado)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstado
        spinnerEstado.setSelection(if (proveedor.estado == 1) 0 else 1)

        layout.apply {
            addView(inputNombre)
            addView(inputTelefono)
            addView(inputCorreo)
            addView(inputDireccion)
            addView(inputContacto)
            addView(spinnerEstado)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Proveedor")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                proveedor.nombre = inputNombre.text.toString()
                proveedor.telefono = inputTelefono.text.toString()
                proveedor.correo = inputCorreo.text.toString()
                proveedor.direccion = inputDireccion.text.toString()
                proveedor.contacto = inputContacto.text.toString()
                proveedor.estado = if (spinnerEstado.selectedItem == "Activo") 1 else 0

                val salida = ProveedorController().editarProveedor(proveedor)
                if (salida > 0) {
                    Toast.makeText(this, "Proveedor actualizado", Toast.LENGTH_SHORT).show()
                    cargarProveedores()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Eliminar") { _, _ ->
                val salida = ProveedorController().eliminarProveedor(proveedor.codigo)
                if (salida > 0) {
                    Toast.makeText(this, "Proveedor eliminado", Toast.LENGTH_SHORT).show()
                    cargarProveedores()
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
