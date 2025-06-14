package com.rafael0117.appgestionalmacen.controller

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Proveedor
import com.rafael0117.appgestionalmacen.utils.appConfig

class ProveedorController {
    fun findAll(): ArrayList<Proveedor> {
        val lista = ArrayList<Proveedor>()
        val cn: SQLiteDatabase = appConfig.BD.readableDatabase
        val rs: Cursor = cn.rawQuery("SELECT * FROM tb_proveedor", null)

        while (rs.moveToNext()) {
            val bean = Proveedor(
                rs.getInt(0),  // codigo
                rs.getString(1),  // nombre
                rs.getString(2),  // telefono
                rs.getString(3),  // correo
                rs.getString(4),  // direccion
                rs.getString(5),  // contacto
                rs.getInt(6)      // est
            )
            lista.add(bean)
        }

        return lista
    }

    fun adicionarProveedor(bean: Proveedor): Int {
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        val row = ContentValues()
        row.put("nombre", bean.nombre)
        row.put("telefono", bean.telefono)
        row.put("correo", bean.correo)
        row.put("direccion", bean.direccion)
        row.put("contacto", bean.contacto)
        row.put("est", bean.estado)

        return cn.insert("tb_proveedor", null, row).toInt()
    }

    fun editarProveedor(bean: Proveedor): Int {
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        val row = ContentValues()
        row.put("nombre", bean.nombre)
        row.put("telefono", bean.telefono)
        row.put("correo", bean.correo)
        row.put("direccion", bean.direccion)
        row.put("contacto", bean.contacto)
        row.put("est", bean.estado)

        return cn.update("tb_proveedor", row, "codigo = ?", arrayOf(bean.codigo.toString()))
    }

    fun eliminarProveedor(codigo: Int): Int {
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        return cn.delete("tb_proveedor", "codigo = ?", arrayOf(codigo.toString()))
    }

    fun buscarProveedor(input: String): ArrayList<Proveedor> {
        val lista = ArrayList<Proveedor>()
        val cn: SQLiteDatabase = appConfig.BD.readableDatabase

        val rs = if (input.all { it.isDigit() }) {
            cn.rawQuery("SELECT * FROM tb_proveedor WHERE codigo = ?", arrayOf(input))
        } else {
            cn.rawQuery("SELECT * FROM tb_proveedor WHERE nombre LIKE ?", arrayOf("%$input%"))
        }

        while (rs.moveToNext()) {
            val bean = Proveedor(
                rs.getInt(0),
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getInt(6)
            )
            lista.add(bean)
        }

        return lista
    }
}