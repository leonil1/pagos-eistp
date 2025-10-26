package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Alumno;

import java.util.List;

public interface IAlumnoService {
    boolean CreateAlumno(Alumno alumno);
    Alumno GetAlumnoById(String dni);
    List<Alumno> obtenerTodosLosAlumnos();
    boolean UpdateAlumno(Alumno alumno);
}
