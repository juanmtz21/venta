package com.example.damll_punto_de_venta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrdinarioBDHelper extends SQLiteOpenHelper {
    public static final String TABLE_VENTA = "venta";
    private static final String DATABASE_NAME = "OrdinarioBD";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PRODUCTS = "productos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_IMAGEN = "imagen";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_IMAGEN_URL = "imagenUrl";

    public static final String COLUMN_ID_PRODUCTO = "id_producto";
    public static final String COLUMN_CANTIDAD_VENDIDA = "cantidad_vendida";
    public static final String COLUMN_PRECIO_VENTA = "precio_venta";
    public static final String COLUMN_IMPORTE = "importe";
    public static final String COLUMN_FECHA_VENTA = "fecha_venta";

    // Sentencia SQL para crear la tabla "productos"
    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_PRECIO + " REAL, " +
                    COLUMN_CANTIDAD + " INTEGER, " +
                    COLUMN_IMAGEN + " TEXT, " +
                    COLUMN_FECHA + " DATE);";

    private static final String CREATE_TABLE_VENTA =
            "CREATE TABLE " + TABLE_VENTA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ID_PRODUCTO + " INTEGER, " +
                    COLUMN_CANTIDAD_VENDIDA + " INTEGER, " +
                    COLUMN_PRECIO_VENTA + " REAL, " +
                    COLUMN_IMPORTE + " REAL, " +
                    COLUMN_FECHA_VENTA + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_ID_PRODUCTO + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + ")" +
                    ")";

    public OrdinarioBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_VENTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENTA);
        onCreate(db);
    }
}
