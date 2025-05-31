package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Pantalla_De_Carga : AppCompatActivity() {
    private val tiempo: Long = 3000L

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_de_carga)

        firebaseAuth = FirebaseAuth.getInstance()


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, tiempo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun verificarUsuario(){
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if(firebaseUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
        else{
            startActivity(Intent(this,MenuPrincipal::class.java))

        }

    }
}
