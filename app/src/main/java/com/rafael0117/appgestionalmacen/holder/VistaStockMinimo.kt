package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaStockMinimo(vista: View): RecyclerView.ViewHolder(vista) {
    var tvCodigo:TextView
    var tvNombre:TextView
    var tvStockInfo:TextView
    init {
        tvCodigo=vista.findViewById(R.id.tvCogidoStock)
        tvNombre=vista.findViewById(R.id.tvNombreProductoStock)
        tvStockInfo=vista.findViewById(R.id.tvStockInfo)
    }
}