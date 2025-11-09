package com.iestpdj.iestpdjpagos.dto;

public class ItemPagoDTO {
    private String descripcion;
    private double monto;

    public ItemPagoDTO(String descripcion, double monto) {
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
}
