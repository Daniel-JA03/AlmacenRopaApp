package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaProducto(vista: View): RecyclerView.ViewHolder(vista) {
    var tvCodigo:TextView
    var tvNombre:TextView
    var tvDescripcion:TextView
    var tvEstado:TextView
    var imgProducto:ImageView
    var btnVerMas:Button

    init {
        tvCodigo=vista.findViewById(R.id.tvCodigoP)
        tvNombre=vista.findViewById(R.id.tvNombreP)
        tvDescripcion=vista.findViewById(R.id.tvDescripcionP)
        tvEstado=vista.findViewById(R.id.tvEstado)
        imgProducto=vista.findViewById(R.id.imgProducto)
        btnVerMas=vista.findViewById(R.id.btnVerMas)
    }

}