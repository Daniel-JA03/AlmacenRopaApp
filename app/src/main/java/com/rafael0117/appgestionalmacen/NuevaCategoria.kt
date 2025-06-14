package com.rafael0117.appgestionalmacen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rafael0117.appgestionalmacen.controller.CategoriaController
import com.rafael0117.appgestionalmacen.entidad.Categoria
import com.rafael0117.appgestionalmacen.holder.VistaCategoria

class NuevaCategoria : AppCompatActivity() {
    private lateinit var edtNombre:EditText
    private lateinit var edtDescripcion:EditText
    private lateinit var btnGuardarCategoria: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_categoria)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val actionBar = supportActionBar
        actionBar?.title = "Nueva Categoria"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        edtNombre = findViewById(R.id.edtNombreC)
        edtDescripcion = findViewById(R.id.edtDescripcionC)
        btnGuardarCategoria = findViewById(R.id.btnGuardarCategoria)

        btnGuardarCategoria.setOnClickListener() {
            grabar()
        }





    }
    fun grabar(){
        var nom=edtNombre.text.toString()
        var des=edtDescripcion.text.toString()
        var bean=Categoria(0,nom,des,1)
        var salida=CategoriaController().adicionarCategoria(bean)
        if(salida==-1)
            Toast.makeText(this,"Error en el registro",Toast.LENGTH_LONG).show()
        else{
            Toast.makeText(this,"Categoria Registrado",Toast.LENGTH_LONG).show()

        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}