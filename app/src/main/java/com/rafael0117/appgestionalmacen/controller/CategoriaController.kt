package com.rafael0117.appgestionalmacen.controller

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Categoria
import com.rafael0117.appgestionalmacen.utils.appConfig

class CategoriaController {

    fun findAll():ArrayList<Categoria>{
        var lista=ArrayList<Categoria>()
        //acceder a la base de datos en modo lectura
        var CN: SQLiteDatabase =appConfig.BD.readableDatabase
        var RS: Cursor =CN.rawQuery("select *from tb_categoria",null)
        //bucle para realizar reacorrido sobre RS
        while (RS.moveToNext()){
            //crear objeto de la clase Docente con los valores actuales de la fila
            var bean=Categoria(RS.getInt(0),RS.getString(1),RS.getString(2))
            //enviar objeto "bean" al arreglo lista
            lista.add(bean)
        }
        return lista
    }

}