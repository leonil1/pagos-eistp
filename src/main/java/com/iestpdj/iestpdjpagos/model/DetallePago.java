package com.iestpdj.iestpdjpagos.model;

public class DetallePago {
    private ConceptoPago concepto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetallePago(ConceptoPago concepto, int cantidad) {
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.precioUnitario = concepto.getPrecio();
        this.subtotal = precioUnitario * cantidad;
    }

    public ConceptoPago getConcepto() {
        return concepto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = precioUnitario * cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
