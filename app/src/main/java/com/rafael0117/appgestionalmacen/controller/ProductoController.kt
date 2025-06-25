package com.rafael0117.appgestionalmacen.controller

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.rafael0117.appgestionalmacen.entidad.Categoria
import com.rafael0117.appgestionalmacen.entidad.Producto
import com.rafael0117.appgestionalmacen.utils.appConfig

class ProductoController {

    fun findAll(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val cn: SQLiteDatabase = appConfig.BD.readableDatabase

        val sql = """
                    SELECT 
                p.codigo, 
                p.nombre, 
                p.descripcion, 
                c.cod AS categoria_codigo, 
                c.nom AS categoria_nombre, 
                p.marca, 
                p.modelo, 
                pr.codigo AS proveedor_codigo, 
                pr.nombre AS proveedor_nombre, 
                p.precio_compra, 
                p.precio_venta, 
                p.stock_actual, 
                p.stock_minimo, 
                p.ubicacion, 
                p.fecha_ingreso, 
                p.estado, 
                p.imagen
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
                rs.getInt(3), // nombre de categoría (c.nom)
                rs.getString(4), // nombre de categoría (c.nom)
                rs.getString(5), // p.marca
                rs.getString(6), // p.modelo
                rs.getInt(7), // nombre de proveedor (pr.nombre)
                rs.getString(8), // nombre de proveedor (pr.nombre)
                rs.getDouble(9), // p.precio_compra
                rs.getDouble(10), // p.precio_venta
                rs.getInt(11),    // p.stock_actual
                rs.getInt(12),   // p.stock_minimo
                rs.getString(13),// p.ubicacion
                rs.getString(14),// p.fecha_ingreso como String
                rs.getInt(15),// p.estado
                rs.getString(16) // p.imagen
            )

            lista.add(producto)
        }

        rs.close()
        return lista
    }
    fun adicionarProducto(bean: Producto): Int {
        var salida = -1
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        val row = ContentValues()

        row.put("nombre", bean.nombre)
        row.put("descripcion", bean.descripcion)
        row.put("categoria_codigo", bean.categoria)
        row.put("marca", bean.marca)
        row.put("modelo", bean.modelo)
        row.put("proveedor_codigo", bean.proveedor)
        row.put("precio_compra", bean.preciocompra)
        row.put("precio_venta", bean.precioventa)
        row.put("stock_actual", bean.stockActual)
        row.put("stock_minimo", bean.stockMinimo)
        row.put("ubicacion", bean.ubicacion)
        row.put("fecha_ingreso", bean.fechaIngreso)
        row.put("estado", bean.estado)
        row.put("imagen", bean.imagen)

        salida = cn.insert("tb_producto", null, row).toInt()

        return salida
    }
    fun editarProducto(bean: Producto):Int{
        var salida=-1
        var cn:SQLiteDatabase=appConfig.BD.writableDatabase
        var row=ContentValues()
        row.put("nombre", bean.nombre)
        row.put("descripcion", bean.descripcion)
        row.put("categoria_codigo", bean.categoria)
        row.put("marca", bean.marca)
        row.put("modelo", bean.modelo)
        row.put("proveedor_codigo", bean.proveedor)
        row.put("precio_compra", bean.preciocompra)
        row.put("precio_venta", bean.precioventa)
        row.put("stock_actual", bean.stockActual)
        row.put("stock_minimo", bean.stockMinimo)
        row.put("ubicacion", bean.ubicacion)
        row.put("fecha_ingreso", bean.fechaIngreso)
        row.put("estado", bean.estado)
        row.put("imagen", bean.imagen)
        salida=cn.update("tb_producto",row,"codigo=?", arrayOf(bean.codigo.toString()))
        return salida
    }
    fun eliminarCategoria(codigo: Int): Int {
        var salida = -1
        val cn: SQLiteDatabase = appConfig.BD.writableDatabase
        salida = cn.delete("tb_producto", "cod = ?", arrayOf(codigo.toString()))
        return salida
    }

    fun findProductosConStockMinimo(): List<Producto> {
        val lista = ArrayList<Producto>()
        val cn: SQLiteDatabase = appConfig.BD.readableDatabase

        // Consulta SQL modificada para alinearse con el constructor
        val sql = """
        SELECT 
            p.codigo, 
            p.nombre, 
            p.descripcion, 
            c.cod AS categoria_codigo, 
            c.nom AS categoria_nombre, 
            p.marca, 
            p.modelo, 
            pr.codigo AS proveedor_codigo, 
            pr.nombre AS proveedor_nombre, 
            p.precio_compra, 
            p.precio_venta, 
            p.stock_actual, 
            p.stock_minimo, 
            p.ubicacion, 
            p.fecha_ingreso, 
            p.estado, 
            p.imagen
        FROM tb_producto p
        JOIN tb_categoria c ON p.categoria_codigo = c.cod
        JOIN tb_proveedor pr ON p.proveedor_codigo = pr.codigo
        WHERE p.stock_actual <= p.stock_minimo
    """.trimIndent()

        val rs = cn.rawQuery(sql, null)

        while (rs.moveToNext()) {
            val producto = Producto(
                // Los parámetros deben coincidir EXACTAMENTE con el constructor
                rs.getInt(0),     // codigo
                rs.getString(1),  // nombre
                rs.getString(2),  // descripcion
                rs.getInt(3),     // categoria_codigo (int)
                rs.getString(4),  // categoria_nombre (string)
                rs.getString(5),  // marca
                rs.getString(6),  // modelo
                rs.getInt(7),     // proveedor_codigo (int)
                rs.getString(8),  // proveedor_nombre (string)
                rs.getDouble(9), // precio_compra
                rs.getDouble(10), // precio_venta
                rs.getInt(11),    // stock_actual
                rs.getInt(12),    // stock_minimo
                rs.getString(13), // ubicacion
                rs.getString(14), // fecha_ingreso (como String)
                rs.getInt(15),    // estado
                rs.getString(16)  // imagen
            )
            lista.add(producto)
        }

        rs.close()
        return lista
    }

}