package com.iestpdj.iestpdjpagos.model;

import javafx.beans.property.*;

public class ConceptoPago {
    private IntegerProperty id_concepto;
    private StringProperty nombre;
    private StringProperty descripcion;
    private DoubleProperty precio;
    private StringProperty estado =  new SimpleStringProperty();;

    public ConceptoPago() {
        this.id_concepto = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.precio = new SimpleDoubleProperty();
    }

    public ConceptoPago(int id_concepto, String nombre, String descripcion, double precio, String estado) {
        this.id_concepto = new SimpleIntegerProperty(id_concepto);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleDoubleProperty(precio);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId_concepto() {
        return id_concepto.get();
    }

    public IntegerProperty id_conceptoProperty() {
        return id_concepto;
    }

    public void setId_concepto(int id_concepto) {
        this.id_concepto.set(id_concepto);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    @Override
    public String toString() {
        return nombre.get() + " - S/ " + precio.get();
    }
}