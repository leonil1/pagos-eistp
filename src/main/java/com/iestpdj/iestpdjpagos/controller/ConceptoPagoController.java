package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class ConceptoPagoController {

    @FXML private TableView<ConceptoPago> tablaConcepto;
    @FXML private TableColumn<ConceptoPago, Integer> colId;
    @FXML private TableColumn<ConceptoPago, String> colNombre;
    @FXML private TableColumn<ConceptoPago, String> colDescripcion;
    @FXML private TableColumn<ConceptoPago, Double> colPrecio;
    @FXML private TableColumn<String, Boolean> colEstado;
    @FXML private TextField txtBuscar;

    private ConceptoPagoDAO dao = new ConceptoPagoDAO();

    private ObservableList<ConceptoPago> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<ConceptoPago, Integer>("id_concepto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setCellFactory(column -> new TableCell<ConceptoPago, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);

                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText("S/ " + String.format("%.2f", precio));
                }
            }
        });
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        tablaConcepto.setItems(lista);
        cargarListaPagos();
    }

    public void cargarListaPagos(){
        lista.clear();
        List<ConceptoPago> conceptoPagos = dao.ListarConceptoPago();
        lista.addAll(conceptoPagos);
    }

    @FXML
    public void buscarConcepto(){
        String filtro = txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()){
            tablaConcepto.setItems(lista);
            mostrarAlerta(Alert.AlertType.INFORMATION,"Busqueda", "No se encontraron coincidencias.");
            return;
        }
        ObservableList<ConceptoPago> filtados = lista.filtered(
                c -> c.getDescripcion().toLowerCase().contains(filtro)
                        || c.getNombre().toLowerCase().contains(filtro)
        );tablaConcepto.setItems(filtados);
    }

    @FXML
    private void nuevoConceptoPago() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistasFX/concepto_pago_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nuevo Concepto de Pago");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarConceptoPago() {
        ConceptoPago seleccionado = tablaConcepto.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccionar", "Debe seleccionar un concepto para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistasFX/concepto_pago_form.fxml"));
            Parent root = loader.load();

            ConceptoPagoFormController formController = loader.getController();
            formController.cargarDatosParaEdicion(
                    seleccionado.getId_concepto(),
                    seleccionado.getNombre(),
                    seleccionado.getDescripcion(),
                    seleccionado.getPrecio(),
                    seleccionado.getEstado()
            );

            Stage stage = new Stage();
            stage.setTitle("Editar Concepto de Pago");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarConcepto(){
        ConceptoPago seleccionado = tablaConcepto.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta( Alert.AlertType.WARNING,"Selecciona concepto pago", "Selecciona un comcepto de pago");
            return;
        }
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar Concepto de Pago");
        confirmar.setHeaderText("¿Eliminar Concepto pago?");
        confirmar.setContentText(
                "Concepto Pago: " + seleccionado.getNombre() + " " );
        Optional<ButtonType> result = confirmar.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean ok = dao.EliminarConceptoPago(seleccionado.getId_concepto());
                if (ok) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar","Concepto pago Eliminado exitoso!");
                    cargarListaPagos();
                }else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Eliminar", "Error al eliminar el Concepto de pago");
                }
            } catch (Exception ex){
                ex.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error" ,"Ocurrio un erro: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void refrescar(){
        javafx.application.Platform.runLater(() -> {
            cargarListaPagos();
            tablaConcepto.getSelectionModel().clearSelection();
            tablaConcepto.refresh();
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
