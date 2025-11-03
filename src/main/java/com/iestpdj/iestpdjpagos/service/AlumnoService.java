package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.EstudianteDAO;
import com.iestpdj.iestpdjpagos.dao.IAlumnoDAO;
import com.iestpdj.iestpdjpagos.model.Estudiante;

import java.util.List;

public class AlumnoService implements IAlumnoService {

    private IAlumnoDAO alumnoDAO;

    public AlumnoService() {
        this.alumnoDAO = new EstudianteDAO();
    }

    @Override
    public boolean CreateAlumno(Estudiante alumno) {
        if (!validarDatosRegistro(alumno)) {
            return false;
        }
       if (alumnoDAO.buscarAlumno(alumno.getDni()) != null) {
           System.out.println("El Alumno ya existe" + alumno.getDni());
           return false;
       }

        Estudiante alumnoSave = new Estudiante();
        alumnoSave.setDni(alumno.getDni());
        alumnoSave.setNombre(alumno.getNombre());
        alumnoSave.setApellido_paterno(alumno.getApellido_paterno());
        alumnoSave.setApellido_materno(alumno.getApellido_materno());
        alumnoSave.setDireccion(alumno.getDireccion());
        alumnoSave.setTelefono(alumno.getTelefono());

        return alumnoDAO.CreateAlumno(alumnoSave);
    }

    @Override
    public Estudiante GetAlumnoById(String dni) {
        if (dni == null || dni.isEmpty()) {
            return null;
        }
        return alumnoDAO.buscarAlumno(dni);
    }

    @Override
    public List<Estudiante> obtenerTodosLosAlumnos() {
        return alumnoDAO.obtenerTodosLosAlumnos();
    }

    @Override
    public boolean UpdateAlumno(Estudiante alumno) {
        return alumnoDAO.ActualizarAlumno(alumno);
    }

    private boolean validarDatosRegistro( Estudiante alumno) {
        if (alumno.getNombre() == null || alumno.getNombre().trim().isEmpty()) {
            System.out.println("El nombre no puede ser vacio");
            return false;
        }

        if (alumno.getApellido_paterno() == null || alumno.getApellido_materno().trim().isEmpty()) {
            System.out.println("El nombre no puede ser vacio");
            return false;
        }

        return true;
    }
}
