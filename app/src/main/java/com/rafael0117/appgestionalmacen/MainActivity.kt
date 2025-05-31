package com.rafael0117.appgestionalmacen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var Btn_Login:Button
    private lateinit var Btn_Registro:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Btn_Login = findViewById(R.id.Btn_Login)
        Btn_Registro = findViewById(R.id.Btn_Registro)

        Btn_Login.setOnClickListener(){
            startActivity(Intent(this,Login::class.java))

        }
        Btn_Registro.setOnClickListener(){
            startActivity(Intent(this,Registro::class.java))

        }
    }
}