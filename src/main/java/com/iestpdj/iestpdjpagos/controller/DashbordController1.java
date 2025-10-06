package com.iestpdj.iestpdjpagos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashbordController1 {

    @FXML
    StackPane contentArea;

    @FXML
    private  void initialize() {

    }

    @FXML
    private void mostrarEstudiante(){
        cargarVista("/vistasFX/estudiante2.fxml");
    }


    private void cargarVista(String rutaFXML){
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(rutaFXML)));
            contentArea.getChildren().setAll(node);
            contentArea.getChildren().clear();
            contentArea.getChildren().addAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
