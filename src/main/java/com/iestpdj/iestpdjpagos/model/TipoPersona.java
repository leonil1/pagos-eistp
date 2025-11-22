package com.iestpdj.iestpdjpagos.model;

public enum TipoPersona {
    ESTUDINATE("Estudiante"),
    DOCENTE("Docente"),
    ADMINTRATIVO("Admintrativo"),
    EXTERNO("Externo");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
