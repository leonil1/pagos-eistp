package com.iestpdj.iestpdjpagos.model;


import java.time.LocalDateTime;

public class PagoReporte {
    private int id;
    private String estudiante;
    private double monto;
    private LocalDateTime fechaPago;
    private String estado;

    public PagoReporte(int id, String estudiante, double monto, LocalDateTime fechaPago, String estado) {
        this.id = id;
        this.estudiante = estudiante;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getEstudiante() { return estudiante; }
    public double getMonto() { return monto; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public String getEstado() { return estado; }
}
