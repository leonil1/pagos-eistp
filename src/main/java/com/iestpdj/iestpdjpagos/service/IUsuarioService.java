package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Usuario;

import java.util.List;

public interface IUsuarioService {
    boolean iniciarSesion(String username, String password);
    Usuario obtenerUsuario(String username);
    boolean registrarUsuario(String username, String password, String email, String rol);
    boolean validarCredenciales(String username, String password);
    List<Usuario> listarUsuarios();
    Usuario obtenerporDni(String dni);
}
