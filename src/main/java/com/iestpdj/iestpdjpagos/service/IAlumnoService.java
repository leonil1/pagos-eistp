package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Alumno;

import java.util.List;

public interface IAlumnoService {
    boolean CreateAlumno(Alumno alumno);
    List<Alumno> obtenerTodosLosAlumnos();
}
