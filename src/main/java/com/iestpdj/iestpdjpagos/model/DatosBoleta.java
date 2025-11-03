package com.iestpdj.iestpdjpagos.model;

import java.sql.Timestamp;

public class DatosBoleta {
    private String numeroBoleta;
    private Timestamp fechaEmision;
    private double total;
    private String nombreEstudiante;
    private String dniEstudiante;
    private String cajero;
    private String metodoPago;

    public DatosBoleta(String numeroBoleta, Timestamp fechaEmision, double total,
                       String nombreEstudiante, String dniEstudiante,
                       String cajero, String metodoPago) {
        this.numeroBoleta = numeroBoleta;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.nombreEstudiante = nombreEstudiante;
        this.dniEstudiante = dniEstudiante;
        this.cajero = cajero;
        this.metodoPago = metodoPago;
    }

    // Getters
    public String getNumeroBoleta() { return numeroBoleta; }
    public Timestamp getFechaEmision() { return fechaEmision; }
    public double getTotal() { return total; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getDniEstudiante() { return dniEstudiante; }
    public String getCajero() { return cajero; }
    public String getMetodoPago() { return metodoPago; }
}
