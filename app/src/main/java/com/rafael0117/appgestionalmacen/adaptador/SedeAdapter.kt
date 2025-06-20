package com.rafael0117.appgestionalmacen.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.Mapa
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.entidad.SedeConCant
import com.rafael0117.appgestionalmacen.holder.VistaSede
import java.util.ArrayList

class SedeAdapter(
    var lista: ArrayList<SedeConCant>,
    private val onItemClick: (SedeConCant) -> Unit
) : RecyclerView.Adapter<VistaSede>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaSede {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sede, parent, false)
        return VistaSede(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: VistaSede, position: Int) {
        val sede = lista[position]
        val context = holder.itemView.context

        holder.tvCodigo.text = "Código: ${sede.codigo}"
        holder.tvNombre.text = sede.nomDis
        holder.tvCantidadSedes.text = "${sede.cantidadSedes} sedes"
        holder.tvEstado.text = if (sede.estado == 1) "Activo" else "Desactivado"
        holder.tvEstado.setTextColor(
            ContextCompat.getColor(
                context,
                if (sede.estado == 1) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        val ID = context.resources.getIdentifier(
            sede.foto, "drawable", context.packageName
        )
        holder.imgSede.setImageResource(ID)

        holder.itemView.setOnClickListener {
            onItemClick(sede)
        }

        holder.btnUbicacion.setOnClickListener {
            val intent = Intent(context, Mapa::class.java)
            intent.putExtra("sede", sede)
            context.startActivity(intent)
        }
    }
}