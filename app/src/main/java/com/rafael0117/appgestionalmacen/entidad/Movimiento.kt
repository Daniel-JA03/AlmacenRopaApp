package com.rafael0117.appgestionalmacen.entidad

class Movimiento( val codigo: Int = 0,
                  val tipo: String,
                  val fecha: String,
                  val sedeOrigen: Int?,
                  val sedeDestino: Int?,
                  val observaciones: String?) {
}