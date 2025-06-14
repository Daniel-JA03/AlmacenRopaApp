package com.rafael0117.appgestionalmacen.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R

class VistaProveedor(vista: View): RecyclerView.ViewHolder(vista) {

    var tvCodigo:TextView
    var tvNombre:TextView
    var tvTelefono:TextView
    var tvCorreo:TextView
    var tvDireccion:TextView
    var tvContacto:TextView
    var tvEstado:TextView


    init {
        tvCodigo=vista.findViewById(R.id.tvCodigoP)
        tvNombre=vista.findViewById(R.id.tvNombreP)
        tvCodigo=vista.findViewById(R.id.tvCodigoP)
        tvTelefono=vista.findViewById(R.id.tvTelefonoP)
        tvCorreo=vista.findViewById(R.id.tvCorreoP)
        tvDireccion=vista.findViewById(R.id.tvDireccionP)
        tvContacto=vista.findViewById(R.id.tvContactoP)
        tvEstado=vista.findViewById(R.id.tvEstadoP)



    }
}