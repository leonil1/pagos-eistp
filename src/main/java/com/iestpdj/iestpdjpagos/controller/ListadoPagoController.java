package com.iestpdj.iestpdjpagos.controller;

import com.iestpdj.iestpdjpagos.dao.PagoDAO;
import com.iestpdj.iestpdjpagos.dto.PagoDTO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListadoPagoController {

    @FXML
    private TableView<PagoDTO> tablaPagos;

    @FXML
    private TableColumn<PagoDTO, Integer> colIdPago;
    @FXML
    private TableColumn<PagoDTO, String> colNombre;
    @FXML
    private TableColumn<PagoDTO, String> colUsuario;
    @FXML
    private TableColumn<PagoDTO, String> colMetodo;
    @FXML
    private TableColumn<PagoDTO, Double> colMonto;
    @FXML
    private TableColumn<PagoDTO, LocalDate> colFecha;
    @FXML
    private TableColumn<PagoDTO, String> colEstado;
    @FXML
    private Pagination pagination;

    private final PagoDAO pagoDAO = new PagoDAO();
    private List<PagoDTO> lista;

    private static final int FILAS_POR_PAGINA = 20;
    private int paginaActual = 1;

    @FXML
    public void initialize() {
        configurarColumnas();
        inicializarPaginacion();
    }
    private void inicializarPaginacion() {
        pagination.setPageFactory(this::crearPagina);
        cargarTotalPaginas();
    }



    private void cargarTotalPaginas() {
        int totalRegistros = pagoDAO.contarPagos(); // 🔹 Nuevo método en el DAO
        int totalPaginas = (int) Math.ceil((double) totalRegistros / FILAS_POR_PAGINA);
        pagination.setPageCount(Math.max(totalPaginas, 1));
    }

    private void configurarColumnas() {
        colIdPago.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId_pago()).asObject());

        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreEstudiante()));

        colUsuario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsuario()));

        colMetodo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMetodoPago()));

        colMonto.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getMontoTotal()).asObject());

        colFecha.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFechaPago()));

        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));

        colMonto.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double monto, boolean empty) {
                super.updateItem(monto, empty);
                setText(empty || monto == null ? null : String.format("S/ %.2f", monto));
            }
        });


        colFecha.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override
            protected void updateItem(LocalDate fecha, boolean empty) {
                super.updateItem(fecha, empty);
                setText(empty || fecha == null ? null : fecha.format(formatter));
            }
        });
    }

    private void cargarPagos(int indicePagina) {
        lista = pagoDAO.listarTodoLosPagosPaginado(indicePagina);
        System.out.println("Cantidad de pagos obtenidos: " + (lista == null ? 0 : lista.size()));

        if (lista == null || lista.isEmpty()) {
            tablaPagos.setItems(FXCollections.observableArrayList());
            pagination.setPageCount(1);
            return;
        }

        int cantidadPaginas = (int) Math.ceil((double) lista.size() / FILAS_POR_PAGINA);
        pagination.setPageCount(cantidadPaginas);
        pagination.setPageFactory(this::crearPagina);
    }

    private Node crearPagina(int indicePagina) {
        paginaActual = indicePagina + 1;
        List<PagoDTO> lista = pagoDAO.listarTodoLosPagosPaginado(paginaActual);

        if (lista == null || lista.isEmpty()) {
            tablaPagos.setItems(FXCollections.observableArrayList());
            return tablaPagos;
        }

        ObservableList<PagoDTO> datos = FXCollections.observableArrayList(lista);
        tablaPagos.setItems(datos);
        return tablaPagos;
    }
}