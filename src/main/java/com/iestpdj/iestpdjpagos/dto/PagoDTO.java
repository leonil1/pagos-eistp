package com.iestpdj.iestpdjpagos.dto;

import java.time.LocalDate;

public class PagoDTO {

    private int id_pago;
    private String nombreEstudiante;
    private String usuario;
    private String metodoPago;
    private double montoTotal;
    private LocalDate fechaPago;
    private String estado;

    // Constructor vacío
    public PagoDTO() {}

    // Constructor completo
    public PagoDTO(int id_pago, String nombreEstudiante, String usuario, String metodoPago,
                   double montoTotal, LocalDate fechaPago, String estado) {
        this.id_pago = id_pago;
        this.nombreEstudiante = nombreEstudiante;
        this.usuario = usuario;
        this.metodoPago = metodoPago;
        this.montoTotal = montoTotal;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
