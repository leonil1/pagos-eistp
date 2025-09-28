package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Alumno;

import java.util.List;

public class AlumnoService implements IAlumnoService {

    @Override
    public boolean CreateAlumno(Alumno alumno) {
        return false;
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return List.of();
    }
}
