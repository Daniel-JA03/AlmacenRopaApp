package com.rafael0117.appgestionalmacen.entidad

import java.io.Serializable

class SedeConCant(
    var codigo: Int,
    var foto: String,
    var nomDis: String,
    var estado: Int,
    var cantidadSedes: Int
): Serializable