package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.AlumnoDAO;
import com.iestpdj.iestpdjpagos.model.Alumno;

import java.util.List;

public class AlumnoService implements IAlumnoService {

    private AlumnoDAO alumnoDAO;
    public AlumnoService() {}

    @Override
    public boolean CreateAlumno(Alumno alumno) {
        if (!validarDatosRegistro(alumno)) {
            return false;
        }
        if (alumnoDAO.EliminarAlumno(alumno)!=null) {
            System.out.println("Alumno encontrado");
            return false;
        }

        Alumno alumnoSave = new Alumno();
        alumnoSave.setDni(alumno.getDni());
        alumnoSave.setNombre(alumno.getNombre());
        alumnoSave.setApellido_paterno(alumno.getApellido_paterno());
        alumnoSave.setApellido_materno(alumno.getApellido_materno());

        alumnoSave.setTelefono(alumno.getTelefono());



        return alumnoDAO.CreateAlumno(alumnoSave);
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return List.of();
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
