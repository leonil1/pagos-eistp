package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Usuario;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean autenticarUsuario(String username, String password) {
        return false;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
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
}
