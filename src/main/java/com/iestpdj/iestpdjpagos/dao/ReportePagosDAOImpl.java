package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.PagoReporte;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportePagosDAOImpl implements ReportePagosDAO {

    private final Connection connection;

    public ReportePagosDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<PagoReporte> obtenerPagosPorDia(LocalDate fecha) {
        List<PagoReporte> lista = new ArrayList<>();
        String sql = """
                SELECT p.id_pago,
                                CONCAT(e.nombre, ' ', e.apellido_paterno) AS estudiante,
                                p.monto_total,
                                p.fecha_pago,
                                p.estado
                         FROM pagos p
                         INNER JOIN estudiante e ON e.id = p.id_estudiante
                         WHERE DATE(p.fecha_pago) = ?
                """;

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setDate(1, Date.valueOf(fecha));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<PagoReporte> obtenerPagosPorRango(LocalDate inicio, LocalDate fin) {
        List<PagoReporte> lista = new ArrayList<>();

        String sql = """
                SELECT p.id_pago,
                                CONCAT(e.nombre, ' ', e.apellido_paterno) AS estudiante,
                                p.monto_total,
                                p.fecha_pago,
                                p.estado
                         FROM pagos p
                         INNER JOIN estudiante e ON e.id = p.id_estudiante
                         WHERE DATE(p.fecha_pago) BETWEEN ? AND ?
                """;

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setDate(1, Date.valueOf(inicio));
            pstm.setDate(2, Date.valueOf(fin));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()){
                lista.add(mapRow(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return lista;
    }


    // ==========================
    //   BUSCAR POR RANGO
    // ==========================
    public List<PagoReporte> buscarPorRango(LocalDate inicio, LocalDate fin) {
        List<PagoReporte> lista = new ArrayList<>();

        String sql = """
                SELECT p.id, e.nombre AS estudiante, p.monto_total, p.fecha_pago, p.estado
                FROM pagos p
                INNER JOIN estudiante e ON p.estudiante_id = e.id
                WHERE DATE(p.fecha_pago) BETWEEN ? AND ?
                ORDER BY p.fecha_pago DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ==========================
    //   TOTAL INGRESOS DEL DÍA
    // ==========================
    public double totalIngresosPorDia(LocalDate fecha) {
        String sql = "SELECT SUM(monto) AS total FROM pagos WHERE DATE(fecha_pago) = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private PagoReporte mapRow(ResultSet rs) throws SQLException {

        // Fecha original
        LocalDateTime fechaPago = rs.getTimestamp("fecha_pago").toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaPago.format(formatter);

        // Monto original
        double monto = rs.getDouble("monto_total");
        String montoFormateado = "S/ " + String.format("%.2f", monto);

        return new PagoReporte(
                rs.getInt("id_pago"),
                rs.getString("estudiante"),
                rs.getDouble("monto_total"),   // ✔ Monto con "S/"
        rs.getTimestamp("fecha_pago").toLocalDateTime(),   // ✔ Fecha dd/MM/yyyy
                rs.getString("estado")
        );
    }
}
