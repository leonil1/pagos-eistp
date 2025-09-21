package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.model.Usuario;
import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label textUsuario;
    @FXML private Label textBienvenido;

    private Usuario usuario;

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        updateUI();
    }

    private void updateUI(){
        if (usuario != null){
            textBienvenido.setText("BIENVENIDO, " + usuario.getUsername() + "!");
            textUsuario.setText("Rol: " + usuario.getRol() + " / Email: " + usuario.getEmail());
        }
    }
}
