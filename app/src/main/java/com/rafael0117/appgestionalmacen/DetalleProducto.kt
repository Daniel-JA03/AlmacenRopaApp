package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleProducto : AppCompatActivity() {

    private lateinit var tvCodigo: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvMarca: TextView
    private lateinit var tvModelo: TextView
    private lateinit var tvProveedor: TextView
    private lateinit var tvPrecioCompra: TextView
    private lateinit var tvPrecioVenta: TextView
    private lateinit var tvStockActual: TextView
    private lateinit var tvStockMinimo: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvFechaIngreso: TextView
    private lateinit var tvEstado: TextView
    private lateinit var imgProducto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        tvCodigo = findViewById(R.id.tvCodigo)
        tvNombre = findViewById(R.id.tvNombre)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvMarca = findViewById(R.id.tvMarca)
        tvModelo = findViewById(R.id.tvModelo)
        tvProveedor = findViewById(R.id.tvProveedor)
        tvPrecioCompra = findViewById(R.id.tvPrecioCompra)
        tvPrecioVenta = findViewById(R.id.tvPrecioVenta)
        tvStockActual = findViewById(R.id.tvStockActual)
        tvStockMinimo = findViewById(R.id.tvStockMinimo)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvFechaIngreso = findViewById(R.id.tvFechaIngreso)
        tvEstado = findViewById(R.id.tvEstado)
        imgProducto = findViewById(R.id.imgProducto)

        // Obtener datos del Intent
        val intent = intent
        val codigo = intent.getIntExtra("codigo", 0)
        val nombre = intent.getStringExtra("nombre")
        val descripcion = intent.getStringExtra("descripcion")
        val categoria = intent.getStringExtra("categoria")
        val marca = intent.getStringExtra("marca")
        val modelo = intent.getStringExtra("modelo")
        val proveedor = intent.getStringExtra("proveedor")
        val precioCompra = intent.getDoubleExtra("preciocompra", 0.0)
        val precioVenta = intent.getDoubleExtra("precioventa", 0.0)
        val stockActual = intent.getIntExtra("stockActual", 0)
        val stockMinimo = intent.getIntExtra("stockMinimo", 0)
        val ubicacion = intent.getStringExtra("ubicacion")
        val fechaIngreso = intent.getStringExtra("fechaIngreso")
        val estado = intent.getIntExtra("estado", 0)
        val imagen = intent.getStringExtra("imagen") // nombre de recurso, ej: "camisa01"

        // Asignar a los TextView
        tvCodigo.text = "Código: $codigo"
        tvNombre.text = nombre ?: "Sin nombre"
        tvDescripcion.text = descripcion ?: "Sin descripción"
        tvCategoria.text = "Categoría: $categoria"
        tvMarca.text = "Marca: $marca"
        tvModelo.text = "Modelo: $modelo"
        tvProveedor.text = "Proveedor: $proveedor"
        tvPrecioCompra.text = "Precio Compra: S/ %.2f".format(precioCompra)
        tvPrecioVenta.text = "Precio Venta: S/ %.2f".format(precioVenta)
        tvStockActual.text = "Stock Actual: $stockActual"
        tvStockMinimo.text = "Stock Mínimo: $stockMinimo"
        tvUbicacion.text = "Ubicación: $ubicacion"
        tvFechaIngreso.text = "Fecha Ingreso: $fechaIngreso"
        tvEstado.text = if (estado == 1) "Estado: Activo" else "Estado: Desactivado"

        // Cargar imagen desde drawable por nombre (ej: "camisa01")
        if (!imagen.isNullOrEmpty()) {
            val resourceId = resources.getIdentifier(imagen, "drawable", packageName)
            if (resourceId != 0) {
                imgProducto.setImageResource(resourceId)
            } else {
                imgProducto.setImageResource(R.drawable.baseline_image_24)
            }
        } else {
            imgProducto.setImageResource(R.drawable.baseline_image_24)
        }
    }
}
