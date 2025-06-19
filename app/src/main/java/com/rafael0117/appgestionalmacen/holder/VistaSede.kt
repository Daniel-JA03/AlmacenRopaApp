package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaSede(vista:View): RecyclerView.ViewHolder(vista) {
    var tvCodigo: TextView
    var imgSede: ImageView
    var tvNombre: TextView
    var tvCantidadSedes: TextView
    var tvEstado: TextView
    var btnUbicacion: Button

    init {
        tvCodigo = vista.findViewById(R.id.tvCodigoS)
        imgSede = vista.findViewById(R.id.imgSede)
        tvNombre = vista.findViewById(R.id.tvNombreD)
        tvCantidadSedes = vista.findViewById(R.id.tvCantidadSedes)
        tvEstado = vista.findViewById(R.id.tvEstado)
        btnUbicacion = vista.findViewById(R.id.btnUbicacion)
    }
}