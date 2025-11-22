package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.EstudianteDAO;
import com.iestpdj.iestpdjpagos.model.Estudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EstudianteController implements Initializable {


    @FXML
    private TableView<Estudiante> tableAlumnos;
    @FXML
    private TableColumn<Estudiante, Integer> colId;
    @FXML
    private TableColumn<Estudiante, String> colTipoPersona;
    @FXML
    private TableColumn<Estudiante, Integer> colDni;
    @FXML
    private TableColumn<Estudiante, String> colNombreCompleto;
    @FXML
    private TableColumn<Estudiante, String> colNombre;
    @FXML
    private TableColumn<Estudiante, String> colApellidoPaterno;
    @FXML
    private TableColumn<Estudiante, String> colApellidoMaterno;
    @FXML
    private TableColumn<Estudiante, String> colDireccion;
    @FXML
    private TableColumn<Estudiante, String> colEmail;
    @FXML
    private TableColumn<Estudiante, String> colTelefono;
    @FXML
    private TableColumn<Estudiante, Boolean> colActivo;


    private EstudianteDAO dao = new EstudianteDAO();

    private ObservableList<Estudiante> lista = FXCollections.observableArrayList();


    @FXML
    private TextField txtBuscar;

    @FXML
    private void buscarEstudiante() {
        String filtro = txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            tableAlumnos.setItems(lista);
            return;
        }

        ObservableList<Estudiante> filtrados = lista.filtered(e ->
                containsIgnoreCase(e.getNombre(), filtro) ||
                        containsIgnoreCase(e.getDni(), filtro) ||
                        containsIgnoreCase(e.getApellido_paterno(), filtro) ||
                        containsIgnoreCase(e.getApellido_materno(), filtro) ||
                        containsIgnoreCase(e.getTipo_persona(), filtro)
        );

        tableAlumnos.setItems(filtrados);
    }

    private boolean containsIgnoreCase(String field, String filtro) {
        if (field == null || filtro == null) return false;
        return field.toLowerCase().contains(filtro.toLowerCase());
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellido_paterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellido_materno"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colTipoPersona.setCellValueFactory(new PropertyValueFactory<>("tipo_persona"));

        colActivo.setCellFactory(column -> new TableCell<Estudiante, Boolean>() {
            @Override
            protected void updateItem(Boolean activo, boolean empty) {
                super.updateItem(activo, empty);

                if (empty || activo == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                if (activo) {
                    setText("ACTIVO");
                    setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-alignment: CENTER;");
                } else {
                    setText("INACTIVO");
                    setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-alignment: CENTER;");
                }
            }
        });

        tableAlumnos.setItems(lista);
        cargarEstudiante();
    }

    public void cargarEstudiante() {
        lista.clear();
        List<Estudiante> alumnos = dao.obtenerTodosLosAlumnos();
        lista.addAll(alumnos);
    }

    @FXML
    private void nuevoEstudiante() {
        abrirFormulario(null);
    }

    @FXML
    private void editarEstudiante() {
        Estudiante alumno = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumno == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccionar Estudiante", "Selecione un estudiante.");
            return;
        }
        abrirFormulario(alumno);
    }

    @FXML
    private void eliminarEstudiante() {
        Estudiante alumno = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumno == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccionar Estudiante", "Seleccione un estudiante.");
            return;
        }

        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Confirmar Eliminación");
        confirmar.setHeaderText("¿Eliminar Estudiante?");
        confirmar.setContentText(
                "Estuiante: " + alumno.getNombre() + " " + alumno.getApellido_paterno() + " " + alumno.getApellido_materno());

        Optional<ButtonType> result = confirmar.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean ok = dao.EliminarAlumno(alumno.getId());
                if (ok){
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Estudiante eliminado exitosamente.");
                    cargarEstudiante();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar estudiante.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrio un Error: " + ex.getMessage());

            }
        }
    }

    private void abrirFormulario(Estudiante alumno) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistasFX/estudiante_form.fxml"));
            Parent root = fxmlLoader.load();
            EstudianteFormController controller = fxmlLoader.getController();

            if (alumno != null) {
                controller.setEstudiante(alumno);
            } else {
                controller.setEstudiante(new Estudiante());
            }

            Stage stage = new Stage();
            stage.initOwner(tableAlumnos.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle(alumno == null ? "nuevo estudiante" : "Editar estudiante");
            stage.showAndWait();

            if (controller.isSaved()){
                cargarEstudiante();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al abrir estudiante");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
