package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.rafael0117.appgestionalmacen.controller.CategoriaController
import com.rafael0117.appgestionalmacen.controller.ProductoController
import com.rafael0117.appgestionalmacen.controller.ProveedorController
import com.rafael0117.appgestionalmacen.entidad.Categoria
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.entidad.Proveedor
import com.squareup.picasso.Picasso

class EditarProducto : AppCompatActivity() {

    private lateinit var edtNombre: TextInputEditText
    private lateinit var edtDescripcion: TextInputEditText
    private lateinit var edtMarca: TextInputEditText
    private lateinit var edtModelo: TextInputEditText
    private lateinit var edtPrecioCompra: TextInputEditText
    private lateinit var edtPrecioVenta: TextInputEditText
    private lateinit var edtStock: TextInputEditText
    private lateinit var edtStockMinimo: TextInputEditText
    private lateinit var edtUbicacion: TextInputEditText

    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerProveedor: Spinner

    private lateinit var rgEstado: RadioGroup
    private lateinit var rbActivo: RadioButton
    private lateinit var rbInactivo: RadioButton

    private lateinit var btnGuardarProducto: Button
    private lateinit var imgProducto: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private var uriImagenSeleccionada: Uri? = null

    private val CODIGO_SELECCION_IMAGEN = 1001

    private lateinit var listaCategorias: List<Categoria>
    private lateinit var listaProveedores: List<Proveedor>
    private lateinit var producto: Producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = "Editar Producto"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Inicializar views
        edtNombre = findViewById(R.id.edtNombre)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtMarca = findViewById(R.id.edtMarca)
        edtModelo = findViewById(R.id.edtModelo)
        edtPrecioCompra = findViewById(R.id.edtPrecioCompra)
        edtPrecioVenta = findViewById(R.id.edtPrecioVenta)
        edtStock = findViewById(R.id.edtStock)
        edtStockMinimo = findViewById(R.id.edtStockMinimo)
        edtUbicacion = findViewById(R.id.edtUbicacion)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        spinnerProveedor = findViewById(R.id.spinnerProveedor)
        rgEstado = findViewById(R.id.rgEstado)
        rbActivo = findViewById(R.id.rbActivo)
        rbInactivo = findViewById(R.id.rbInactivo)
        btnGuardarProducto = findViewById(R.id.btnGuardarProducto)
        imgProducto = findViewById(R.id.imgProducto)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)

        // Obtener producto desde intent
        producto = intent.getSerializableExtra("producto") as Producto

        // Cargar categorías y proveedores
        listaCategorias = CategoriaController().findAll()
        listaProveedores = ProveedorController().findAll()

        val adapterCategoria = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias.map { it.nombre })
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCategoria

        val adapterProveedor = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProveedores.map { it.nombre })
        adapterProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProveedor.adapter = adapterProveedor

        // Mostrar datos del producto
        edtNombre.setText(producto.nombre)
        edtDescripcion.setText(producto.descripcion)
        edtMarca.setText(producto.marca)
        edtModelo.setText(producto.modelo)
        edtPrecioCompra.setText(producto.preciocompra.toString())
        edtPrecioVenta.setText(producto.precioventa.toString())
        edtStock.setText(producto.stockActual.toString())
        edtStockMinimo.setText(producto.stockMinimo.toString())
        edtUbicacion.setText(producto.ubicacion)

        rbActivo.isChecked = producto.estado == 1
        rbInactivo.isChecked = producto.estado == 0

        spinnerCategoria.setSelection(listaCategorias.indexOfFirst { it.codigo == producto.categoria })
        spinnerProveedor.setSelection(listaProveedores.indexOfFirst { it.codigo == producto.proveedor })

        if (producto.imagen.isNotEmpty()) {
            Picasso.get().load(producto.imagen).into(imgProducto)
        }

        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, CODIGO_SELECCION_IMAGEN)
        }

        btnGuardarProducto.setText("Actualizar Producto")
        btnGuardarProducto.setOnClickListener {
            mostrarDialogoConfirmacion()
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar actualización")
        builder.setMessage("¿Está seguro que desea actualizar este producto?")
        builder.setPositiveButton("Sí") { dialog, _ ->
            actualizarProducto()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun actualizarProducto() {
        val nombre = edtNombre.text.toString().trim()
        val descripcion = edtDescripcion.text.toString().trim()
        val categoria = listaCategorias[spinnerCategoria.selectedItemPosition].codigo
        val marca = edtMarca.text.toString().trim()
        val modelo = edtModelo.text.toString().trim()
        val proveedor = listaProveedores[spinnerProveedor.selectedItemPosition].codigo
        val precioCompra = edtPrecioCompra.text.toString().toDoubleOrNull() ?: -1.0
        val precioVenta = edtPrecioVenta.text.toString().toDoubleOrNull() ?: -1.0
        val stock = edtStock.text.toString().toIntOrNull() ?: -1
        val stockMin = edtStockMinimo.text.toString().toIntOrNull() ?: -1
        val ubicacion = edtUbicacion.text.toString().trim()
        val estado = if (rbActivo.isChecked) 1 else 0

        if (nombre.isBlank() || precioCompra < 0 || precioVenta < 0 || stock < 0) {
            Toast.makeText(this, "Complete correctamente todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (uriImagenSeleccionada != null) {
            val nombreArchivo = "productos/${System.currentTimeMillis()}.jpg"
            val storageRef = FirebaseStorage.getInstance().getReference(nombreArchivo)

            storageRef.putFile(uriImagenSeleccionada!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        producto.imagen = uri.toString()
                        guardarActualizacion(nombre, descripcion, categoria, marca, modelo, proveedor,
                            precioCompra, precioVenta, stock, stockMin, ubicacion, estado)
                    }
                }
        } else {
            guardarActualizacion(nombre, descripcion, categoria, marca, modelo, proveedor,
                precioCompra, precioVenta, stock, stockMin, ubicacion, estado)
        }
    }

    private fun guardarActualizacion(
        nombre: String, descripcion: String, categoria: Int, marca: String, modelo: String,
        proveedor: Int, precioCompra: Double, precioVenta: Double, stock: Int, stockMin: Int,
        ubicacion: String, estado: Int
    ) {
        producto.nombre = nombre
        producto.descripcion = descripcion
        producto.categoria = categoria
        producto.marca = marca
        producto.modelo = modelo
        producto.proveedor = proveedor
        producto.preciocompra = precioCompra
        producto.precioventa = precioVenta
        producto.stockActual = stock
        producto.stockMinimo = stockMin
        producto.ubicacion = ubicacion
        producto.estado = estado

        val salida = ProductoController().editarProducto(producto)

        if (salida == -1) {
            Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_SELECCION_IMAGEN && resultCode == RESULT_OK && data != null) {
            uriImagenSeleccionada = data.data
            imgProducto.setImageURI(uriImagenSeleccionada)
        }
    }
}