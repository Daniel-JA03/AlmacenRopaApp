package com.rafael0117.appgestionalmacen.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Categoria
import com.rafael0117.appgestionalmacen.holder.VistaCategoria

class CategoriaAdapter(
    var lista: ArrayList<Categoria>,
    private val onItemClick: (Categoria) -> Unit
) : RecyclerView.Adapter<VistaCategoria>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaCategoria {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return VistaCategoria(item)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: VistaCategoria, position: Int) {
        val categoria = lista[position]

        holder.tvCodigo.text = categoria.codigo.toString()
        holder.tvNombre.text = categoria.nombre
        holder.tvDescripcion.text = categoria.descripcion

        holder.tvEstado.text = if (categoria.estado == 1) "Activo" else "Desactivado"
        holder.tvEstado.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (categoria.estado == 1) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        holder.itemView.setOnClickListener {
            onItemClick(categoria)
        }
    }
}
