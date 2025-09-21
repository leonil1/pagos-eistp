package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    boolean autenticarUsuario(String username, String password);
    Usuario buscarPorUsername(String username);
    boolean crearUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    boolean eliminarUsuario(int id);
    List<Usuario> obtenerTodosLosUsuarios();

}
