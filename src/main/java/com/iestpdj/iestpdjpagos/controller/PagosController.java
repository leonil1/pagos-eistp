package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.dao.EstudianteDAO;
import com.iestpdj.iestpdjpagos.dao.MetodoPagoDAO;
import com.iestpdj.iestpdjpagos.dao.PagoDAO;
import com.iestpdj.iestpdjpagos.model.*;
import com.iestpdj.iestpdjpagos.utils.BoletaPDFGenerator;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.*;

import static javafx.application.Application.launch;

public class PagosController extends Application {

    @FXML private TextField txtDNI;
    @FXML private TextField txtNombreEstudiante;
    @FXML private TextField txtEmailEstudiante;
    @FXML private TextField txtTelefonoEstudiante;
    @FXML private ComboBox<ConceptoPago> cboConceptos;
    @FXML private Spinner<Integer> spnCantidad;
    @FXML private TableView<DetallePago> tblDetalles;
    @FXML private ComboBox<MetodoPago> cboMetodoPago;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblTotal;
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnProcesar;

    private Estudiante estudianteSeleccionado;
    private ObservableList<DetallePago> detallesPago;
    private List<ConceptoPago> conceptoDiponible;
    private List<MetodoPago> metodoDisponble;

    private EstudianteDAO estudianteDAO;
    private ConceptoPagoDAO conceptoPagoDAO;
    private MetodoPagoDAO metodoPagoDAO;
    private PagoDAO pagoDAO;

    @FXML
    public void initialize() {
        inicializarDAOs();
        cargarDato();;
    }

    public void inicializarDAOs(){
        estudianteDAO = new EstudianteDAO();
        conceptoPagoDAO = new ConceptoPagoDAO();
        metodoPagoDAO = new MetodoPagoDAO();
        pagoDAO = new PagoDAO();
    }

    private void cargarDato(){
        try {
            conceptoDiponible = conceptoPagoDAO.ListarConceptoPago();
            cboConceptos.setItems(FXCollections.observableArrayList(conceptoDiponible));

            metodoDisponble = metodoPagoDAO.listarMetodoPagos();
            cboMetodoPago.setItems(FXCollections.observableArrayList(metodoDisponble));
        } catch (Exception ex){
            mostrarError("Error Al Cargar Datos", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void buscarEstudiante(){
        String dni = txtDNI.getText().trim();

        if (dni.isEmpty()) {
            mostrarAdvertencia("DNI requerido", "Por favor ingrese un DNI");
            txtDNI.requestFocus();
            return;
        }

        if (dni.length() != 8) {
            mostrarAdvertencia("DNI inválido", "El DNI debe tener 8 dígitos");
            txtDNI.selectAll();
            txtDNI.requestFocus();
            return;
        }

        if (!dni.matches("\\d+")) {
            mostrarAdvertencia("DNI inválido", "El DNI solo debe contener números");
            txtDNI.selectAll();
            txtDNI.requestFocus();
            return;
        }

        try {

          Estudiante estudiante = estudianteDAO.buscarAlumno(dni);

          if (estudiante != null){
              estudianteSeleccionado = estudiante;
              txtNombreEstudiante.setText(estudiante.getNombreCompleto());
              txtEmailEstudiante.setText(
                      estudiante.getEmail() != null ? estudiante.getEmail(): "No Registrado"
              );
              txtTelefonoEstudiante.setText(
                      estudiante.getTelefono() != null ? estudiante.getTelefono(): "No Registrado"
              );

              mostrarInformacion("✓ Estudiante encontrado",
                      "Datos del estudiante cargados correctamente");

              cboConceptos.requestFocus();
          }

        } catch (Exception e) {
            mostrarError("Error al buscar estudiante",
                    "Ocurrió un error al consultar la base de datos:\n" + e.getMessage());
            e.printStackTrace();
        }
    }



    private void configurarTabla(){
        tblDetalles.setItems(detallesPago);

        TableColumn<DetallePago, String> colConcepto =
                (TableColumn<DetallePago, String>) tblDetalles.getColumns().get(0);
        colConcepto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getConcepto().getId_concepto() + " - " +
                        cellData.getValue().getConcepto().getDescripcion()));

        TableColumn<DetallePago, Integer> colCantidad =
                (TableColumn<DetallePago, Integer>) tblDetalles.getColumns().get(1);
        colCantidad.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCantidad()));

