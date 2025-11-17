package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import javafx.concurrent.Task;
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
    private ConceptoPagoDAO dao = new ConceptoPagoDAO();

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

        // Normalizar separador decimal (\`1,23\` -> \`1.23\`)
        precioStr = precioStr.replace(',', '.');

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                mostrarAlerta("Precio inválido", "El precio no puede ser negativo.", Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Formato inválido", "El precio debe ser numérico.", Alert.AlertType.ERROR);
            return;
        }

        if (estado == null) {
            if (!cbEstado.getItems().isEmpty()) {
                estado = cbEstado.getItems().get(0);
            } else {
                estado = "Activo";
            }
        }

        final ConceptoPago concepto = new ConceptoPago(
                modoEdicion ? idConceptoEdicion : 0,
                nombre,
                descripcion,
                precio,
                estado
        );

        // Evitar múltiples envíos mientras se procesa
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                if (modoEdicion) {
                    return dao.ActualizarConceptoPago(concepto);
                } else {
                    return dao.CreateConceptoPago(concepto);
                }
            }
        };

        task.setOnSucceeded(evt -> {
            boolean resultado = task.getValue();
            btnGuardar.setDisable(false);
            btnCancelar.setDisable(false);
            if (resultado) {
                mostrarAlerta("Éxito", modoEdicion ? "Concepto actualizado correctamente." : "Concepto guardado correctamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el concepto.", Alert.AlertType.ERROR);
            }
        });

        task.setOnFailed(evt -> {
            btnGuardar.setDisable(false);
            btnCancelar.setDisable(false);
            Throwable ex = task.getException();
            mostrarAlerta("Error de persistencia", "Ocurrió un error al acceder a la base de datos: " + (ex != null ? ex.getMessage() : "desconocido"), Alert.AlertType.ERROR);
        });

        new Thread(task).start();
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
