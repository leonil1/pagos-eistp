package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.IUsuarioDAO;
import com.iestpdj.iestpdjpagos.dao.UsuarioDAO;
import com.iestpdj.iestpdjpagos.model.Usuario;

import java.util.List;

public class UsuarioService implements IUsuarioService {

    private IUsuarioDAO usuarioDAO;


    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }


    @Override
    public boolean iniciarSesion(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("El nombre del usuarioes requerido");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("La Contrase ña es requerido");
            return false;
        }

        return usuarioDAO.autenticarUsuario(username, password);
    }

    @Override
    public Usuario obtenerUsuario(String username) {

        if (username == null || username.trim().isEmpty()){
            return null;
        }
        return usuarioDAO.buscarPorUsername(username);
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
