package com.rafael0117.appgestionalmacen.adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.holder.VistaSede
import java.util.ArrayList

class SedeAdapter(
    var lista: ArrayList<Sede>,
    private val onItemClick: (Sede) -> Unit): RecyclerView.Adapter<VistaSede>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaSede {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_sede, parent, false)
        return VistaSede(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: VistaSede, position: Int) {
        val sede = lista[position]
        var context:Context=holder.itemView.context

        holder.tvCodigo.text = sede.codigo.toString()
        holder.tvNombre.text = sede.nomDis
        holder.tvCantidadSedes.text = sede.canSedes.toInt().toString()
        holder.tvEstado.text = if(sede.estado == 1) "Activo" else "Desactivado"
        holder.tvEstado.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (sede.estado == 1) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        var ID:Int

        ID = context.resources.getIdentifier(lista.get(position).foto, "drawable", context.packageName)
        holder.imgSede.setImageResource(ID)
        holder.itemView.setOnClickListener {
            onItemClick(sede)
        }

    }


}