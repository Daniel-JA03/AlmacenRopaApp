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
                    "VALUES ('Camisa Casual Hombre', 'Camisa de algodón manga larga', 1, 'H&M', 'CSH2024', 1, 45.50, 75.90, 100, 10, 'Estante A1', '2024-06-01', 'Activo', 'camisa.jpg')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Pantalón Jeans Mujer', 'Jeans ajustado azul oscuro', 2, 'Levi''s', 'WJ305', 2, 60.00, 99.90, 50, 5, 'Estante B3', '2024-06-05', 'Activo', 'camisa')"
        )

        db.execSQL(
            "INSERT INTO tb_producto (nombre, descripcion, categoria_codigo, marca, modelo, proveedor_codigo, precio_compra, precio_venta, stock_actual, stock_minimo, ubicacion, fecha_ingreso, estado, imagen) " +
                    "VALUES ('Camisa Blanca Formal', 'Ideal para oficina o eventos', 1, 'Zara', 'CWB450', 3, 52.75, 89.99, 80, 15, 'Estante A2', '2024-06-08', 'Activo', 'camisa')"
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
                    "estado INTEGER" +
                    ")"
        )
        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Los Olivos',  1)"
        )

        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Miraflores',  1)"
        )

        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Santiago de Surco',  1)"
        )

        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Comas',  1)"
        )

        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'San Miguel',  1)"
        )

        db.execSQL(
            "INSERT INTO tb_sedes values(null, 'localizacion_sedes', 'Independencia',  1)"
        )


        // tabla ubicacion
        db.execSQL(
            "create table tb_ubicacion" +
                    "(" +
                    "codigo integer primary key autoincrement," +
                    "sede_codigo INTEGER, " +
                    "latitud DOUBLE," +
                    "longitud DOUBLE," +
                    "descripcion varchar(80), " +
                    "FOREIGN KEY (sede_codigo) REFERENCES tb_sedes(codigo) " +
                    ")"
        )
        // Ubicaciones - Los Olivos
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 1, -12.0063303, -77.0831586, 'Sucursal A - Los Olivos')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 1, -12.0078200, -77.0811000, 'Sucursal B - Los Olivos')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 1, -11.992568, -77.073012, 'Sucursal C - Los Olivos')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 1, -11.962341, -77.078333, 'Sucursal D - Los Olivos')"
        )

        // Ubicaciones - Miraflores
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 2, -12.125799, -77.036627, 'Sucursal A - Miraflores')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 2, -12.123782, -77.012108, 'Sucursal B - Miraflores')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 2, -12.129775, -77.019694, 'Sucursal C - Miraflores')"
        )

        // Ubicaciones - Santiago de Surco
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 3, -12.145644, -77.002987, 'Sucursal A - Santiago de Surco')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 3, -12.092608, -76.970543, 'Sucursal B - Santiago de Surco')"
        )

        // Ubicaciones - Comas
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 4, -11.911032, -77.047821, 'Sucursal A - Comas')"
        )

        // Ubicaciones - San Miguel
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 5, -12.073500, -77.095145, 'Sucursal A - San Miguel')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 5, -12.063260, -77.083644, 'Sucursal B - San Miguel')"
        )

        // Ubicaciones - Independencia
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 6, -11.992270, -77.049117, 'Sucursal A - Independencia')"
        )
        db.execSQL(
            "INSERT INTO tb_ubicacion values(null, 6, -11.976905, -77.051864, 'Sucursal B - Independencia')"
        )

        db.execSQL(
            "CREATE TABLE tb_movimiento (" +
                    "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo VARCHAR(20) NOT NULL, " +
                    "fecha VARCHAR(20) NOT NULL, " +
                    "sede_origen INTEGER, " +
                    "sede_destino INTEGER, " +
                    "observaciones VARCHAR(200), " +
                    "FOREIGN KEY (sede_origen) REFERENCES tb_sedes(codigo), " +
                    "FOREIGN KEY (sede_destino) REFERENCES tb_sedes(codigo)" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE tb_detalle_movimiento (" +
                    "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "movimiento_codigo INTEGER, " +
                    "producto_codigo INTEGER, " +
                    "cantidad INTEGER, " +
                    "FOREIGN KEY (movimiento_codigo) REFERENCES tb_movimiento(codigo), " +
                    "FOREIGN KEY (producto_codigo) REFERENCES tb_producto(codigo)" +
                    ")"
        )






    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS tb_ubicacion")
        db.execSQL("DROP TABLE IF EXISTS tb_sedes")
        db.execSQL("DROP TABLE IF EXISTS tb_producto")
        db.execSQL("DROP TABLE IF EXISTS tb_proveedor")
        db.execSQL("DROP TABLE IF EXISTS tb_categoria")
        // Agrega otras tablas si las tienes
        onCreate(db)
    }
}