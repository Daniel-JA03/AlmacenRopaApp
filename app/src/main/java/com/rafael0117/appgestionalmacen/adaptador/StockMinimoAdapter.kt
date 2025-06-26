package com.rafael0117.appgestionalmacen.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafael0117.appgestionalmacen.R
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.holder.VistaCategoria
import com.rafael0117.appgestionalmacen.holder.VistaStockMinimo

class StockMinimoAdapter( val lista: List<Producto>) :
    RecyclerView.Adapter<VistaStockMinimo>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaStockMinimo {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_producto, parent, false)
        return VistaStockMinimo(item)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: VistaStockMinimo, position: Int) {
        val producto = lista[position]
        holder.tvCodigo.text = "Código: ${producto.codigo}"
        holder.tvNombre.text = producto.nombre
        holder.tvStockInfo.text = "Stock actual: ${producto.stockActual} | Stock mínimo: ${producto.stockMinimo}"
    }
}