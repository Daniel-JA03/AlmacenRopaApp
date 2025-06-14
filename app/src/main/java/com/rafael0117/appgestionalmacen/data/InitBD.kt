package com.rafael0117.appgestionalmacen.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rafael0117.appgestionalmacen.utils.appConfig

class InitBD: SQLiteOpenHelper(appConfig.CONTEXT,appConfig.BD_NAME,null,
    appConfig.VERSION) {
    override fun onCreate(db: SQLiteDatabase) {


        db.execSQL(
            "create table tb_categoria" +
                "(" +
                "cod integer primary key autoincrement," +
                "nom varchar(30)," +
                "des varchar(90)," +
                "est INTEGER" +
                ")")
        db.execSQL(
            "INSERT INTO tb_categoria (nom, des, est) VALUES ('Camisas', 'Ropa superior de vestir para hombres y mujeres',1)"

        )
        db.execSQL(
            "INSERT INTO tb_categoria (nom, des, est) VALUES ('Pantalones', 'Prendas inferiores en distintas tallas y estilos',0)"
        )


        db.execSQL(
            "CREATE TABLE tb_proveedor (" +
                    "codigo integer PRIMARY KEY AUTOINCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "telefono VARCHAR(20), " +
                    "correo VARCHAR(50), " +
                    "direccion VARCHAR(100), " +
                    "contacto VARCHAR(50)," +
                    "est INTEGER" +
                    ")"
        )
        // Insertar datos de ejemplo en tb_proveedor (proveedores de ropa)
        db.execSQL("INSERT INTO tb_proveedor (nombre, telefono, correo, direccion, contacto, est) VALUES " +
                "('Moda Perú S.A.C.', '987654321', 'ventas@modaperu.com', 'Av. Grau 1234, Lima', 'Carlos Rivas', 1)")

        db.execSQL("INSERT INTO tb_proveedor (nombre, telefono, correo, direccion, contacto, est) VALUES " +
                "('Textiles Andinos', '945678900', 'contacto@textilesandinos.pe', 'Jr. Junín 876, Huancayo', 'María Cárdenas', 1)")

        db.execSQL("INSERT INTO tb_proveedor (nombre, telefono, correo, direccion, contacto, est) VALUES " +
                "('Ropa al Por Mayor S.R.L.', '913456789', 'info@ropamayor.com', 'Av. Industrial 567, Gamarra - Lima', 'Jorge Mendoza', 0)")


        // Tabla Producto
        db.execSQL(
            "CREATE TABLE tb_producto (" +
                    "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "descripcion VARCHAR(100), " +
                    "categoria_codigo integer, " +     // clave foránea
                    "marca VARCHAR(30), " +
                    "modelo VARCHAR(30), " +
                    "proveedor_codigo integer, " +     // clave foránea
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

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tb_categoria")
        db.execSQL("DROP TABLE IF EXISTS tb_proveedor")

        // Agrega otras tablas si las tienes
        onCreate(db)
    }
}