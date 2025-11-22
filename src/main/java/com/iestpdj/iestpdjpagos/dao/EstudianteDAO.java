package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Estudiante;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements IAlumnoDAO{

    private Connection connection;

    public EstudianteDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    @Override
    public boolean CreateAlumno(Estudiante alumno) {
        String sql = "INSERT INTO estudiante (dni, nombre, apellido_paterno, apellido_materno, direccion, email, telefono, activo, tipo_persona) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getDni());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getApellido_paterno());
            pstmt.setString(4, alumno.getApellido_materno());
            pstmt.setString(5, alumno.getDireccion());
            pstmt.setString(6, alumno.getEmail());
            pstmt.setString(7, alumno.getTelefono());
            pstmt.setBoolean(8, alumno.isActivo());
            pstmt.setString(9, alumno.getTipo_persona());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear alumno: " + e.getMessage());
            return false;
        }
    }


    @Override
    public Estudiante buscarAlumno(String dni) {
        String sql = "SELECT * FROM estudiante WHERE dni = ? AND activo = TRUE";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Estudiante(
                        rs.getLong("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getBoolean("activo"),
                        rs.getString("tipo_persona")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Estudiante> obtenerTodosLosAlumnos() {

        List<Estudiante> lista = new ArrayList<>();

        String sql = """
        SELECT 
           id,
           dni,
           nombre,
           apellido_paterno,
           apellido_materno,
           direccion,
            email,
            telefono,
            activo,
            tipo_persona
        FROM estudiante
        WHERE activo = 1
        ORDER BY apellido_paterno, apellido_materno, nombre
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Estudiante alumno = new Estudiante();
                alumno.setId(rs.getLong("id"));
                alumno.setDni(rs.getString("dni"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setApellido_paterno(rs.getString("apellido_paterno"));
                alumno.setApellido_materno(rs.getString("apellido_materno"));
                alumno.setEmail(rs.getString("email"));
                alumno.setDireccion(rs.getString("direccion"));
                alumno.setEmail(rs.getString("email"));
                alumno.setTelefono(rs.getString("telefono"));
                alumno.setActivo(rs.getBoolean("activo"));
                alumno.setTipo_persona(rs.getString("tipo_persona"));
                lista.add(alumno);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener alumnos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public Boolean ActualizarAlumno(Estudiante alumno) {
        String sql = "UPDATE estudiante SET dni=?, nombre=?, apellido_paterno=?, apellido_materno=?, direccion=?, email=?, telefono=?, activo=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getDni());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getApellido_paterno());
            pstmt.setString(4, alumno.getApellido_materno());
            pstmt.setString(5, alumno.getDireccion());
            pstmt.setString(6, alumno.getEmail());
            pstmt.setString(7, alumno.getTelefono());
            pstmt.setBoolean(8, alumno.isActivo());
            pstmt.setString(9, alumno.getTipo_persona());
            pstmt.setLong(9, alumno.getId());

            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException ex) {
            System.err.println("Error al actualizar estudiante: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Boolean EliminarAlumno(Long id) {
        String sql = "DELETE FROM estudiante WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar Estudiante: " + ex.getMessage());
            return false;
        }
    }
}
