package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class MenuPrincipal : AppCompatActivity() {

    private lateinit var tvNombres:TextView
    private lateinit var tvCorreo:TextView
    private lateinit var btnCerrarSesion:Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var progressBar: ProgressBar
    private lateinit var cdCategorias: CardView
    private lateinit var cdInventario: CardView
    private lateinit var cdProveedores: CardView
    private lateinit var cdLocalizacion: CardView
    private lateinit var cdAbastecimiento: CardView
    private lateinit var cdStockMinimo: CardView
    private lateinit var cdTraslado: CardView

    private lateinit var Usuarios:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val actionBar = supportActionBar
        actionBar?.title="Almacen Ropa"


        cdCategorias=findViewById(R.id.cdCategorias)
        cdInventario=findViewById(R.id.cdInventario)
        cdProveedores=findViewById(R.id.cdProveedores)
        cdLocalizacion=findViewById(R.id.cdLocalizacion)
        cdAbastecimiento=findViewById(R.id.cdAbastecimiento)
        cdStockMinimo=findViewById(R.id.cdStockMinimo)
        cdTraslado=findViewById(R.id.cdTraslado)

        btnCerrarSesion=findViewById(R.id.CerrarSesion)
        tvNombres=findViewById(R.id.NombrePrincipal)
        tvCorreo=findViewById(R.id.CorreoPrinciapl)
        progressBar=findViewById(R.id.progressBarDatos)
        Usuarios=FirebaseDatabase.getInstance().getReference("Usuarios")
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        cdCategorias.setOnClickListener(){
            startActivity(Intent(this,Categorias::class.java))
        }
        cdInventario.setOnClickListener(){
            startActivity(Intent(this,InventarioProducto::class.java))
        }

        cdProveedores.setOnClickListener(){
            startActivity(Intent(this,Proveedores::class.java))
        }

        cdLocalizacion.setOnClickListener() {
            startActivity(Intent(this, Sedes::class.java))
        }
        cdAbastecimiento.setOnClickListener(){
            startActivity(Intent(this,Productos::class.java))
        }
        cdStockMinimo.setOnClickListener(){
            startActivity(Intent(this,StockMinimo::class.java))
        }
        cdTraslado.setOnClickListener(){
            startActivity(Intent(this,RegistrarMovimiento::class.java))
        }


        btnCerrarSesion.setOnClickListener(){
            salirAplicacion()

        }

    }

    override fun onStart() {
        comprobarInicioSesion()
        super.onStart()
    }
    private fun comprobarInicioSesion(){
        if(firebaseUser!=null){
            cargaDatos()
        }
        else{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun cargaDatos() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            Usuarios.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        progressBar.visibility = View.GONE
                        tvNombres.visibility = View.VISIBLE
                        tvCorreo.visibility = View.VISIBLE

                        val nombre: String = snapshot.child("Nombre").getValue(String::class.java) ?: ""
                        val correo: String = snapshot.child("correo").getValue(String::class.java) ?: ""
                        val rol: String = snapshot.child("rol").getValue(String::class.java) ?: ""

                        tvNombres.text = nombre
                        tvCorreo.text = correo

                        // Lógica para mostrar u ocultar según el rol
                        if (rol.equals("Administrador", ignoreCase = true)) {
                            // Mostrar todo
                            cdCategorias.visibility = View.VISIBLE
                            cdInventario.visibility = View.GONE
                            cdProveedores.visibility = View.VISIBLE
                            cdLocalizacion.visibility = View.VISIBLE
                            cdAbastecimiento.visibility = View.VISIBLE
                            cdStockMinimo.visibility = View.VISIBLE
                            cdTraslado.visibility = View.VISIBLE
                        } else if (rol.equals("Vendedor", ignoreCase = true)) {
                            // Mostrar solo algunos
                            cdCategorias.visibility = View.GONE
                            cdInventario.visibility = View.VISIBLE
                            cdProveedores.visibility = View.GONE
                            cdLocalizacion.visibility = View.VISIBLE
                            cdAbastecimiento.visibility = View.GONE


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MenuPrincipal, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }



    private fun salirAplicacion() {
        firebaseAuth.signOut()
        startActivity(Intent(this,MainActivity::class.java))
        Toast.makeText(this,"Cerraste Sesion Exitosamente",Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Cierra todas las actividades de la pila
    }

}