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
            startActivity(Intent(this,Proveedores::class.java))
        }

        cdProveedores.setOnClickListener(){
            startActivity(Intent(this,Proveedores::class.java))
        }
        cdProveedores.setOnClickListener(){
            startActivity(Intent(this,Proveedores::class.java))
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
            Usuarios.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Si el usuario existe
                    if(snapshot.exists()){
                        progressBar.visibility=View.GONE
                        tvNombres.visibility=View.VISIBLE
                        tvCorreo.visibility=View.VISIBLE
                        //Obtener los datos
                        val nombre:String = ""+snapshot.child("Nombre").getValue()
                        val correo:String = ""+snapshot.child("correo").getValue()
                        tvNombres.setText(nombre)
                        tvCorreo.setText(correo)
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
}