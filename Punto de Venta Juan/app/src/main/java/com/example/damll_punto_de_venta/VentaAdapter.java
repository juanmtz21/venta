package com.example.damll_punto_de_venta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class VentaAdapter extends ArrayAdapter<Venta> {

    private Context context;
    private List<Venta> ventas;

    public VentaAdapter(Context context, List<Venta> ventas) {
        super(context, 0, ventas);
        this.context = context;
        this.ventas = ventas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_venta, parent, false);
        }

        Venta venta = ventas.get(position);

        TextView tvIdProducto = listItemView.findViewById(R.id.tvIdProductoVenta);
        TextView tvCantidadVendida = listItemView.findViewById(R.id.tvCantidadVendida);
        TextView tvPrecioVenta = listItemView.findViewById(R.id.tvPrecioVenta);
        TextView tvImporte = listItemView.findViewById(R.id.tvImporte);

        tvIdProducto.setText("ID de Producto: " + venta.getIdProducto());
        tvCantidadVendida.setText("Cantidad Vendida: " + venta.getCantidadVendida());
        tvPrecioVenta.setText("Precio de Venta: " + formatDecimal(venta.getPrecioVenta()));
        tvImporte.setText("Importe: " + formatDecimal(venta.getImporte()));

        return listItemView;
    }

    private String formatDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }
}
