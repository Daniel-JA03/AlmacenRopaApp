package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaEntrada(vista:View):RecyclerView.ViewHolder(vista) {
    // Atributos
    var tvNombreProducto:TextView
    var tvCantidadIngresada:TextView

    // Referenciar
    init {
        tvNombreProducto = vista.findViewById(R.id.tvNombreProducto)
        tvCantidadIngresada = vista.findViewById(R.id.tvCantidadIngresada)
    }
}