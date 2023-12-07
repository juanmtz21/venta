package com.example.damll_punto_de_venta;

public class Venta {

    private int id;
    private int idProducto;
    private int cantidadVendida;
    private double precioVenta;
    private double importe;

    public Venta(int id, int idProducto, int cantidadVendida, double precioVenta, double importe) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidadVendida = cantidadVendida;
        this.precioVenta = precioVenta;
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getImporte() {
        return importe;
    }
}
