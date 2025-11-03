package com.iestpdj.iestpdjpagos.model;

public class DetalleBoletaInfo {
    private String concepto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleBoletaInfo(String concepto, int cantidad,
                             double precioUnitario, double subtotal) {
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters
    public String getConcepto() { return concepto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
}
