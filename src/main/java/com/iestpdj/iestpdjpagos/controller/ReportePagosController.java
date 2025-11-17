package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.model.PagoReporte;
import com.iestpdj.iestpdjpagos.service.ReportePagoService;
import com.iestpdj.iestpdjpagos.utils.ExcelGenerator;
import com.iestpdj.iestpdjpagos.utils.PdfGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class ReportePagosController {
    @FXML private TableView<PagoReporte> tablaPagos;
    @FXML private TableColumn<PagoReporte, Integer> colId;
    @FXML private TableColumn<PagoReporte, String> colEstudiante;
    @FXML private TableColumn<PagoReporte, Double> colMonto;
    @FXML private TableColumn<PagoReporte, String> colFecha;
    @FXML private TableColumn<PagoReporte, String> colEstado;

    @FXML private DatePicker fechaDia;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;

    private final ReportePagoService service = new ReportePagoService();
    private final ObservableList<PagoReporte> data = FXCollections.observableArrayList();
    ExcelGenerator excel = new ExcelGenerator();
    PdfGenerator pdf = new PdfGenerator();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(f -> new javafx.beans.property.SimpleIntegerProperty(f.getValue().getId()).asObject());
        colEstudiante.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getEstudiante()));
        colMonto.setCellValueFactory(f -> new javafx.beans.property.SimpleDoubleProperty(f.getValue().getMonto()).asObject());

        // FORMATO CORRECTO DE FECHA
        colFecha.setCellValueFactory(f ->
                new javafx.beans.property.SimpleStringProperty(
                        f.getValue().getFechaPago().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );

        colEstado.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getEstado()));

        tablaPagos.setItems(data);
    }

    // 🔍 BUSCAR POR DÍA
    @FXML
    public void buscarPorDia() {
        LocalDate fecha = fechaDia.getValue();
        if (fecha == null) {
            alerta("Seleccione una fecha.");
            return;
        }

        data.setAll(service.pagosPorDia(fecha));
    }

    // 🔍 BUSCAR POR RANGO
    @FXML
    public void buscarPorRango() {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();

        if (inicio == null || fin == null) {
            alerta("Seleccione ambas fechas.");
            return;
        }

        if (inicio.isAfter(fin)) {
            alerta("La fecha inicial no puede ser mayor que la final.");
            return;
        }

        data.setAll(service.pagosPorRango(inicio, fin));
    }

    // 📄 EXPORTAR PDF
    @FXML
    public void exportarPDF() {

        if (data.isEmpty()) {
            alerta("No hay datos para exportar.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File archivo = chooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                pdf.generarPDF(data, archivo.getAbsolutePath(), null);
                info("PDF generado correctamente.");
            } catch (Exception e) {
                alerta("Error al generar PDF: " + e.getMessage());
            }
        }
    }

    // 📊 EXPORTAR EXCEL
    @FXML
    public void exportarExcel() {
        if (data.isEmpty()) {
            alerta("No hay datos para exportar.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar Excel");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));

        File archivo = chooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                excel.generarExcel(data, archivo.getAbsolutePath());
                info("Excel generado correctamente.");
            } catch (Exception e) {
                alerta("Error al generar Excel: " + e.getMessage());
            }
        }
    }

    // 🔔 ALERTAS
    private void alerta(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
