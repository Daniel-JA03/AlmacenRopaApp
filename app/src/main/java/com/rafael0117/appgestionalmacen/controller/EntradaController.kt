package com.rafael0117.appgestionalmacen.controller

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Entrada
import com.rafael0117.appgestionalmacen.utils.appConfig

class EntradaController

fun findAll(): ArrayList<Entrada> {
    val lista = ArrayList<Entrada>()
    val CN: SQLiteDatabase = appConfig.BD.readableDatabase

    // INNER JOIN para traer el nombre del proveedor en lugar del código
    val RS: Cursor = CN.rawQuery(
        "SELECT e.codigo, p.nombre, e.fecha_ingreso " +
                "FROM tb_entrada e " +
                "INNER JOIN tb_proveedor p ON e.proveedor_codigo = p.codigo", null
    )

    while (RS.moveToNext()) {
        val entrada = Entrada(
            id = RS.getInt(0),
            proveedor = RS.getString(1),
            fecha = RS.getString(2)
        )
        lista.add(entrada)
    }

    return lista
}


