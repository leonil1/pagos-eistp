package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    private void btnUsuario(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vistaFX/usuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Listado de Usuario");


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
