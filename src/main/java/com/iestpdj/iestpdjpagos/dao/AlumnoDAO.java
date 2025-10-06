package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Alumno;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO implements IAlumnoDAO{

    private Connection connection;

    public AlumnoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    @Override
    public boolean CreateAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumno (dni, nombre, apellido_paterno, apellido_materno, email, telefono, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getDni());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getApellido_paterno());
            pstmt.setString(4, alumno.getApellido_materno());
            pstmt.setString(5, alumno.getEmail());
            pstmt.setString(6, alumno.getTelefono());
            pstmt.setBoolean(7, alumno.isActivo());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            System.out.println("Error al crear alumno: " + e.getMessage());
            return false;
        }
    }


    @Override
    public Alumno buscarAlumno(String dni) {
        return null;
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumno WHERE activo = true";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getLong("id"));
                alumno.setDni(rs.getString("dni"));
                alumno.setNombre(rs.getString("nombres"));
                alumno.setApellido_paterno(rs.getString("apellido_paterno"));
                alumno.setApellido_materno(rs.getString("apellido_materno"));
                alumno.setEmail(rs.getString("correo"));
                alumno.setTelefono(rs.getString("telefono"));
                alumno.setActivo(rs.getBoolean("activo"));
                lista.add(alumno);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener alumnos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Boolean ActualizarAlumno(Alumno alumno) {
        return null;
    }

    @Override
    public Boolean EliminarAlumno(Alumno alumno) {
        return null;
    }
}
