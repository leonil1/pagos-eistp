package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.AlumnoDAO;
import com.iestpdj.iestpdjpagos.model.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class EstudianteController {


    @FXML
    private TableView<Alumno> tableAlumnos;
    @FXML
    private TableColumn<Alumno, Integer> colId;
    @FXML
    private TableColumn<Alumno, Integer> colDni;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, String> colApellidoPaterno;
    @FXML
    private TableColumn<Alumno, String> colApellidoMaterno;
    @FXML
    private TableColumn<Alumno, String> colDireccion;
    @FXML
    private TableColumn<Alumno, String> colEmail;
    @FXML
    private TableColumn<Alumno, String> colTelefono;
    @FXML
    private TableColumn<Alumno, Boolean> colActivo;

    private AlumnoDAO dao = new AlumnoDAO();

    private ObservableList<Alumno> lista = FXCollections.observableArrayList();

    private boolean maximizado = false;

    @FXML
    private TextField txtBuscar;

    @FXML
    private void buscarEstudiante() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            tableAlumnos.setItems(lista); // restablece
            mostrarAlerta(Alert.AlertType.INFORMATION, "Informacion", "Alumno no encontrado");
            return ;
        }

        ObservableList<Alumno> filtrados = lista.filtered(
                e -> e.getNombre().toLowerCase().contains(filtro)
                        || e.getDni().toLowerCase().contains(filtro)
        );
        tableAlumnos.setItems(filtrados);
    }

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellido_paterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellido_materno"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        tableAlumnos.setItems(lista);
        cargarEstudiante();
    }

    public void cargarEstudiante() {
        lista.clear();
        List<Alumno> alumnos = dao.obtenerTodosLosAlumnos();
        lista.addAll(alumnos);
    }

    @FXML
    private void nuevoEstudiante() {
        abrirFormulario(null);
    }

    @FXML
    private void editarEstudiante() {
        Alumno alumno = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumno == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccionar Estudiante", "Selecione un estudiante.");
            return;
        }
        abrirFormulario(alumno);
    }

    @FXML
    private void eliminarEstudiante() {
        Alumno alumno = tableAlumnos.getSelectionModel().getSelectedItem();
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

    private void abrirFormulario(Alumno alumno) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistasFX/estudiante_form.fxml"));
            Parent root = fxmlLoader.load();
            EstudianteFormController controller = fxmlLoader.getController();

            if (alumno != null) {
                controller.setEstudiante(alumno);
            } else {
                controller.setEstudiante(new Alumno());
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

    public void maximizarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(!maximizado);
        maximizado = !maximizado;
    }

    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
