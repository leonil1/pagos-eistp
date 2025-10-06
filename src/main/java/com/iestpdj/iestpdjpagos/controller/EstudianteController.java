package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.model.Alumno;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstudianteController {

    @FXML
    private TableView<Alumno> tableAlumnos;

    @FXML
    void initialize() {

    }

    @FXML
    private void nuevoEstudiante() {
        abrirFormulario(null);
    }

    private void abrirFormulario(Alumno alumno) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistasFX/estudiante_form.fxml"));
            Parent root = fxmlLoader.load();
            EstudianteFormController controller = fxmlLoader.getController();

            Stage stage = new Stage();
            stage.initOwner(tableAlumnos.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle(alumno == null ? "nuevo estudiante": "Editar estudiante");
            stage.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al abrir estudiante");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
