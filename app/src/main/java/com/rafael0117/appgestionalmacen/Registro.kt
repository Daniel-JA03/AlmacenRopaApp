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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception


class Registro : AppCompatActivity() {


    private lateinit var EdtNombre:EditText
    private lateinit var EdtCorreo:EditText
    private lateinit var EdtContrasena:EditText
    private lateinit var EdtConfirmarContrasena:EditText
    private lateinit var btnRegistrar:Button
    private lateinit var tvTengoCuenta:TextView


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    //VARIABLES GLOBALES
    var nombre:String=""
    var correo:String=""
    var password:String=""
    var confirmarpassword:String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val actionBar = supportActionBar
        actionBar?.title = "Registrar"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)



        EdtNombre=findViewById(R.id.NombreEt)
        EdtCorreo=findViewById(R.id.CorreoEt)
        EdtContrasena=findViewById(R.id.ContraseñaEt)
        EdtConfirmarContrasena=findViewById(R.id.etConfirmarContraseña)
        btnRegistrar=findViewById(R.id.RegistrarUsuario)
        tvTengoCuenta=findViewById(R.id.TengounacuentaTXT)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        btnRegistrar.setOnClickListener(){
            validarDatos()

        }

        tvTengoCuenta.setOnClickListener(){
            startActivity(Intent(this,Login::class.java))


        }


        }

    private fun validarDatos(){
        nombre=EdtNombre.text.toString()
        correo=EdtCorreo.text.toString()
        password=EdtContrasena.text.toString()
        confirmarpassword=EdtConfirmarContrasena.text.toString()

    if(TextUtils.isEmpty(nombre)){
        Toast.makeText(this,"Ingrese nombre",Toast.LENGTH_SHORT).show()
    }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Ingrese correo",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingrese Contraseña",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(confirmarpassword)){
            Toast.makeText(this,"Confirme Contraseña",Toast.LENGTH_SHORT).show()
        }
        else if(!password.equals(confirmarpassword)){
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
        }
        else{
            crearCuenta()
        }

    }

    private fun guardarInformacion(){
        progressDialog.setMessage("Guardando su informacion")
        progressDialog.show()

        //Obtener la identificacion de usuario actual
        var uid:String= firebaseAuth.uid.toString()

        val datos = HashMap<String, String>()
        datos["uid"] = uid
        datos["correo"] = correo
        datos["Nombre"] = nombre
        datos["password"] = password
        val databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")
        databaseReference.child(uid)
            .setValue(datos)
            .addOnSuccessListener {
                // Aquí puedes mostrar un mensaje o redirigir al usuario
                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                startActivity(Intent(this,MenuPrincipal::class.java))
                finish()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }



    }

    private fun crearCuenta() {
        progressDialog.setMessage("Creando su cuenta...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                // Aquí guardas info en Firestore o lo que hagas
                guardarInformacion()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    e.message ?: "Error al crear la cuenta",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }











}
