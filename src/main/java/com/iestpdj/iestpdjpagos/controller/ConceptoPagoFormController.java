package com.iestpdj.iestpdjpagos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ConceptoPagoFormController {
    @FXML
    private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbEstado;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Label lblTitulo;

    private boolean modoEdicion = false;
    private int idConceptoEdicion;

    @FXML
    public void initialize() {
        cbEstado.getSelectionModel().selectFirst();

        btnGuardar.setOnAction(e -> guardarConcepto());
        btnCancelar.setOnAction(e -> cerrarVentana());
    }

    public void cargarDatosParaEdicion(int id, String nombre, String descripcion, double precio, String estado) {
        lblTitulo.setText("Editar Concepto de Pago");
        modoEdicion = true;
        idConceptoEdicion = id;
        txtNombre.setText(nombre);
        txtDescripcion.setText(descripcion);
        txtPrecio.setText(String.valueOf(precio));
        cbEstado.setValue(estado);
    }

    private void guardarConcepto() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String estado = cbEstado.getValue();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Debe ingresar al menos el nombre y precio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                mostrarAlerta("Precio inválido", "El precio no puede ser negativo.", Alert.AlertType.WARNING);
                return;
            }

            // TODO: Lógica para guardar o actualizar concepto
            // Si estás usando DAO:
            // ConceptoPagoDAO.guardar(new ConceptoPago(idConceptoEdicion, nombre, descripcion, precio, estado));

            mostrarAlerta("Éxito", "Concepto guardado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (NumberFormatException ex) {
            mostrarAlerta("Formato inválido", "El precio debe ser numérico.", Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