        TableColumn<DetallePago, String> colPrecioUnitario =
                (TableColumn<DetallePago, String>) tblDetalles.getColumns().get(2);
        colPrecioUnitario.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f",
                        cellData.getValue().getPrecioUnitario())));

        TableColumn<DetallePago, String> colSubtotal =
                (TableColumn<DetallePago, String>) tblDetalles.getColumns().get(3);
        colSubtotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f",
                        cellData.getValue().getSubtotal())));


        TableColumn<DetallePago, Void> colAcciones =
                (TableColumn<DetallePago, Void>) tblDetalles.getColumns().get(4);
        colAcciones.setCellFactory(column -> new TableCell<DetallePago, Void>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.getStyleClass().add("btn-danger");
                btnEliminar.setOnAction(event -> {
                    DetallePago detalle = getTableView().getItems().get(getIndex());
                    eliminarDetalle(detalle);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                    setAlignment(Pos.CENTER);
                }
            }
        });

    }

    @FXML
    private void eliminarDetalle(  DetallePago detalle){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar este detalle?");
        alert.setContentText("Concepto:" + detalle.getConcepto().getDescripcion() + "\n" +
                "Cantidad: " + detalle.getCantidad() + "\n" +
                "Subtotal: S/ " + String.format("%.2f", detalle.getSubtotal()));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            detallesPago.remove(detalle);
            actualizarTotal();
            mostrarInformacionRapida("✓ Detalle eliminado",
                    "El detalle ha sido eliminado correctamente.");
        }
    }

    private void actualizarTotal() {
        double total = detallesPago.stream()
                .mapToDouble(DetallePago::getSubtotal)
                .sum();

        lblTotal.setText(String.format("S/ %.2f", total));
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(valueFactory);
        spnCantidad.setEditable(true);
    }

    private void configurarEventos() {
        // Actualizar total cuando cambia la cantidad en la tabla
        detallesPago.addListener((javafx.collections.ListChangeListener.Change<? extends DetallePago> c) -> {
            actualizarTotal();
        });

        // Enter en DNI busca automáticamente
        txtDNI.setOnAction(event -> buscarEstudiante());
    }

    @FXML
    private void agregarConcepto(){
        if (estudianteSeleccionado == null) {
            mostrarAdvertencia("⚠ Estudiante requerido",
                    "Primero debe buscar y seleccionar un estudiante");
            txtDNI.requestFocus();
            return;
        }

        // Validar concepto
        ConceptoPago concepto = cboConceptos.getValue();
        if (concepto == null) {
            mostrarAdvertencia("⚠ Concepto requerido",
                    "Por favor seleccione un concepto de pago de la lista");
            cboConceptos.requestFocus();
            return;
        }

        // Obtener cantidad
        Integer cantidad = spnCantidad.getValue();
        if (cantidad == null || cantidad <= 0) {
            mostrarAdvertencia("⚠ Cantidad inválida",
                    "La cantidad debe ser mayor a 0");
            spnCantidad.requestFocus();
            return;
        }

        // Verificar si ya existe el concepto
        boolean existe = detallesPago.stream()
                .anyMatch(d -> d.getConcepto().getId_concepto() == concepto.getId_concepto());

        if (existe){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Concepto duplicado");
            alert.setHeaderText("⚠ Este concepto ya ha sido agregado");
            alert.setContentText("¿Desea incrementar la cantidad del concepto existente?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){
                DetallePago detalleExistente = detallesPago.stream()
                        .filter(d -> d.getConcepto().getId_concepto() == concepto.getId_concepto())
                        .findFirst()
                        .orElse(null);

                if (detalleExistente != null) {
                    detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
                    tblDetalles.refresh();
                    actualizarTotal();
                }
            }
            return;
        }

        // con tabla
//       DetallePago nuevoDetalle = new DetallePago(
//               // completar el metodo
//       );

       // detallesPago.add(nuevoDetalle);

        // Limpiar selección
        cboConceptos.setValue(null);
        spnCantidad.getValueFactory().setValue(1);
        cboConceptos.requestFocus();

        // Mostrar mensaje
        mostrarInformacionRapida("✓ Concepto agregado",
                concepto.getNombre() + " agregado correctamente");
    }



    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacionRapida(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Cerrar automáticamente después de 2 segundos
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> alert.close());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        alert.show();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
