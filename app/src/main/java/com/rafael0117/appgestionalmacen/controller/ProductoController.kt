package com.rafael0117.appgestionalmacen.controller

import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.utils.appConfig

class ProductoController {

    fun findAll(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val cn: SQLiteDatabase = appConfig.BD.readableDatabase

        val sql = """
        SELECT 
            p.codigo, p.nombre, p.descripcion, 
            c.nom AS categoria, 
            p.marca, p.modelo, 
            pr.nombre AS proveedor, 
            p.precio_compra, p.precio_venta, 
            p.stock_actual, p.stock_minimo, 
            p.ubicacion, p.fecha_ingreso, 
            p.estado, p.imagen
        FROM tb_producto p
        JOIN tb_categoria c ON p.categoria_codigo = c.cod
        JOIN tb_proveedor pr ON p.proveedor_codigo = pr.codigo
    """.trimIndent()

        val rs = cn.rawQuery(sql, null)

        while (rs.moveToNext()) {
            // En lugar de parsear la fecha, se deja como String
            val producto = Producto(
                rs.getInt(0),    // p.codigo
                rs.getString(1), // p.nombre
                rs.getString(2), // p.descripcion
                rs.getString(3), // nombre de categoría (c.nom)
                rs.getString(4), // p.marca
                rs.getString(5), // p.modelo
                rs.getString(6), // nombre de proveedor (pr.nombre)
                rs.getDouble(7), // p.precio_compra
                rs.getDouble(8), // p.precio_venta
                rs.getInt(9),    // p.stock_actual
                rs.getInt(10),   // p.stock_minimo
                rs.getString(11),// p.ubicacion
                rs.getString(12),// p.fecha_ingreso como String
                rs.getInt(13),// p.estado
                rs.getString(14) // p.imagen
            )

            lista.add(producto)
        }

        rs.close()
        return lista
    }
}