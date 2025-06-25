package com.rafael0117.appgestionalmacen.entidad

import java.io.Serializable

class Producto(
    var codigo:Int,
    var nombre:String,
    var descripcion:String,
    var categoria: Int,
    var categoriaNombre: String? = null,
    var marca: String,
    var modelo: String,
    var proveedor: Int,
    var proveedorNombre: String? = null,
    var preciocompra:Double,
    var precioventa:Double,
    var stockActual:Int,
    var stockMinimo:Int,
    var ubicacion:String,
    var fechaIngreso:String,
    var estado:Int,
    var imagen:String
    ):Serializable {
}