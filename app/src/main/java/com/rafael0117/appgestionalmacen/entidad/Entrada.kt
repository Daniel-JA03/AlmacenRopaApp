package com.rafael0117.appgestionalmacen.entidad

class Entrada
    (
    val id: Int = 0,
    val proveedor: String,
    val fecha: String,
    val productos: List<ProductoAgregado> = emptyList()

            ){
}