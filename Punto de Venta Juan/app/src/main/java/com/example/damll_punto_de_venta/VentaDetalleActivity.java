package com.example.damll_punto_de_venta;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VentaDetalleActivity extends AppCompatActivity {

    private OrdinarioBDHelper dbHelper;
    private ListView listViewVenta;
    private VentaAdapter ventaAdapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_detalle);

        dbHelper = new OrdinarioBDHelper(this);
        listViewVenta = findViewById(R.id.listViewVenta);
        tvTotal = findViewById(R.id.tvTotal);

        mostrarVenta();
    }

    private void mostrarVenta() {
        List<Venta> listaVenta = new ArrayList<>();
        double totalVenta = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                OrdinarioBDHelper.COLUMN_ID,
                OrdinarioBDHelper.COLUMN_ID_PRODUCTO,
                OrdinarioBDHelper.COLUMN_CANTIDAD_VENDIDA,
                OrdinarioBDHelper.COLUMN_PRECIO_VENTA,
                OrdinarioBDHelper.COLUMN_IMPORTE
        };

        Cursor cursor = db.query(
                OrdinarioBDHelper.TABLE_VENTA,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_ID));
            int idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_ID_PRODUCTO));
            int cantidadVendida = cursor.getInt(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_CANTIDAD_VENDIDA));
            double precioVenta = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_PRECIO_VENTA));
            double importe = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdinarioBDHelper.COLUMN_IMPORTE));

            totalVenta += importe;

            listaVenta.add(new Venta(id, idProducto, cantidadVendida, precioVenta, importe));
        }

        cursor.close();

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        tvTotal.setText(decimalFormat.format(totalVenta));

        ventaAdapter = new VentaAdapter(this, listaVenta);
        listViewVenta.setAdapter(ventaAdapter);
    }
}