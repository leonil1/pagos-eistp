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
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    @FXML private TableView<DetallePagoView> tblDetalles;
    @FXML private ComboBox<MetodoPago> cboMetodoPago;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblTotal;
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnProcesar;

    private Estudiante estudianteSeleccionado;
    private ObservableList<DetallePagoView> detallesPago;
    private List<ConceptoPago> conceptoDiponible;
    private List<MetodoPago> metodoDisponble;

    private EstudianteDAO estudianteDAO;
    private ConceptoPagoDAO conceptoPagoDAO;
    private MetodoPagoDAO metodoPagoDAO;
    private PagoDAO pagoDAO;

    private int idUsuarioActual = 1;

    @FXML
    public void initialize() {
        inicializarDAOs();
        cargarDato();;
        configurarTabla();
        configurarSpinner();
        configurarEventos();
    }

    public void inicializarDAOs(){
        estudianteDAO = new EstudianteDAO();
        conceptoPagoDAO = new ConceptoPagoDAO();
        metodoPagoDAO = new MetodoPagoDAO();
        pagoDAO = new PagoDAO();
        detallesPago = FXCollections.observableArrayList();
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

        TableColumn<DetallePagoView, String> colConcepto =
                (TableColumn<DetallePagoView, String>) tblDetalles.getColumns().get(0);
        colConcepto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdConcepto() + " - " +
                        cellData.getValue().getConcepto()));

        TableColumn<DetallePagoView, Integer> colCantidad =
                (TableColumn<DetallePagoView, Integer>) tblDetalles.getColumns().get(1);
        colCantidad.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCantidad()));

        TableColumn<DetallePagoView, String> colPrecioUnitario =
                (TableColumn<DetallePagoView, String>) tblDetalles.getColumns().get(2);
        colPrecioUnitario.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f",
                        cellData.getValue().getPrecioUnitario())));

        TableColumn<DetallePagoView, String> colSubtotal =
                (TableColumn<DetallePagoView, String>) tblDetalles.getColumns().get(3);
        colSubtotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f",
                        cellData.getValue().getSubtotal())));


        TableColumn<DetallePagoView, Void> colAcciones =
                (TableColumn<DetallePagoView, Void>) tblDetalles.getColumns().get(4);
        colAcciones.setCellFactory(column -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.getStyleClass().add("btn-danger");
                btnEliminar.setOnAction(event -> {
                    DetallePagoView detalle = getTableView().getItems().get(getIndex());
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
    private void eliminarDetalle(DetallePagoView detalle){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar este detalle?");
        alert.setContentText("Concepto:" + detalle.getConcepto() + "\n" +
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



    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(valueFactory);
        spnCantidad.setEditable(true);
    }

    private void configurarEventos() {
        // Actualizar total cuando cambia la cantidad en la tabla
        detallesPago.addListener((javafx.collections.ListChangeListener.Change<? extends DetallePagoView> c) -> {
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
                .anyMatch(d -> d.getIdConcepto() == concepto.getId_concepto());

        if (existe){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Concepto duplicado");
            alert.setHeaderText("⚠ Este concepto ya ha sido agregado");
            alert.setContentText("¿Desea incrementar la cantidad del concepto existente?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){
                DetallePagoView detalleExistente = detallesPago.stream()
                        .filter(d -> d.getIdConcepto()== concepto.getId_concepto())
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


        DetallePagoView nuevoDetalle = new DetallePagoView(
               concepto.getId_concepto(),
               concepto.getDescripcion(),
                cantidad,
               concepto.getPrecio()
       );

        detallesPago.add(nuevoDetalle);

        // Limpiar selección
        cboConceptos.setValue(null);
        spnCantidad.getValueFactory().setValue(1);
        cboConceptos.requestFocus();

        // Mostrar mensaje
        mostrarInformacionRapida("✓ Concepto agregado",
                concepto.getNombre() + " agregado correctamente");
    }

    @FXML
    private void procesarPago(){

        if (estudianteSeleccionado == null) {
            mostrarAdvertencia("⚠ Estudiante requerido",
                    "Primero debe buscar y seleccionar un estudiante");
            txtDNI.requestFocus();
            return;
        }

        if (detallesPago.isEmpty()){
            mostrarAdvertencia("⚠ Detalles de pago vacíos",
                    "Debe agregar al menos un concepto de pago");
            cboConceptos.requestFocus();
            return;
        }

        MetodoPago metodo = cboMetodoPago.getValue();
        if (metodo == null){
            mostrarAdvertencia("⚠ Método de pago requerido",
                    "Por favor seleccione un método de pago de la lista");
            cboMetodoPago.requestFocus();
            return;
        }

        double total = detallesPago.stream()
                .mapToDouble(DetallePagoView::getSubtotal)
                .sum();

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar pago");
        confirmacion.setHeaderText("¿Desea procesar el pago por S/" + String.format("%.2f", total) + "?");

        StringBuilder contenido = new StringBuilder();
        contenido.append("ESTUDIANTE:\n");
        contenido.append(" . ").append(estudianteSeleccionado.getNombreCompleto()).append("\n");
        contenido.append(" . DNI ").append(estudianteSeleccionado.getDni()).append("\n\n");
        contenido.append("DETALLES DE PAGO:\n");
        for (DetallePagoView detalle: detallesPago){
            contenido.append(" . ").append(detalle.getIdConcepto())
                    .append(" (x").append(detalle.getCantidad()).append(") - S/")
                    .append(String.format("%.2f", detalle.getSubtotal())).append("\n");
        }
        contenido.append("\nMETODO DE PAGO: ").append(metodo.getNombre());
        contenido.append("\n\nTOTAL: s/ ").append(String.format("%.2f", total));

        confirmacion.setContentText(contenido.toString());

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            registrarPago(metodo, total);
        }
    }

    private void registrarPago(MetodoPago metodo, double total){
        try {
            Alert progress = new Alert(Alert.AlertType.INFORMATION);
            progress.setTitle("Procesando...");
            progress.setHeaderText("⏳ Procesando pago");
            progress.setContentText("Por favor espere...");
            progress.show();

            List<DetallePago> detalles = new ArrayList<>();
            for (DetallePagoView vista : detallesPago){
                ConceptoPago concepto = conceptoDiponible.stream()
                        .filter(c -> c.getId_concepto()==vista.getIdConcepto())
                        .findFirst()
                        .orElse(null);

                if (concepto != null){
                    detalles.add(new DetallePago(concepto, vista.getCantidad()));
                }
            }

            int idBoleta = pagoDAO.registrarPagoCompleto(
                    estudianteSeleccionado.getId(),
                    idUsuarioActual,
                    metodo.getId_metodo(),
                    total,
                    detalles
            );

            progress.close();

            generarBoletaPDF(idBoleta);

            Alert exito = new Alert(Alert.AlertType.INFORMATION);
            exito.setTitle("✓ Pago Exitoso");
            exito.setHeaderText("¡Pago procesado correctamente!");
            exito.setContentText("• Pago registrado en el sistema\n" +
                    "• Boleta generada en PDF\n" +
                    "• Total: S/ " + String.format("%.2f", total));
            exito.showAndWait();

            limpiarFormulario();
        } catch (Exception ex){
            mostrarError("❌ Error al procesar pago",
                    "Ocurrió un error al registrar el pago:\n\n" + ex.getMessage() +
                            "\n\nPor favor, verifique la conexión a la base de datos.");
            ex.printStackTrace();
        }
    }

    private void generarBoletaPDF(int idBoleta){
        try {
            DatosBoleta datos = pagoDAO.obtenerDatosBoleta(idBoleta);
            List<DetalleBoletaInfo> detalles = pagoDAO.obtenerDetallesBoleta(idBoleta);

            File pdf = BoletaPDFGenerator.generarBoletaPDF(datos, detalles);

            // Validaciones básicas del archivo generado
            if (pdf == null) {
                mostrarError("Error al generar PDF", "No se generó el archivo PDF (null).");
                return;
            }
            if (!pdf.exists() || pdf.length() == 0) {
                mostrarError("Error al generar PDF",
                        "El archivo PDF no existe o está vacío:\n" + pdf.getAbsolutePath());
                return;
            }

            // Verificar soporte del Desktop y la acción OPEN
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                    // Abrir en un hilo separado para no bloquear la UI
                    new Thread(() -> {
                        try {
                            desktop.open(pdf);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            javafx.application.Platform.runLater(() ->
                                    mostrarInformacion("PDF Generado",
                                            "No se pudo abrir automáticamente. Archivo en:\n" + pdf.getAbsolutePath())
                            );
                        }
                    }).start();
                    return;
                }
            }

            // Si no se puede abrir automáticamente, mostrar la ruta
            mostrarInformacion("PDF Generado",
                    "La boleta se ha generado correctamente en:\n" + pdf.getAbsolutePath());
        } catch (Exception ex){
            mostrarError("Error al generar PDF ",
                    "No se pudo generar la boleta en PDF:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void limpiarFormulario(){
        Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
        confirmacion.setTitle("Confirmar Limpieza");
        confirmacion.setHeaderText("¿Limpiar el Formulario?");
        confirmacion.setContentText("Se perderan todo los datos Ingresados");

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            txtDNI.clear();
            limpiarDatosEstudiante();
            detallesPago.clear();
            cboConceptos.setValue(null);
            cboMetodoPago.setValue(null);
            txtObservaciones.clear();
            spnCantidad.getValueFactory().setValue(1);
            actualizarTotal();
            txtDNI.requestFocus();
        }
    }

    private void limpiarDatosEstudiante() {
        estudianteSeleccionado = null;
        txtNombreEstudiante.clear();
        txtEmailEstudiante.clear();
        txtTelefonoEstudiante.clear();
    }

    private void actualizarTotal() {
        double total = detallesPago.stream()
                .mapToDouble(DetallePagoView::getSubtotal)
                .sum();

        lblTotal.setText(String.format("S/ %.2f", total));
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
