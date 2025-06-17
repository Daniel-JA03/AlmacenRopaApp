package com.rafael0117.appgestionalmacen.adaptador

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.DetalleProducto
import com.rafael0117.appgestionalmacen.Email
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.holder.VistaCategoria
import com.rafael0117.appgestionalmacen.holder.VistaProducto

class ProductoAdapter (var lista:ArrayList<Producto>):RecyclerView.Adapter<VistaProducto>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaProducto {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return VistaProducto(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: VistaProducto, position: Int) {
        val producto = lista[position]
        holder.tvCodigo.text = producto.codigo.toString()
        holder.tvNombre.text = producto.nombre
        holder.tvDescripcion.text = producto.descripcion
        holder.tvEstado.text = if (producto.estado == 1) "Activo" else "Desactivado"
        holder.btnVerMas.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleProducto::class.java).apply {
                putExtra("codigo", producto.codigo)
                putExtra("nombre", producto.nombre)
                putExtra("descripcion", producto.descripcion)
                putExtra("categoria", producto.categoria)
                putExtra("marca", producto.marca)
                putExtra("modelo", producto.modelo)
                putExtra("proveedor", producto.proveedor)
                putExtra("preciocompra", producto.preciocompra)
                putExtra("precioventa", producto.precioventa)
                putExtra("stockActual", producto.stockActual)
                putExtra("stockMinimo", producto.stockMinimo)
                putExtra("ubicacion", producto.ubicacion)
                putExtra("fechaIngreso", producto.fechaIngreso)
                putExtra("estado", producto.estado)
                putExtra("imagen", producto.imagen)
            }
            context.startActivity(intent)
        }

    }
}