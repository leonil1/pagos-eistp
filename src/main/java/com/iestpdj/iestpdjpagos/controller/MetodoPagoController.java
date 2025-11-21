package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.MetodoPagoDAO;
import com.iestpdj.iestpdjpagos.model.MetodoPago;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class MetodoPagoController {
    @FXML private TableView<MetodoPago> tablaMetodoPago;
    @FXML private TableColumn<MetodoPago, Integer> colId;
    @FXML private TableColumn<MetodoPago, String> colNombre;
    @FXML private TableColumn<MetodoPago, String> colDescripcion;
    @FXML private TableColumn<MetodoPago, Boolean> colEstado;

    @FXML private TextField txtBuscar;

    private MetodoPagoDAO dao = new MetodoPagoDAO();

    private ObservableList<MetodoPago> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_metodo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean activo, boolean empty) {
                super.updateItem(activo, empty);

                if (empty || activo == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                String texto = activo ? "ACTIVO" : "INACTIVO";
                setText(texto);

                if (activo) {
                    setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;"); // Verde
                } else {
                    setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;"); // Rojo
                }
            }
        });

        tablaMetodoPago.setItems(lista);
        cargarMetodoPago();
    }

    @FXML
    public void buscarMetodoPago(){
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()){
            tablaMetodoPago.setItems(lista);
            return;
        }

        ObservableList<MetodoPago> filtados = lista.filtered(
                e -> e.getNombre().toLowerCase().contains(filtro)
        );
        tablaMetodoPago.setItems(filtados);
    }

    public void cargarMetodoPago(){
        lista.clear();
        List<MetodoPago> metodoPagos = dao.listarMetodoPagos();
        lista.addAll(metodoPagos);
    }

    @FXML
    private void nuevoMetodoPago(){
        abrirFormulario(null);
    }

    @FXML
    private void editarMetodoPago(){
        MetodoPago metodoPago = tablaMetodoPago.getSelectionModel().getSelectedItem();
        if (metodoPago == null){
            mostrarAlerta(Alert.AlertType.WARNING, "Selecionar Metodo Pago", "Seleccione un metodo pago");
            return;
        }
        abrirFormulario(metodoPago);
    }

    private void abrirFormulario(MetodoPago metodoPago){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistasFX/metodo_form_pago.fxml"));
            Parent root = fxmlLoader.load();
            MetodoPagoFormController controler = fxmlLoader.getController();

            if (metodoPago != null){
                controler.setMetodoPago(metodoPago);
            }else {
                controler.setMetodoPago(new MetodoPago());
            }
            Stage stage = new Stage();
            stage.initOwner(txtBuscar.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle(metodoPago == null ? "Nuenvo Metodo Pago": "Editar Metodo Pago");
            stage.showAndWait();
            if (controler.isSaved()){
                cargarMetodoPago();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error Al Abrir el Metodo pago");
        }
    }

    @FXML
    private void eliminarMetodoPago(){
        MetodoPago metodo = tablaMetodoPago.getSelectionModel().getSelectedItem();
        if (metodo == null){
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccionar Metodo PAgo","Seleccionar un Meto de Pago");
            return;
        }
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Confirmar Eliminación");
        confirmar.setHeaderText("¿Eliminar Meto Pago?");
        confirmar.setContentText("Estudiante: " + metodo.getNombre());
        Optional<ButtonType> result = confirmar.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            try {
                boolean ok = dao.EliminarMetodoPago(metodo.getId_metodo());
                if (ok){
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Estudiante eliminado exitosamente.");
                    cargarMetodoPago();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar estudiante.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrio un Error: " + ex.getMessage());

            }
        }
    }

    @FXML
    private void refrescarMetodoPago(){

    }


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
