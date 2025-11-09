package com.iestpdj.iestpdjpagos.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DetallePagoView {

    private int idConcepto;
    private String concepto;
    private IntegerProperty cantidad;
    private double precioUnitario;
    private DoubleProperty subtotal;

    public DetallePagoView(int idConcepto, String concepto, int cantidad, double precioUnitario) {
        this.idConcepto = idConcepto;
        this.concepto = concepto;
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioUnitario = precioUnitario;
        this.subtotal = new SimpleDoubleProperty(cantidad * precioUnitario);

        // Listener para actualizar subtotal cuando cambia la cantidad
        this.cantidad.addListener((obs, oldVal, newVal) -> {
            this.subtotal.set(newVal.intValue() * precioUnitario);
        });
    }
    public int getIdConcepto() { return idConcepto; }
    public String getConcepto() { return concepto; }

    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int cantidad) { this.cantidad.set(cantidad); }
    public IntegerProperty cantidadProperty() { return cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }

    public double getSubtotal() { return subtotal.get(); }
    public DoubleProperty subtotalProperty() { return subtotal; }
}
