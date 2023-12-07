package com.example.damll_punto_de_venta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPantallaInventario = findViewById(R.id.btnPantallaInventario);
        Button btnPantallaVenta = findViewById(R.id.btnPantallaVenta);
        Button btnSalir = findViewById(R.id.btnSalir);

        btnPantallaInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de inventario al hacer clic en el botón "Pantalla Inventario"
                Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
                startActivity(intent);
            }
        });

        btnPantallaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VentasActivity.class);
                startActivity(intent);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la aplicación al presionar el botón "Salir"
                finish();
            }
        });
    }
}