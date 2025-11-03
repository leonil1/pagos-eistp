package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.MetodoPagoDAO;
import com.iestpdj.iestpdjpagos.model.MetodoPago;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MetodoPagoFormController {

    public TextField textNombre;
    public TextField textDescripcion;
    @FXML
    private RadioButton rbActivo;

    @FXML
    private RadioButton rbInactivo;

    private MetodoPago metodoPago;
    private MetodoPagoDAO dao = new MetodoPagoDAO();
    private boolean saved = false;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbActivo.setToggleGroup(group);
        rbInactivo.setToggleGroup(group);
    }

    public void setMetodoPago(MetodoPago metodoPago){
        this.metodoPago = metodoPago;
        if (metodoPago.getId_metodo() != null){
            textNombre.setText(metodoPago.getNombre());
            textDescripcion.setText(metodoPago.getDescripcion());
            rbActivo.setSelected(metodoPago.isActivo());
        }
    }

    @FXML
    private void guardar() {
        String nomnre = textNombre.getText().trim();
        String descripcion = textDescripcion.getText().trim();

        if (nomnre.isEmpty() || descripcion.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validacion", "Nombre no puede ser vacio");
            return;
        }

        metodoPago.setNombre(nomnre);
        metodoPago.setDescripcion(descripcion);
        metodoPago.setActivo(rbActivo.isSelected());

        try {
            if (metodoPago.getId_metodo() != null && metodoPago.getId_metodo() > 0){
                dao.ActualizarMetodoPago(metodoPago);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Se Guardo con exito");
            } else {
                dao.CreateMetodoPago(metodoPago);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Creado", "Se Guardo con exito");
            }

            saved = true;
            cerrarVentana();
        } catch (Exception ex){
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", ex.getMessage());
        }
    }

    @FXML
    private void cancelar(){
        cerrarVentana();
    }

    private void cerrarVentana(){
        Stage stage = (Stage) textNombre.getScene().getWindow();
        stage.close();
    }

    public boolean isSaved(){
        return saved;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
