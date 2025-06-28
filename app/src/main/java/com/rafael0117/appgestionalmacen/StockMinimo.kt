package com.rafael0117.appgestionalmacen

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.adaptador.StockMinimoAdapter
import com.rafael0117.appgestionalmacen.controller.ProductoController
import com.rafael0117.appgestionalmacen.controller.ProveedorController
import com.rafael0117.appgestionalmacen.entidad.Producto
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class StockMinimo : AppCompatActivity() {

    private lateinit var spinnerProveedor: Spinner
    private lateinit var rvStockMinimo: RecyclerView
    private lateinit var btnGenerarPDF: Button
    private lateinit var listaFiltrada: List<Producto>
    private var pdfBytes: ByteArray? = null

    private val createPdfLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            contentResolver.openOutputStream(uri)?.use { output ->
                pdfBytes?.let { output.write(it) }
                Toast.makeText(this, "✅ PDF guardado exitosamente", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(this, "❌ No se seleccionó ubicación", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stock_minimo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            title = "Stock Minimo"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        spinnerProveedor = findViewById(R.id.spinnerProveedor)
        rvStockMinimo = findViewById(R.id.rvStockMinimo)
        btnGenerarPDF = findViewById(R.id.btnGenerarPDF)

        cargarProveedores()

        btnGenerarPDF.setOnClickListener {
            if (::listaFiltrada.isInitialized && listaFiltrada.isNotEmpty()) {
                generarPDF(listaFiltrada)
            } else {
                Toast.makeText(this, "No hay productos con stock mínimo para este proveedor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarProveedores() {
        val proveedores = ProveedorController().findAll()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            proveedores.map { it.nombre }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProveedor.adapter = adapter

        spinnerProveedor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val codProveedor = proveedores[position].codigo
                filtrarProductos(codProveedor)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun filtrarProductos(codProveedor: Int) {
        val productos = ProductoController().findAll()

        listaFiltrada = productos.filter {
            it.proveedor == codProveedor && it.stockActual <= it.stockMinimo
        }

        rvStockMinimo.adapter = StockMinimoAdapter(ArrayList(listaFiltrada))
        rvStockMinimo.layoutManager = LinearLayoutManager(this)
    }

    private fun generarPDF(lista: List<Producto>) {
        val document = PdfDocument()
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = 50
        paint.textSize = 16f
        canvas.drawText("REPORTE DE STOCK MÍNIMO", 180f, y.toFloat(), paint)
        y += 30

        paint.textSize = 12f
        for (producto in lista) {
            canvas.drawText("• ${producto.nombre} - Stock actual: ${producto.stockActual}, Stock mínimo: ${producto.stockMinimo}", 40f, y.toFloat(), paint)
            y += 20
            if (y >= 800) break
        }

        document.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        document.writeTo(outputStream)
        document.close()

        pdfBytes = outputStream.toByteArray()

        val proveedorNombre = spinnerProveedor.selectedItem.toString()
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-zA-Z0-9_]".toRegex(), "")
        val fechaActual = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "Stock_Minimo_${proveedorNombre}_$fechaActual.pdf"

        createPdfLauncher.launch(nombreArchivo)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
