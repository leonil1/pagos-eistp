package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.EstudianteDAO;
import com.iestpdj.iestpdjpagos.model.Estudiante;
import com.iestpdj.iestpdjpagos.model.TipoPersona;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EstudianteFormController {
    @FXML
    private ComboBox<TipoPersona> comboTipoPersona;
    public TextField textDni;
    public TextField textNombre;
    public TextField textApellidoPaterno;
    public TextField textApellidoMaterno;
    public TextField textDireccion;
    public TextField textEmail;
    public TextField textTelefono;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private ToggleGroup groupActivo;
    @FXML
    private RadioButton rbInactivo;

    private Estudiante alumno;
    private EstudianteDAO dao = new EstudianteDAO();
    private boolean saved = false;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbActivo.setToggleGroup(group);
        rbInactivo.setToggleGroup(group);
        comboTipoPersona.getItems().addAll(TipoPersona.values());
        comboTipoPersona.getSelectionModel().selectFirst();
    }

    public void  setEstudiante(Estudiante alumno){
        this.alumno = alumno;

        if (alumno.getId() != null) {
            textDni.setText(alumno.getDni());
            textNombre.setText(alumno.getNombre());
            textApellidoPaterno.setText(alumno.getApellido_paterno());
            textApellidoMaterno.setText(alumno.getApellido_materno());
            textDireccion.setText(alumno.getDireccion());
            textEmail.setText(alumno.getEmail());
            textTelefono.setText(alumno.getTelefono());
            rbActivo.setSelected(alumno.isActivo());
            rbInactivo.setSelected(!alumno.isActivo());

            // ✔ seleccionar el tipo persona correctamente
            comboTipoPersona.getSelectionModel().select(
                    TipoPersona.valueOf(alumno.getTipo_persona().trim().toUpperCase())
            );
        }
    }

    @FXML
    private void guardar() {
        String dni = textDni.getText().trim();
        String nombre = textNombre.getText().trim();
        String apellidoParterno = textApellidoPaterno.getText().trim();
        String apellidoMaterno = textApellidoMaterno.getText().trim();
        String direccion = textDireccion.getText().trim();
        String email = textEmail.getText().trim();
        String telefono = textTelefono.getText().trim();

        if (dni.isEmpty() || nombre.isEmpty() || apellidoParterno.isEmpty() || apellidoMaterno.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validacion", "DNI, Nombre, Apellidos no pueden ser vacíos");
            return;
        }

        if (dni.length() != 8){
            mostrarAlerta(Alert.AlertType.WARNING, "Validacion", "El DNI debe tener 8 dígitos");
            return;
        }

        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido_paterno(apellidoParterno);
        alumno.setApellido_materno(apellidoMaterno);
        alumno.setDireccion(direccion);
        alumno.setEmail(email);
        alumno.setTelefono(telefono);
        alumno.setActivo(rbActivo.isSelected());

        // ✔ GUARDAR TIPO PERSONA
        if (comboTipoPersona.getValue() != null) {
            alumno.setTipo_persona(comboTipoPersona.getValue().name());
        }

        try {
            if (alumno.getId() != null && alumno.getId() > 0) {
                dao.ActualizarAlumno(alumno);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Estudiante actualizado");
            } else {
                dao.CreateAlumno(alumno);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado", "Estudiante creado");
            }
            saved = true;
            cerrarVentana();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Fallo al guardar estudiante", ex.getMessage());
        }
    }

    @FXML
    private void cancelar(){
        cerrarVentana();
    }

    private void cerrarVentana(){
        Stage stage = (Stage) textDni.getScene().getWindow();
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
