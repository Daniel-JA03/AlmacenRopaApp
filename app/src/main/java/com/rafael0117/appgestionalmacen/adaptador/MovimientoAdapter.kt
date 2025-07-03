package com.rafael0117.appgestionalmacen.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Movimiento
import com.rafael0117.appgestionalmacen.holder.VistaMovimiento

class MovimientoAdapter(
    private val movimientos: List<Movimiento>,
    private val onItemClick: (Movimiento) -> Unit
) : RecyclerView.Adapter<VistaMovimiento>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaMovimiento {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento, parent, false)
        return VistaMovimiento(view)
    }

    override fun getItemCount(): Int = movimientos.size

    override fun onBindViewHolder(holder: VistaMovimiento, position: Int) {
        val movimiento = movimientos[position]
        holder.tvMovimientoInfo.text = "ID: ${movimiento.codigo} - ${movimiento.tipo} (Origen:${movimiento.sedeOrigen} Destino:${movimiento.sedeDestino})"
        holder.tvMovimientoFecha.text = "Fecha: ${movimiento.fecha}"
        holder.itemView.setOnClickListener { onItemClick(movimiento) }
    }
}