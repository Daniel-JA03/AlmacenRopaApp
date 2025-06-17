package com.rafael0117.appgestionalmacen.entidad

import java.util.Date

class Producto(var codigo:Int,
               var nombre:String,
               var descripcion:String,
               var categoria: String,
               var marca: String,
               var modelo: String,
               var proveedor: String,
               var preciocompra:Double,
               var precioventa:Double,
               var stockActual:Int,
               var stockMinimo:Int,
               var ubicacion:String,
               var fechaIngreso:String,
               var estado:Int,
               var imagen:String
    ) {
}