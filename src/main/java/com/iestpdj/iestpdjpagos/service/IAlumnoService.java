package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Estudiante;

import java.util.List;

public interface IAlumnoService {
    boolean CreateAlumno(Estudiante alumno);
    Estudiante GetAlumnoById(String dni);
    List<Estudiante> obtenerTodosLosAlumnos();
    boolean UpdateAlumno(Estudiante alumno);
}
