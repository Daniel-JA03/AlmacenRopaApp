package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MenuPrincipal : AppCompatActivity() {

    private lateinit var btnCerrarSesion:Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCerrarSesion=findViewById(R.id.CerrarSesion)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!


        btnCerrarSesion.setOnClickListener(){
            salirAplicacion()

        }
    }

    private fun salirAplicacion() {
        firebaseAuth.signOut()
        startActivity(Intent(this,MainActivity::class.java))
        Toast.makeText(this,"Cerraste Sesion Exitosamente",Toast.LENGTH_SHORT).show()
    }
}