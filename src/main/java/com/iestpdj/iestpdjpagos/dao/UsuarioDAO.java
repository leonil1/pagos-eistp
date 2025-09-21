package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Usuario;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean autenticarUsuario(String username, String password) {

        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = true";
        try {
            PreparedStatement statemen = connection.prepareStatement(sql);
            statemen.setString(1, username);
            statemen.setString(2, hashPassword(password));
            ResultSet rs = statemen.executeQuery();
            return rs.next();
        }catch (Exception e) {
            System.out.println("Error al Authenticar el usuario " + e.getMessage());
            return false;
        }

    }

    @Override
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getBoolean("activo"));
                return usuario;
            }
        }catch (Exception e) {
            System.out.println("Error al buscar el usuario " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean crearUsuario(Usuario usuario) {
        return false;
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        return false;
    }

    @Override
    public boolean eliminarUsuario(int id) {
        return false;
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return List.of();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al hashear password: " + e.getMessage());
            return password; // En caso de error, devolver password sin hashear (NO recomendado en producción)
        }
    }
}
