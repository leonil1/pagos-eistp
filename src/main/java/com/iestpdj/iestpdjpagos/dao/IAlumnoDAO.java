package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Estudiante;

import java.util.List;

public interface IAlumnoDAO {
    boolean CreateAlumno(Estudiante alumno);
    Estudiante buscarAlumno(String dni);
    List<Estudiante> obtenerTodosLosAlumnos();
    Boolean ActualizarAlumno(Estudiante alumno);
    Boolean EliminarAlumno(Long id);
}
