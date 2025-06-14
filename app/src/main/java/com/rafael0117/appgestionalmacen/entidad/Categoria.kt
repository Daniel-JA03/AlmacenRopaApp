package com.rafael0117.appgestionalmacen.entidad

import java.io.Serializable

class Categoria(var codigo: Int,
                var nombre: String,
                var descripcion: String,
                var estado:Int):Serializable {
}