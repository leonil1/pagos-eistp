package com.iestpdj.iestpdjpagos.model;

import java.time.LocalDateTime;

public class Estudiante {

    private Long id;
    private String dni;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String direccion;
    private String email;
    private String telefono;
    private boolean activo;
    private String tipo_persona;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_modificacion;
    private String nombreCompleto;

    public Estudiante() {
        super();
    }

    public Estudiante(String dni, String nombre, String apellido_paterno, String apellido_materno,
                      String direccion, String email, String telefono, boolean activo, String tipo_persona,
                      LocalDateTime fecha_creacion, LocalDateTime fecha_modificacion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.tipo_persona = tipo_persona;
        this.fecha_creacion = fecha_creacion;
        this.fecha_modificacion = fecha_modificacion;
    }

    public Estudiante(long id, String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String email,
                      String direccion, String telefono, boolean activo, String tipo_persona) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido_paterno = apellidoPaterno;
        this.apellido_materno = apellidoMaterno;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.tipo_persona = tipo_persona;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido_paterno + " " + apellido_materno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDateTime getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(LocalDateTime fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public String getTipo_persona() {
        return tipo_persona;
    }

    public void setTipo_persona(String tipo_persona) {
        this.tipo_persona = tipo_persona;
    }
}
