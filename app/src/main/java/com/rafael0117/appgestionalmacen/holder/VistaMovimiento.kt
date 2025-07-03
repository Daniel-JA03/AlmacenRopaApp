package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaMovimiento(vista: View): RecyclerView.ViewHolder(vista) {
    var tvMovimientoInfo:TextView
    var tvMovimientoFecha:TextView


    init {
        tvMovimientoInfo=vista.findViewById(R.id.tvMovimientoInfo)
        tvMovimientoFecha=vista.findViewById(R.id.tvMovimientoFecha)
    }
}