package com.example.damll_punto_de_venta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VentasActivity extends AppCompatActivity {

    private OrdinarioBDHelper dbHelper;
    private ListView listViewProductos;
    private Button btnFinalizarVenta;
    private ProductoAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        dbHelper = new OrdinarioBDHelper(this);
        listViewProductos = findViewById(R.id.listViewProductos);
        btnFinalizarVenta = findViewById(R.id.btnFinalizarVenta);

        mostrarProductos();

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDialogoVenta(position);
            }
        });
        btnFinalizarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarVenta();
            }
        });
    }

    private void finalizarVenta() {
        Intent intent = new Intent(this, VentaDetalleActivity.class);
        startActivity(intent);
    }

    private void mostrarProductos() {
        List<Producto> listaProductos = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                OrdinarioBDHelper.COLUMN_ID,
                OrdinarioBDHelper.COLUMN_NOMBRE,
                OrdinarioBDHelper.COLUMN_PRECIO,
                OrdinarioBDHelper.COLUMN_IMAGEN
        };

        Cursor cursor = db.query(
                OrdinarioBDHelper.TABLE_PRODUCTS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_NOMBRE));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_PRECIO));
            String imagen = cursor.getString(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_IMAGEN));

            listaProductos.add(new Producto(id, nombre, precio, imagen));
        }

        cursor.close();

        productoAdapter = new ProductoAdapter(this, listaProductos);
        listViewProductos.setAdapter(productoAdapter);
    }

    private void mostrarDialogoVenta(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Venta");
        dialogBuilder.setMessage("Ingresa la cantidad vendida:");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cantidad_venta, null);
        dialogBuilder.setView(dialogView);

        final EditText etCantidadVenta = dialogView.findViewById(R.id.etCantidadVenta);

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cantidadStr = etCantidadVenta.getText().toString().trim();

                if (cantidadStr.isEmpty()) {
                    Toast.makeText(VentasActivity.this, "Por favor, ingresa la cantidad vendida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int cantidadVendida = Integer.parseInt(cantidadStr);
                if (cantidadVendida <= 0) {
                    Toast.makeText(VentasActivity.this, "La cantidad vendida debe ser mayor a 0.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Producto producto = productoAdapter.getItem(position);
                if (producto != null) {
                    guardarVenta(producto.getId(), cantidadVendida, producto.getPrecio());
                }
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

    private void guardarVenta(int idProducto, int cantidadVendida, double precioProducto) {
        double importe = cantidadVendida * precioProducto;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrdinarioBDHelper.COLUMN_ID_PRODUCTO, idProducto);
        values.put(OrdinarioBDHelper.COLUMN_CANTIDAD_VENDIDA, cantidadVendida);
        values.put(OrdinarioBDHelper.COLUMN_PRECIO_VENTA, precioProducto);
        values.put(OrdinarioBDHelper.COLUMN_IMPORTE, importe);

        long newRowId = db.insert(OrdinarioBDHelper.TABLE_VENTA, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Venta registrada correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al registrar la venta.", Toast.LENGTH_SHORT).show();
        }
    }
}