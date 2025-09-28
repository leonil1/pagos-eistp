package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Alumno;
import com.iestpdj.iestpdjpagos.model.Usuario;

import java.util.List;

public interface IAlumnoDAO {
    boolean CreateAlumno(Alumno alumno);
    Alumno buscarAlumno(String dni);
    List<Alumno> obtenerTodosLosAlumnos();
    Boolean ActualizarAlumno(Alumno alumno);
    Boolean EliminarAlumno(Alumno alumno);
}
