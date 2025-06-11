package com.rafael0117.appgestionalmacen.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rafael0117.appgestionalmacen.utils.appConfig

class InitBD: SQLiteOpenHelper(appConfig.CONTEXT,appConfig.BD_NAME,null,
    appConfig.VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL( "create table tb_categoria" +
                "(" +
                "cod integer primary key autoincrement," +
                "nom varchar(30)," +
                "des varchar(90)" +
                ")")
        db.execSQL(
            "CREATE TABLE tb_proveedor (" +
                    "codigo VARCHAR(10) PRIMARY KEY AUTOINCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "telefono VARCHAR(20), " +
                    "correo VARCHAR(50), " +
                    "direccion VARCHAR(100), " +
                    "contacto VARCHAR(50)" +
                    ")"
        )

        // Tabla Producto
        db.execSQL(
            "CREATE TABLE tb_producto (" +
                    "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "descripcion VARCHAR(100), " +
                    "categoria_codigo VARCHAR(10), " +     // clave foránea
                    "marca VARCHAR(30), " +
                    "modelo VARCHAR(30), " +
                    "proveedor_codigo VARCHAR(10), " +     // clave foránea
                    "precio_compra REAL, " +
                    "precio_venta REAL, " +
                    "stock_actual INTEGER, " +
                    "stock_minimo INTEGER, " +
                    "ubicacion VARCHAR(50), " +
                    "fecha_ingreso VARCHAR(20), " +        // formato: yyyy-MM-dd
                    "estado VARCHAR(20), " +
                    "imagen VARCHAR(200), " +
                    "FOREIGN KEY (categoria_codigo) REFERENCES tb_categoria(codigo), " +
                    "FOREIGN KEY (proveedor_codigo) REFERENCES tb_proveedor(codigo)" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}