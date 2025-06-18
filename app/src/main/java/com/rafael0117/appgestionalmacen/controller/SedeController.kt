package com.rafael0117.appgestionalmacen.controller

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.utils.appConfig
import java.util.ArrayList

class SedeController {
    fun findAll(): ArrayList<Sede> {
        var lista = ArrayList<Sede>()
        // acceder a la base de datos en modo lectura
        var CN: SQLiteDatabase = appConfig.BD.readableDatabase
        var RS: Cursor = CN.rawQuery("select * from tb_sedes", null)
        // bucle para realizar recorrido sobre RS
        while (RS.moveToNext()) {
            // crear objeto de la clase Sede con los valores actuales de la fila
            var bean = Sede(RS.getInt(0), RS.getString(1),
                RS.getString(2), RS.getInt(3), RS.getInt(4))
            // enviar objeto bean al arreglo lista
            lista.add(bean)
        }
        return lista
    }
}