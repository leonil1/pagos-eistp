package com.iestpdj.iestpdjpagos.model;

public class MetodoPago {
    private int idMetodo;
    private String nombre;
    private String descripcion;
    private boolean activo;

    // Getters y Setters
    public int getIdMetodo() { return idMetodo; }
    public void setIdMetodo(int idMetodo) { this.idMetodo = idMetodo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre; // Para mostrar el nombre directamente en ComboBox
    }
}
