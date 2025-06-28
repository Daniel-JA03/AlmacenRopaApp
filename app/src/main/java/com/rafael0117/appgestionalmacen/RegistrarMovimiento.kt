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
    private lateinit var spinnerProducto: Spinner
    private lateinit var etCantidad: EditText
    private lateinit var spinnerSedeOrigen: Spinner
    private lateinit var spinnerSedeDestino: Spinner
    private lateinit var etObservaciones: EditText
    private lateinit var btnGuardar: Button

    private val movimientoController = MovimientoController()
    private val productoController = ProductoController()
    private val sedeController = SedeController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_movimiento)

        spinnerTipo = findViewById(R.id.spinnerTipo)
        spinnerProducto = findViewById(R.id.spinnerProducto)
        etCantidad = findViewById(R.id.etCantidad)
        spinnerSedeOrigen = findViewById(R.id.spinnerSedeOrigen)
        spinnerSedeDestino = findViewById(R.id.spinnerSedeDestino)
        etObservaciones = findViewById(R.id.etObservaciones)
        btnGuardar = findViewById(R.id.btnGuardarMovimiento)

        // Cargar tipos de movimiento
        val tipos = arrayOf("Entrada", "Salida", "Traslado")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Cargar productos desde la BD
        val productos: ArrayList<Producto> = productoController.findAll()
        spinnerProducto.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, productos)

        // Cargar sedes desde la BD
        val sedes: ArrayList<Sede> = sedeController.findAll()
        spinnerSedeOrigen.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sedes)
        spinnerSedeDestino.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sedes)

        btnGuardar.setOnClickListener {
            val tipo = spinnerTipo.selectedItem?.toString() ?: ""
            val producto = spinnerProducto.selectedItem as? Producto
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0
            val sedeOrigen = spinnerSedeOrigen.selectedItem as? Sede
            val sedeDestino = spinnerSedeDestino.selectedItem as? Sede
            val observaciones = etObservaciones.text.toString()
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (tipo.isBlank() || producto == null || sedeOrigen == null || sedeDestino == null) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cantidad <= 0) {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tipo == "Traslado" && sedeOrigen.codigo == sedeDestino.codigo) {
                Toast.makeText(this, "Sede origen y destino no pueden ser iguales en un traslado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movimiento = Movimiento(
                tipo = tipo,
                fecha = fecha,
                sedeOrigen = sedeOrigen.codigo,
                sedeDestino = sedeDestino.codigo,
                observaciones = observaciones
            )
            val detalle = DetalleMovimiento(
                movimientoCodigo = 0,
                productoCodigo = producto.codigo,
                cantidad = cantidad
            )

            val idMovimiento = movimientoController.registrarMovimiento(movimiento, listOf(detalle))
            if (idMovimiento > 0) {
                Toast.makeText(this, "Movimiento registrado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar movimiento", Toast.LENGTH_SHORT).show()
            }
        }
    }
}