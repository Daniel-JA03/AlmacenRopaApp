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
        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Camisa Casual Hombre', 'Camisa de algodón manga larga', 1, 'H&M', 'CSH2024', 1, 45.50, 75.90, 100, 10, 'Estante A1', '2024-06-01', 'Activo', 'https://ejemplo.com/img/camisa1.jpg')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Pantalón Jeans Mujer', 'Jeans ajustado azul oscuro', 2, 'Levi''s', 'WJ305', 2, 60.00, 99.90, 50, 5, 'Estante B3', '2024-06-05', 'Activo', 'https://ejemplo.com/img/jeans1.jpg')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Camisa Blanca Formal', 'Ideal para oficina o eventos', 1, 'Zara', 'CWB450', 3, 52.75, 89.99, 80, 15, 'Estante A2', '2024-06-08', 'Activo', 'https://ejemplo.com/img/camisa_blanca.jpg')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Pantalón Chino Hombre', 'Pantalón casual beige', 2, 'Uniqlo', 'PCH780', 1, 40.00, 65.00, 60, 8, 'Estante B1', '2024-06-10', 'Activo', 'https://ejemplo.com/img/chino1.jpg')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Camisa Flanelada', 'Camisa a cuadros de invierno', 1, 'Old Navy', 'FLA2024', 2, 55.00, 85.00, 40, 5, 'Estante A4', '2024-06-12', 'Desactivado', 'https://ejemplo.com/img/flanelada.jpg')"
        )

        // Tabla localizacion (sedes)
        db.execSQL(
            "create table tb_sedes" +
                    "(" +
                    "codigo integer primary key autoincrement," +
                    "foto VARCHAR(200), " +
                    "nomDis varchar(30)," +
                    "canSedes INTEGER," +
                    "estado INTEGER" +
                    ")"
        )
        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Los Olivos', 2, 1)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tb_categoria")
        db.execSQL("DROP TABLE IF EXISTS tb_proveedor")
        db.execSQL("DROP TABLE IF EXISTS tb_sedes")

        // Agrega otras tablas si las tienes
        onCreate(db)
    }
}