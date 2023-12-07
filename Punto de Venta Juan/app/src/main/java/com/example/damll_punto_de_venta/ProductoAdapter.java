package com.example.damll_punto_de_venta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Context context;
    private List<Producto> productos;

    public ProductoAdapter(Context context, List<Producto> productos) {
        super(context, 0, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_producto, parent, false);
        }

        Producto producto = productos.get(position);

        TextView tvIdProducto = listItemView.findViewById(R.id.tvIdProducto);
        TextView tvNombreProducto = listItemView.findViewById(R.id.tvNombreProducto);
        TextView tvPrecioProducto = listItemView.findViewById(R.id.tvPrecioProducto);
        TextView tvImagenProducto = listItemView.findViewById(R.id.tvImagenProducto);

        tvIdProducto.setText("ID de Producto: " + producto.getId());
        tvNombreProducto.setText("Nombre del Producto: " + producto.getNombre());
        tvPrecioProducto.setText("Precio: " + producto.getPrecio());
        tvImagenProducto.setText("Nombre de la Imagen: " + producto.getImagen());

        return listItemView;
    }
}
