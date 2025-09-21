package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.IUsuarioDAO;
import com.iestpdj.iestpdjpagos.model.Usuario;

import java.util.List;

public class UsuarioService implements IUsuarioService {

    private IUsuarioDAO usuarioDAO;

    @Override
    public boolean iniciarSesion(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        

        return false;
    }

    @Override
    public Usuario obtenerUsuario(String username) {
        return null;
    }

    @Override
    public boolean registrarUsuario(String username, String password, String email, String rol) {
        return false;
    }

    @Override
    public boolean validarCredenciales(String username, String password) {
        return false;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return List.of();
    }
}
