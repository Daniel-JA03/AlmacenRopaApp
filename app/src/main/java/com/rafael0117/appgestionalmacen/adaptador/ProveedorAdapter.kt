package com.rafael0117.appgestionalmacen.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Proveedor
import com.rafael0117.appgestionalmacen.holder.VistaProveedor


class ProveedorAdapter(
    var lista: ArrayList<Proveedor>,
    private val onItemClick: (Proveedor) -> Unit
) : RecyclerView.Adapter<VistaProveedor>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaProveedor {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_proveedor, parent, false)
        return VistaProveedor(item)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: VistaProveedor, position: Int) {
        val proveedor = lista[position]

        holder.tvCodigo.text = proveedor.codigo.toString()
        holder.tvNombre.text = proveedor.nombre
        holder.tvTelefono.text = proveedor.telefono
        holder.tvCorreo.text = proveedor.correo
        holder.tvDireccion.text = proveedor.direccion
        holder.tvContacto.text = proveedor.contacto

        holder.tvEstado.text = if (proveedor.estado == 1) "Activo" else "Desactivado"
        holder.tvEstado.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (proveedor.estado == 1) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        holder.itemView.setOnClickListener {
            onItemClick(proveedor)
        }
    }
}
