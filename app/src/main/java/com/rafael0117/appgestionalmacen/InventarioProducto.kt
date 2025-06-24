package com.rafael0117.appgestionalmacen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.DocumentProperties
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.rafael0117.appgestionalmacen.adaptador.ProductoAdapter
import com.rafael0117.appgestionalmacen.controller.ProductoController
import com.rafael0117.appgestionalmacen.entidad.Producto
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class InventarioProducto : AppCompatActivity() {

    private lateinit var edtBuscarProducto: EditText
    private lateinit var btnBuscarProducto: Button
    private lateinit var rgEstadoProducto: RadioGroup
    private lateinit var rbTodos: RadioButton
    private lateinit var rbActivos: RadioButton
    private lateinit var rbInactivos: RadioButton
    private lateinit var rvInventarioProductos: RecyclerView

    private lateinit var fabVerPdf: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventario_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val actionBar = supportActionBar
        actionBar?.title = "Inventario"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        edtBuscarProducto = findViewById(R.id.edtBuscarProducto)
        btnBuscarProducto = findViewById(R.id.btnBuscarProducto)
        rgEstadoProducto = findViewById(R.id.rgEstadoProducto)
        rbTodos = findViewById(R.id.rbTodosEstado)
        rbActivos = findViewById(R.id.rbActivos)
        rbInactivos = findViewById(R.id.rbInactivos)
        rvInventarioProductos = findViewById(R.id.rvInventarioProductos)
        fabVerPdf = findViewById(R.id.fabVerPdf)

        fabVerPdf.setOnClickListener {
            val listaCompleta = ProductoController().findAll()
            generarPDFdeProductosConiText(listaCompleta, this)
        }


        btnBuscarProducto.setOnClickListener {
            cargarProductos()
        }

        rgEstadoProducto.setOnCheckedChangeListener { _, _ ->
            cargarProductos()
        }

        cargarProductos() // carga inicial

        // Verificar alerta de stock mínimo
        val productosCriticos = ProductoController().findProductosConStockMinimo()
        if (productosCriticos.isNotEmpty()) {
            obtenerCorreosDeVendedores { vendedores ->
                if (vendedores.isNotEmpty()) {
                    enviarAlertaPorCorreo(this, productosCriticos, vendedores.toTypedArray())
                } else {
                    Toast.makeText(this, "No hay vendedores registrados.", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    // PDF listado de productos
    private fun generarPDFdeProductosConiText(productos: List<Producto>, context: Context) {
        val directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        directorio?.mkdirs()

        val archivo = File(directorio, "listado_productos.pdf")

        try {
            // Crear escritor y documento PDF
            val writer = PdfWriter(FileOutputStream(archivo))
            val pdfDoc = PdfDocument(writer, DocumentProperties())
            val document = Document(pdfDoc)

            // Cargar logo desde recursos
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.localizacion_sedes)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageData = ImageDataFactory.create(stream.toByteArray())
            val logo = Image(imageData).setWidth(UnitValue.createPercentValue(20f))

            // Añadir logo y título
            val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1f))).useAllAvailableWidth()
            headerTable.addCell(Cell().add(logo.setHorizontalAlignment(HorizontalAlignment.CENTER)))
            headerTable.addCell(
                Cell().add(
                    Paragraph("Empresa RopaStock")
                        .setFontSize(14f)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.GRAY)
                )
            )
            headerTable.addCell(
                Cell().add(
                    Paragraph("Listado de Productos")
                        .setFontSize(20f)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(10f)
                        .setMarginBottom(20f)
                )
            )

            document.add(headerTable)

            // Crear tabla de productos
            val table = Table(UnitValue.createPercentArray(floatArrayOf(3f, 2f, 2f, 2f, 2f))).useAllAvailableWidth()
            table.setTextAlignment(TextAlignment.CENTER)
            table.setFontSize(10f)

            // Estilo de encabezado
            val headerStyle = Style()
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(DeviceRgb(30, 136, 229)) // 1E88E5 en RGB decimal
                .setBold()
                .setPadding(6f)

            // Encabezados
            table.addHeaderCell(Cell().add(Paragraph("Nombre").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Precio Compra").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Precio Venta").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Stock").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Estado").addStyle(headerStyle)))

            // Datos de productos
            for (producto in productos) {
                val estadoTexto = if (producto.estado == 1) "Activo" else "Inactivo"

                // Estilo común para celdas
                val cellStyle = Style()
                    .setPadding(5f)
                    .setBorder(Border.NO_BORDER)

                table.addCell(Cell().add(Paragraph(producto.nombre).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(producto.preciocompra.toString()).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(producto.precioventa.toString()).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(producto.stockActual.toString()).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(estadoTexto).addStyle(cellStyle)))
            }

            // Añadir tabla al documento
            document.add(table)

            // Pie de página
            val footer = Paragraph("© ${Calendar.getInstance().get(Calendar.YEAR)} Empresa RopaStock - Todos los derechos reservados")
                .setFontSize(9f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30f)
            document.add(footer)

            // Cerrar documento
            document.close()

            Toast.makeText(context, "PDF generado en: ${archivo.path}", Toast.LENGTH_LONG).show()

            // Abrir PDF
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                archivo
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al crear el PDF", Toast.LENGTH_SHORT).show()
        }
    }

    // PDF Alerta de stock
    private fun generarPDFdeAlertaStock(context: Context, productos: List<Producto>, outputFile: File) {
        try {
            val writer = PdfWriter(FileOutputStream(outputFile))
            val pdfDoc = PdfDocument(writer, DocumentProperties())
            val document = Document(pdfDoc)

            // Título
            document.add(
                Paragraph("Alerta de Stock Mínimo")
                    .setFontSize(18f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setMarginBottom(20f)
            )

            // Tabla
            val table = Table(UnitValue.createPercentArray(floatArrayOf(3f, 2f, 2f, 2f))).useAllAvailableWidth()

            // Encabezados
            val headerStyle = Style()
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(DeviceRgb(211, 47, 47)) // Rojo oscuro
                .setBold()
                .setPadding(6f)

            table.addHeaderCell(Cell().add(Paragraph("Nombre").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Stock Actual").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Stock Mínimo").addStyle(headerStyle)))
            table.addHeaderCell(Cell().add(Paragraph("Estado").addStyle(headerStyle)))

            // Datos
            for (producto in productos) {
                val cellStyle = Style()
                    .setPadding(5f)
                    .setBorder(Border.NO_BORDER)

                table.addCell(Cell().add(Paragraph(producto.nombre).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(producto.stockActual.toString()).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(producto.stockMinimo.toString()).addStyle(cellStyle)))
                table.addCell(Cell().add(Paragraph(if (producto.estado == 1) "Activo" else "Inactivo").addStyle(cellStyle)))
            }

            document.add(table)

            // Mensaje final
            document.add(
                Paragraph("\nEstos productos han alcanzado su nivel mínimo de inventario.")
                    .setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic()
                    .setMarginTop(30f)
            )

            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // funcion para enviar la alerta al correo
    private fun enviarAlertaPorCorreo(context: Context, productos: List<Producto>, destinatarios: Array<String>) {
        if (productos.isEmpty()) return

        val directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        directorio?.mkdirs()
        val archivoPdf = File(directorio, "alerta_stock_minimo.pdf")
        generarPDFdeAlertaStock(context, productos, archivoPdf)

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, destinatarios)
            putExtra(Intent.EXTRA_SUBJECT, "⚠️ Alerta: Producto(s) en stock mínimo")
            putExtra(Intent.EXTRA_TEXT, "Hola,\n\nLos siguientes productos han alcanzado su nivel mínimo de inventario:")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                archivoPdf
            )
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        try {
            // Usamos el launcher para recibir el resultado
            correoLauncher.launch(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "❌ No hay se encontro correos de Vendedor.", Toast.LENGTH_SHORT).show()
        }
    }

    private val correoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Toast.makeText(this, "✅ Se envió correctamente la alerta de stock mínimo", Toast.LENGTH_LONG).show()
    }


    private fun obtenerCorreosDeVendedores(onComplete: (List<String>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val usuariosRef = database.getReference("Usuarios")

        usuariosRef.orderByChild("rol").equalTo("Vendedor")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val emails = mutableListOf<String>()
                    for (child in snapshot.children) {
                        val correo = child.child("correo").value as? String
                        correo?.let { emails.add(it) }
                    }
                    onComplete(emails)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseDBError", "Error al leer usuarios: ${error.message}")
                    onComplete(emptyList())
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
