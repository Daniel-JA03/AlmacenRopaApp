package com.rafael0117.appgestionalmacen

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private lateinit var edtCorreo:EditText
    private lateinit var edtContrasena:EditText
    private lateinit var btnLogin:Button
    private lateinit var tvUsuarioNuevo:TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    var correo:String = ""
    var password:String = ""



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val actionBar = supportActionBar
        actionBar?.title = "Login"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        edtCorreo=findViewById(R.id.CorreoLogin)
        edtContrasena=findViewById(R.id.PassLogin)
        btnLogin=findViewById(R.id.BtnLogeo)
        tvUsuarioNuevo=findViewById(R.id.UsuarioNuevoTXT)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        btnLogin.setOnClickListener(){
            validarDatos()


        }
        tvUsuarioNuevo.setOnClickListener(){
            startActivity(Intent(this,Registro::class.java))
        }






    }

    private fun validarDatos() {
        correo = edtCorreo.text.toString()
        password = edtContrasena.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Correo invalido",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingrese Contraseña",Toast.LENGTH_SHORT).show()
        }
        else{
            loginDelUsuario()
        }

    }

    private fun loginDelUsuario() {
        progressDialog.setMessage("Iniciando Sesión...")
        progressDialog.show()



        firebaseAuth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = firebaseAuth.currentUser
                    progressDialog.dismiss()
                    startActivity(Intent(this,MenuPrincipal::class.java))
                    Toast.makeText(this, "Bienvenido(a) ${user!!.email}", Toast.LENGTH_SHORT).show()
                    finish()


                    // Ir a otra actividad si es necesario
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Fallo: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}