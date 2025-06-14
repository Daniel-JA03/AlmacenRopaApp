package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class Email : AppCompatActivity() {

    private lateinit var cajaCorreo: TextInputEditText
    private lateinit var cajaAsunto: TextInputEditText
    private lateinit var cajaMensaje: TextInputEditText
    private lateinit var btnAdjuntar: Button
    private lateinit var btnEnviar: Button
    private lateinit var tvAdjunto: TextView

    private var uriPdf: Uri? = null

    // Permite seleccionar un PDF desde el almacenamiento
    private val seleccionarPdfLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                uriPdf = uri
                val nombreArchivo = uri.lastPathSegment?.split("/")?.last() ?: "PDF adjunto"
                tvAdjunto.text = "Adjunto: $nombreArchivo"
            } else {
                Toast.makeText(this, "No se seleccionó ningún archivo", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        // Vincular vistas
        cajaCorreo = findViewById(R.id.caja_correo)
        cajaAsunto = findViewById(R.id.caja_asunto)
        cajaMensaje = findViewById(R.id.caja_mensaje)
        btnAdjuntar = findViewById(R.id.btn_adjuntar)
        btnEnviar = findViewById(R.id.btn_enviar)
        tvAdjunto = findViewById(R.id.tv_adjunto)

        // Obtener los datos enviados por otra actividad (si los hay)
        val correoRecibido = intent.getStringExtra("correo") ?: ""
        val asuntoRecibido = intent.getStringExtra("asunto") ?: ""
        val mensajeRecibido = intent.getStringExtra("mensaje") ?: ""

        // Mostrar en las cajas de texto
        cajaCorreo.setText(correoRecibido)
        cajaAsunto.setText(asuntoRecibido)
        cajaMensaje.setText(mensajeRecibido)

        // Acción de adjuntar
        btnAdjuntar.setOnClickListener {
            seleccionarPdfLauncher.launch("application/pdf")
        }

        // Acción de enviar
        btnEnviar.setOnClickListener {
            enviarCorreo()
        }
    }

    private fun enviarCorreo() {
        val correo = cajaCorreo.text.toString().trim()
        val asunto = cajaAsunto.text.toString().trim()
        val mensaje = cajaMensaje.text.toString().trim()

        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el correo destinatario", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (uriPdf != null) "application/pdf" else "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
            putExtra(Intent.EXTRA_SUBJECT, asunto)
            putExtra(Intent.EXTRA_TEXT, mensaje)

            if (uriPdf != null) {
                putExtra(Intent.EXTRA_STREAM, uriPdf)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        startActivity(Intent.createChooser(intent, "Selecciona una app de correo"))
    }
}
