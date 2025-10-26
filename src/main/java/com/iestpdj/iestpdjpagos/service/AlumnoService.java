package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.AlumnoDAO;
import com.iestpdj.iestpdjpagos.dao.IAlumnoDAO;
import com.iestpdj.iestpdjpagos.model.Alumno;

import java.util.List;

public class AlumnoService implements IAlumnoService {

    private IAlumnoDAO alumnoDAO;

    public AlumnoService() {
        this.alumnoDAO = new AlumnoDAO();
    }

    @Override
    public boolean CreateAlumno(Alumno alumno) {
        if (!validarDatosRegistro(alumno)) {
            return false;
        }
       if (alumnoDAO.buscarAlumno(alumno.getDni()) != null) {
           System.out.println("El Alumno ya existe" + alumno.getDni());
           return false;
       }

        Alumno alumnoSave = new Alumno();
        alumnoSave.setDni(alumno.getDni());
        alumnoSave.setNombre(alumno.getNombre());
        alumnoSave.setApellido_paterno(alumno.getApellido_paterno());
        alumnoSave.setApellido_materno(alumno.getApellido_materno());
        alumnoSave.setDireccion(alumno.getDireccion());
        alumnoSave.setTelefono(alumno.getTelefono());

        return alumnoDAO.CreateAlumno(alumnoSave);
    }

    @Override
    public Alumno GetAlumnoById(String dni) {
        if (dni == null || dni.isEmpty()) {
            return null;
        }
        return alumnoDAO.buscarAlumno(dni);
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoDAO.obtenerTodosLosAlumnos();
    }

    @Override
    public boolean UpdateAlumno(Alumno alumno) {
        return alumnoDAO.ActualizarAlumno(alumno);
    }

    private boolean validarDatosRegistro( Alumno alumno) {
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
