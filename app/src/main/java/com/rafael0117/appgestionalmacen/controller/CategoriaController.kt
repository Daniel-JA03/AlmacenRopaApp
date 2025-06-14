package com.rafael0117.appgestionalmacen.controller

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.Categorias
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
            var bean=Categoria(RS.getInt(0),RS.getString(1),RS.getString(2),RS.getInt(3))
            //enviar objeto "bean" al arreglo lista
            lista.add(bean)
        }
        return lista
    }
    fun adicionarCategoria(bean:Categoria):Int{
        var salida=-1
        var cn:SQLiteDatabase=appConfig.BD.writableDatabase
        var row=ContentValues()
        row.put("nom",bean.nombre)
        row.put("des",bean.descripcion)
        row.put("est", bean.estado)
        salida=cn.insert("tb_categoria","cod",row).toInt()
        return salida
    }
    //Esto ira en una pantallla de Categoria
    fun editarCategoria(bean:Categoria):Int{
        var salida=-1
        var cn:SQLiteDatabase=appConfig.BD.writableDatabase
        var row=ContentValues()
        row.put("nom",bean.nombre)
        row.put("des",bean.descripcion)
        row.put("est", bean.estado)
        salida=cn.update("tb_categoria",row,"cod=?", arrayOf(bean.codigo.toString()))
        return salida
    }
    fun eliminarCategoria(codigo: Int): Int {
        var salida = -1
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        salida = cn.delete("tb_categoria", "cod = ?", arrayOf(codigo.toString()))
        return salida
    }
    fun buscarCategoria(input: String): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val cn = appConfig.BD.readableDatabase

        val rs = if (input.all { it.isDigit() }) {
            // Buscar por código
            cn.rawQuery("SELECT * FROM tb_categoria WHERE cod = ?", arrayOf(input))
        } else {
            // Buscar por nombre (LIKE permite coincidencias parciales)
            cn.rawQuery("SELECT * FROM tb_categoria WHERE nom LIKE ?", arrayOf("%$input%"))
        }

        while (rs.moveToNext()) {
            val bean = Categoria(
                rs.getInt(0),     // cod
                rs.getString(1),  // nom
                rs.getString(2),  // des
                rs.getInt(3)      // est
            )
            lista.add(bean)
        }

        return lista
    }



}