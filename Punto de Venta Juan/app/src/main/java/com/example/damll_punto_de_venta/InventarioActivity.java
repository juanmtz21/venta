package com.example.damll_punto_de_venta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class InventarioActivity extends AppCompatActivity {

    private OrdinarioBDHelper dbHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        dbHelper = new OrdinarioBDHelper(this);

        Button btnInsertarProducto = findViewById(R.id.btnInsertarProducto);
        Button btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        Button btnActualizarProducto = findViewById(R.id.btnActualizarProducto);

        btnInsertarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoInsertarProducto();
            }
        });

        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoEliminarProducto();
            }
        });

        btnActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoActualizarProducto();
            }
        });
    }

    private void mostrarDialogoInsertarProducto() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_insertar_producto, null);
        dialogBuilder.setView(dialogView);

        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        final EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        final EditText etImagen = dialogView.findViewById(R.id.etImagen);

        dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString().trim();
                String precioStr = etPrecio.getText().toString().trim();
                String cantidadStr = etCantidad.getText().toString().trim();
                String imagen = etImagen.getText().toString().trim();

                if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || imagen.isEmpty()) {
                    Toast.makeText(InventarioActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double precio = Double.parseDouble(precioStr);
                int cantidad = Integer.parseInt(cantidadStr);

                // Obtener la fecha actual
                String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(which));

                insertarProducto(nombre, precio, cantidad, imagen, fecha);
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void insertarProducto(String nombre, double precio, int cantidad, String imagen, String fecha) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrdinarioBDHelper.COLUMN_NOMBRE, nombre);
        values.put(OrdinarioBDHelper.COLUMN_PRECIO, precio);
        values.put(OrdinarioBDHelper.COLUMN_CANTIDAD, cantidad);
        values.put(OrdinarioBDHelper.COLUMN_IMAGEN, imagen);
        values.put(OrdinarioBDHelper.COLUMN_FECHA, fecha);

        long newRowId = db.insert(OrdinarioBDHelper.TABLE_PRODUCTS, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Producto insertado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al insertar el producto.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoEliminarProducto() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_eliminar_producto, null);
        dialogBuilder.setView(dialogView);

        final EditText etNombreProducto = dialogView.findViewById(R.id.etNombreProducto);

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreProducto = etNombreProducto.getText().toString().trim();

                if (nombreProducto.isEmpty()) {
                    Toast.makeText(InventarioActivity.this, "Por favor, ingresa el nombre del producto a eliminar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                eliminarProducto(nombreProducto);
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void eliminarProducto(String nombreProducto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = OrdinarioBDHelper.COLUMN_NOMBRE + "=?";
        String[] selectionArgs = {nombreProducto};

        int rowsDeleted = db.delete(OrdinarioBDHelper.TABLE_PRODUCTS, selection, selectionArgs);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Producto eliminado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró el producto o ocurrió un error.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoActualizarProducto() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_actualizar_producto, null);
        dialogBuilder.setView(dialogView);

        final EditText etNombreProducto = dialogView.findViewById(R.id.etNombreProducto);
        final EditText etNuevoPrecio = dialogView.findViewById(R.id.etNuevoPrecio);
        final EditText etNuevaCantidad = dialogView.findViewById(R.id.etNuevaCantidad);

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreProducto = etNombreProducto.getText().toString().trim();
                String nuevoPrecioStr = etNuevoPrecio.getText().toString().trim();
                String nuevaCantidadStr = etNuevaCantidad.getText().toString().trim();

                if (nombreProducto.isEmpty() || nuevoPrecioStr.isEmpty() || nuevaCantidadStr.isEmpty()) {
                    Toast.makeText(InventarioActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);

                actualizarProducto(nombreProducto, nuevoPrecio, nuevaCantidad);
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void actualizarProducto(String nombreProducto, double nuevoPrecio, int nuevaCantidad) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrdinarioBDHelper.COLUMN_PRECIO, nuevoPrecio);
        values.put(OrdinarioBDHelper.COLUMN_CANTIDAD, nuevaCantidad);

        String selection = OrdinarioBDHelper.COLUMN_NOMBRE + "=?";
        String[] selectionArgs = {nombreProducto};

        int rowsUpdated = db.update(OrdinarioBDHelper.TABLE_PRODUCTS, values, selection, selectionArgs);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Producto actualizado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró el producto o ocurrió un error.", Toast.LENGTH_SHORT).show();
        }
    }
}