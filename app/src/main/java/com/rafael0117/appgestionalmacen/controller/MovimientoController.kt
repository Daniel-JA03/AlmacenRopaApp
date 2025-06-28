package com.rafael0117.appgestionalmacen.controller

import android.content.ContentValues
import com.rafael0117.appgestionalmacen.entidad.DetalleMovimiento
import com.rafael0117.appgestionalmacen.entidad.Movimiento
import com.rafael0117.appgestionalmacen.utils.appConfig

class MovimientoController {
    // Agregar un movimiento (cabecera y detalle)
    fun registrarMovimiento(movimiento: Movimiento, detalles: List<DetalleMovimiento>): Long {
        val db = appConfig.BD.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put("tipo", movimiento.tipo)
            values.put("fecha", movimiento.fecha)
            values.put("sede_origen", movimiento.sedeOrigen)
            values.put("sede_destino", movimiento.sedeDestino)
            values.put("observaciones", movimiento.observaciones)

            val movimientoId = db.insert("tb_movimiento", null, values)
            if (movimientoId == -1L) return -1

            for (detalle in detalles) {
                val detalleValues = ContentValues()
                detalleValues.put("movimiento_codigo", movimientoId)
                detalleValues.put("producto_codigo", detalle.productoCodigo)
                detalleValues.put("cantidad", detalle.cantidad)
                db.insert("tb_detalle_movimiento", null, detalleValues)
            }

            db.setTransactionSuccessful()
            return movimientoId
        } catch (e: Exception) {
            return -1
        } finally {
            db.endTransaction()
        }
    }

    // Listar todos los movimientos
    fun listarMovimientos(): ArrayList<Movimiento> {
        val lista = ArrayList<Movimiento>()
        val db = appConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_movimiento", null)
        while (cursor.moveToNext()) {
            val movimiento = Movimiento(
                codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                sedeOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("sede_origen")),
                sedeDestino = cursor.getInt(cursor.getColumnIndexOrThrow("sede_destino")),
                observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
            )
            lista.add(movimiento)
        }
        return lista
    }

    // Listar detalles de un movimiento específico
    fun listarDetallesDeMovimiento(movimientoId: Int): ArrayList<DetalleMovimiento> {
        val lista = ArrayList<DetalleMovimiento>()
        val db = appConfig.BD.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM tb_detalle_movimiento WHERE movimiento_codigo = ?",
            arrayOf(movimientoId.toString())
        )
        while (cursor.moveToNext()) {
            val detalle = DetalleMovimiento(
                codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                movimientoCodigo = cursor.getInt(cursor.getColumnIndexOrThrow("movimiento_codigo")),
                productoCodigo = cursor.getInt(cursor.getColumnIndexOrThrow("producto_codigo")),
                cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
            )
            lista.add(detalle)
        }
        return lista
    }
}