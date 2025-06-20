package com.rafael0117.appgestionalmacen.controller

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.entidad.SedeConCant
import com.rafael0117.appgestionalmacen.entidad.Ubicacion
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
                RS.getString(2), RS.getInt(3))
            // enviar objeto bean al arreglo lista
            lista.add(bean)
        }
        return lista
    }

    // Registrar nueva sede
    fun registrarSede(bean: Sede): Int {
        val db: SQLiteDatabase = appConfig.BD.writableDatabase
        val values = ContentValues()
        values.put("foto", bean.foto)
        values.put("nomDis", bean.nomDis)
        values.put("estado", bean.estado)
        return db.insert("tb_sedes", null, values).toInt()
    }

    // Registrar nueva ubicacion
    fun registrarUbicacion(sedeCodigo: Int, latitud: Double, longitud: Double, descripcion: String) {
        val db: SQLiteDatabase = appConfig.BD.writableDatabase
        val values = ContentValues()
        values.put("sede_codigo", sedeCodigo)
        values.put("latitud", latitud)
        values.put("longitud", longitud)
        values.put("descripcion", descripcion)

        db.insert("tb_ubicacion", null, values)
    }

    // Obtener ubicaciones por sede
    fun obtenerUbicacionesPorSede(sedeCodigo: Int): ArrayList<Ubicacion> {
        val lista = ArrayList<Ubicacion>()
        val db: SQLiteDatabase = appConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_ubicacion WHERE sede_codigo = ?", arrayOf(sedeCodigo.toString()))
        while (cursor.moveToNext()) {
            val ubicacion = Ubicacion(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getDouble(2),
                cursor.getDouble(3),
                cursor.getString(4)
            )
            lista.add(ubicacion)
        }
        cursor.close()
        return lista
    }

    // Obtener sedes con cantidad de ubicaciones
    fun findAllConCant(): ArrayList<SedeConCant> {
        val lista = ArrayList<SedeConCant>()
        val db = appConfig.BD.readableDatabase
        val query = """
        SELECT 
            s.codigo, 
            s.foto, 
            s.nomDis, 
            s.estado,
            COUNT(u.codigo) AS cantidadSedes
        FROM tb_sedes s
        LEFT JOIN tb_ubicacion u ON s.codigo = u.sede_codigo
        GROUP BY s.codigo
    """.trimIndent()

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val sede = SedeConCant(
                cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                cursor.getString(cursor.getColumnIndexOrThrow("nomDis")),
                cursor.getInt(cursor.getColumnIndexOrThrow("estado")),
                cursor.getInt(cursor.getColumnIndexOrThrow("cantidadSedes"))
            )
            lista.add(sede)
        }
        cursor.close()
        return lista
    }
}