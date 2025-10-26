package com.iestpdj.iestpdjpagos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class DashbordController1 {

    @FXML
    StackPane contentArea;

    @FXML
    private  void initialize() {
//        try {
//            Pane vistaInicio = FXMLLoader.load(getClass().getResource("/vistasFX/inicio.fxml"));
//            contentArea.getChildren().setAll(vistaInicio);
//        } catch (IOException e) {
//            mostrarError("Error al cargar la vista de inicio", e);
//        }
    }


    @FXML
    private void mostrarEstudiante(){
        cargarVista("/vistasFX/estudiante2.fxml");
    }

    @FXML
    private void mostrarPagos() {
        cargarVista("/vistasFX/form-pago.fxml");
    }

    @FXML
    private void mostrarConceptoPago(){
        cargarVista("/vistasFX/concepto_pago.fxml");
    }

    @FXML
    private void mostrarMetodoPago(){
        cargarVista("/vistasFX/metodo_pago.fxml");
    }

    @FXML
    private void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que deseas cerrar sesión?");
        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("Aceptar")) {
                System.exit(0);
            }
        });
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

    private void mostrarError(String mensaje, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
}
