package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class ConceptoPagoController {

    @FXML private TableView<ConceptoPago> tablaConceto;
    @FXML private TableColumn<ConceptoPago, Integer> colId;
    @FXML private TableColumn<ConceptoPago, String> colNombre;
    @FXML private TableColumn<ConceptoPago, String> colDescripcion;
    @FXML private TableColumn<ConceptoPago, String> colPrecio;
    @FXML private TableColumn<ConceptoPago, Boolean> colEstado;

    private ConceptoPagoDAO dao = new ConceptoPagoDAO();

    private ObservableList<ConceptoPago> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<ConceptoPago, Integer>("id_concepto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaConceto.setItems(lista);
        cargarListaPagos();
    }

    public void cargarListaPagos(){
        lista.clear();
        List<ConceptoPago> conceptoPagos = dao.ListarConceptoPago();
        lista.addAll(conceptoPagos);
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
        ConceptoPago seleccionado = tablaConceto.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccionar", "Debe seleccionar un concepto para editar.", Alert.AlertType.WARNING);
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
