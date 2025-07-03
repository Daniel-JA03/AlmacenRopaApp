package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.rafael0117.appgestionalmacen.controller.MovimientoController
import com.rafael0117.appgestionalmacen.controller.ProductoController
import com.rafael0117.appgestionalmacen.controller.SedeController
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.entidad.Movimiento
import com.rafael0117.appgestionalmacen.entidad.DetalleMovimiento
import java.text.SimpleDateFormat
import java.util.*

class RegistrarMovimiento : AppCompatActivity() {

    private lateinit var spinnerTipo: Spinner
    private lateinit var autoProducto: AutoCompleteTextView
    private lateinit var etCantidad: EditText
    private lateinit var spinnerSedeOrigen: Spinner
    private lateinit var spinnerSedeDestino: Spinner
    private lateinit var etObservaciones: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnConfirmar: Button
    private lateinit var listaProductos: ListView

    private val movimientoController = MovimientoController()
    private val productoController = ProductoController()
    private val sedeController = SedeController()

    private lateinit var productos: List<Producto>
    private val listaDetalleMov = ArrayList<DetalleMovimiento>()
    private val listaVisual = ArrayList<String>()
    private lateinit var adaptadorLista: ArrayAdapter<String>

    private var tipoMovimiento = ""
    private var sedeOrigen: Sede? = null
    private var sedeDestino: Sede? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_movimiento)

        // Referencias UI
        spinnerTipo = findViewById(R.id.spinnerTipo)
        autoProducto = findViewById(R.id.autoProducto)
        etCantidad = findViewById(R.id.etCantidad)
        spinnerSedeOrigen = findViewById(R.id.spinnerSedeOrigen)
        spinnerSedeDestino = findViewById(R.id.spinnerSedeDestino)
        etObservaciones = findViewById(R.id.etObservaciones)
        btnAgregar = findViewById(R.id.btnAgregarProducto)
        btnConfirmar = findViewById(R.id.btnConfirmarMovimiento)
        listaProductos = findViewById(R.id.listaProductosSeleccionados)

        // Set up spinners
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Entrada", "Salida", "Traslado"))
        spinnerSedeOrigen.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sedeController.findAll())
        spinnerSedeDestino.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sedeController.findAll())

        // Cargar productos y configurar AutoComplete
        productos = productoController.findAll()
        val nombresProductos = productos.map { "${it.codigo} - ${it.nombre}" }
        autoProducto.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresProductos))

        adaptadorLista = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVisual)
        listaProductos.adapter = adaptadorLista

        btnAgregar.setOnClickListener {
            tipoMovimiento = spinnerTipo.selectedItem.toString()
            sedeOrigen = spinnerSedeOrigen.selectedItem as? Sede
            sedeDestino = spinnerSedeDestino.selectedItem as? Sede

            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0

            val textoProducto = autoProducto.text.toString()

            val producto = productos.find {
                textoProducto.contains(it.codigo.toString()) || textoProducto.contains(it.nombre, ignoreCase = true)
            }

            if (tipoMovimiento.isBlank() || producto == null || sedeOrigen == null || sedeDestino == null) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cantidad <= 0) {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((tipoMovimiento == "Salida" || tipoMovimiento == "Traslado") && cantidad > producto.stockActual) {
                Toast.makeText(this, "Stock insuficiente para el producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tipoMovimiento == "Traslado" && sedeOrigen!!.codigo == sedeDestino!!.codigo) {
                Toast.makeText(this, "Sede origen y destino no pueden ser iguales", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // CORREGIDO: Se pasan los 4 parámetros requeridos
            listaDetalleMov.add(DetalleMovimiento(0, 0, producto.codigo, cantidad))
            listaVisual.add("${producto.nombre} (ID: ${producto.codigo}) - Cant: $cantidad")
            adaptadorLista.notifyDataSetChanged()

            // Limpiar campos
            etCantidad.setText("")
            autoProducto.setText("")
        }

        btnConfirmar.setOnClickListener {
            if (listaDetalleMov.isEmpty()) {
                Toast.makeText(this, "Debe agregar al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val observaciones = etObservaciones.text.toString()

            val movimiento = Movimiento(
                tipo = tipoMovimiento,
                fecha = fecha,
                sedeOrigen = sedeOrigen!!.codigo,
                sedeDestino = sedeDestino!!.codigo,
                observaciones = observaciones
            )

            val idMovimiento = movimientoController.registrarMovimiento(movimiento, listaDetalleMov)

            if (idMovimiento > 0) {
                Toast.makeText(this, "Movimiento registrado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar movimiento", Toast.LENGTH_SHORT).show()
            }
        }
    }
}