package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import java.util.Date
import java.util.Locale

class NuevoProducto : AppCompatActivity() {
    // Campos de texto
    private lateinit var edtNombre: TextInputEditText
    private lateinit var edtDescripcion: TextInputEditText
    private lateinit var edtMarca: TextInputEditText
    private lateinit var edtModelo: TextInputEditText
    private lateinit var edtPrecioCompra: TextInputEditText
    private lateinit var edtPrecioVenta: TextInputEditText
    private lateinit var edtStock: TextInputEditText
    private lateinit var edtStockMinimo: TextInputEditText
    private lateinit var edtUbicacion: TextInputEditText

    // Spinners
    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerProveedor: Spinner

    // RadioButtons y RadioGroup
    private lateinit var rgEstado: RadioGroup
    private lateinit var rbActivo: RadioButton
    private lateinit var rbInactivo: RadioButton

    // Botón
    private lateinit var btnGuardarProducto: Button
    lateinit var listaCategorias: List<Categoria>
    lateinit var listaProveedores: List<Proveedor>
    private lateinit var imgProducto: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private var uriImagenSeleccionada: Uri? = null
    private val CODIGO_SELECCION_IMAGEN = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            title="Nuevo Producto"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

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

        btnGuardarProducto.setOnClickListener(){
            grabarProducto()
        }
        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, CODIGO_SELECCION_IMAGEN)
        }


        // Lista de ejemplo (debes obtener esto desde tu BD en realidad)
        listaCategorias = CategoriaController().findAll()
        listaProveedores = ProveedorController().findAll()

// Adapter para mostrar nombres, pero guardar códigos
        val adapterCategoria = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias.map { it.nombre })
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCategoria

        val adapterProveedor = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProveedores.map { it.nombre })
        adapterProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProveedor.adapter = adapterProveedor

    }

    fun grabarProducto() {
        val nombre = edtNombre.text.toString().trim()
        val descripcion = edtDescripcion.text.toString().trim()
        val categoria = listaCategorias[spinnerCategoria.selectedItemPosition].codigo
        val marca = edtMarca.text.toString().trim()
        val modelo = edtModelo.text.toString().trim()
        val proveedor = listaProveedores[spinnerProveedor.selectedItemPosition].codigo
        val precioCompra = edtPrecioCompra.text.toString().toDoubleOrNull() ?: -1.0
        val precioVenta = edtPrecioVenta.text.toString().toDoubleOrNull() ?: -1.0
        val stockActual = edtStock.text.toString().toIntOrNull() ?: -1
        val stockMinimo = edtStockMinimo.text.toString().toIntOrNull() ?: -1
        val ubicacion = edtUbicacion.text.toString().trim()
        val estado = if (rbActivo.isChecked) 1 else 0
        val fechaIngreso = obtenerFechaActual()

        Log.d("NuevoProducto", "Datos recopilados: $nombre, $descripcion, $marca, $modelo")

        if (nombre.isBlank() || precioCompra < 0 || precioVenta < 0 || stockActual < 0) {
            Log.e("NuevoProducto", "Validación fallida - campos vacíos o inválidos")
            Toast.makeText(this, "Complete correctamente todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (uriImagenSeleccionada != null) {
            val nombreArchivo = "productos/${System.currentTimeMillis()}.jpg"
            val storageRef = FirebaseStorage.getInstance().getReference(nombreArchivo)
            Log.d("NuevoProducto", "Subiendo imagen a: $nombreArchivo")

            storageRef.putFile(uriImagenSeleccionada!!)
                .addOnSuccessListener {
                    Log.d("NuevoProducto", "Imagen subida con éxito")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        Log.d("NuevoProducto", "URL de imagen: $uri")
                        guardarProductoConImagen(
                            nombre, descripcion, categoria, marca, modelo, proveedor,
                            precioCompra, precioVenta, stockActual, stockMinimo,
                            ubicacion, fechaIngreso, estado, uri.toString()
                        )
                    }.addOnFailureListener { e ->
                        Log.e("NuevoProducto", "Error al obtener URL de imagen: ${e.message}")
                        Toast.makeText(this, "Error al obtener URL de imagen", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("NuevoProducto", "Error al subir imagen: ${e.message}")
                    Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                }

        } else {
            Log.d("NuevoProducto", "No se seleccionó imagen, se guardará sin imagen")
            guardarProductoConImagen(
                nombre, descripcion, categoria, marca, modelo, proveedor,
                precioCompra, precioVenta, stockActual, stockMinimo,
                ubicacion, fechaIngreso, estado, ""
            )
        }
    }


    fun guardarProductoConImagen(
        nombre: String,
        descripcion: String,
        categoria: Int,
        marca: String,
        modelo: String,
        proveedor: Int,
        precioCompra: Double,
        precioVenta: Double,
        stockActual: Int,
        stockMinimo: Int,
        ubicacion: String,
        fechaIngreso: String,
        estado: Int,
        imagen: String
    ) {
        val producto = Producto(
            codigo = 0,
            nombre = nombre,
            descripcion = descripcion,
            categoria = categoria,
            marca = marca,
            modelo = modelo,
            proveedor = proveedor,
            preciocompra = precioCompra,
            precioventa = precioVenta,
            stockActual = stockActual,
            stockMinimo = stockMinimo,
            ubicacion = ubicacion,
            fechaIngreso = fechaIngreso,
            estado = estado,
            imagen = imagen
        )

        Log.d("NuevoProducto", "Producto a guardar: $producto")

        val salida = ProductoController().adicionarProducto(producto)
        if (salida == -1) {
            Log.e("NuevoProducto", "Error al registrar producto en la base de datos")
            Toast.makeText(this, "Error al registrar producto", Toast.LENGTH_LONG).show()
        } else {
            Log.d("NuevoProducto", "Producto registrado correctamente con ID: $salida")
            Toast.makeText(this, "Producto registrado exitosamente", Toast.LENGTH_LONG).show()
            // limpiarCampos() si quieres resetear el formulario
        }
    }




    fun obtenerFechaActual(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
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