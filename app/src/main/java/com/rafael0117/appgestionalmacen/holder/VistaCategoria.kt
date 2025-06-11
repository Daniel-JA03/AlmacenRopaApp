package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaCategoria(vista:View):RecyclerView.ViewHolder(vista) {
    var tvCodigo:TextView
    var tvNombre:TextView
    var tvDescripcion:TextView

    init {
        tvCodigo=vista.findViewById(R.id.tvCodigoC)
        tvNombre=vista.findViewById(R.id.tvNombreC)
        tvDescripcion=vista.findViewById(R.id.tvDescripcionC)

    }

}