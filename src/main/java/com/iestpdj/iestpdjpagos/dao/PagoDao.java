package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.DetallePago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;
import com.iestpdj.iestpdjpagos.model.DatosBoleta;
import com.iestpdj.iestpdjpagos.model.DetalleBoletaInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDao {

    private Connection connection;

    public PagoDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public int registrarPagoCompleto(int idEstudiante, int idUsuario, int idMetodo,
                                     double montoTotal, List<DetallePago> detalles)
            throws SQLException {

        Connection conn = null;
        int idPago = 0;
        int idBoleta = 0;

        try {

            // 1. Insertar pago
            String sqlPago = "INSERT INTO pagos (id_estudiante, id_usuario, id_metodo, " +
                    "monto_total, estado) VALUES (?, ?, ?, ?, 'PAGADO')";
            PreparedStatement stmtPago = connection.prepareStatement(sqlPago,
                    Statement.RETURN_GENERATED_KEYS);
            stmtPago.setInt(1, idEstudiante);
            stmtPago.setInt(2, idUsuario);
            stmtPago.setInt(3, idMetodo);
            stmtPago.setDouble(4, montoTotal);
            stmtPago.executeUpdate();

            ResultSet rs = stmtPago.getGeneratedKeys();
            if (rs.next()) {
                idPago = rs.getInt(1);
            }

            // 2. Generar número de boleta
            String numeroBoleta = generarNumeroBoleta(conn);

            // 3. Insertar boleta
            String sqlBoleta = "INSERT INTO boleta (id_pago, numero_boleta, total, estado) " +
                    "VALUES (?, ?, ?, 'VIGENTE')";
            PreparedStatement stmtBoleta = connection.prepareStatement(sqlBoleta,
                    Statement.RETURN_GENERATED_KEYS);
            stmtBoleta.setInt(1, idPago);
            stmtBoleta.setString(2, numeroBoleta);
            stmtBoleta.setDouble(3, montoTotal);
            stmtBoleta.executeUpdate();

            rs = stmtBoleta.getGeneratedKeys();
            if (rs.next()) {
                idBoleta = rs.getInt(1);
            }

            // 4. Insertar detalles de boleta
            String sqlDetalle = "INSERT INTO detalle_boleta (id_boleta, id_concepto, " +
                    "cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtDetalle = connection.prepareStatement(sqlDetalle);

            for (DetallePago detalle : detalles) {
                stmtDetalle.setInt(1, idBoleta);
                stmtDetalle.setInt(2, detalle.getConcepto().getId_concepto());
                stmtDetalle.setInt(3, detalle.getCantidad());
                stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                stmtDetalle.setDouble(5, detalle.getSubtotal());
                stmtDetalle.addBatch();
            }
            stmtDetalle.executeBatch();

            conn.commit();
            return idBoleta;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private String generarNumeroBoleta(Connection conn) throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(numero_boleta, 10) AS UNSIGNED)) as ultimo " +
                "FROM boleta WHERE numero_boleta LIKE 'BOL-" +
                java.time.Year.now().getValue() + "-%'";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int siguiente = 1;
            if (rs.next() && rs.getObject("ultimo") != null) {
                siguiente = rs.getInt("ultimo") + 1;
            }

            return String.format("BOL-%d-%04d", java.time.Year.now().getValue(), siguiente);
        }
    }

    public DatosBoleta obtenerDatosBoleta(int idBoleta) throws SQLException {
        String sql = "SELECT b.numero_boleta, b.fecha_emision, b.total, " +
                "e.nombre, e.apellido_paterno, e.apellido_materno, e.dni, " +
                "u.username as cajero, m.nombre as metodo_pago " +
                "FROM boleta b " +
                "JOIN pagos p ON b.id_pago = p.id_pago " +
                "JOIN estudiante e ON p.id_estudiante = e.id " +
                "JOIN usuarios u ON p.id_usuario = u.id " +
                "JOIN metodo_pago m ON p.id_metodo = m.id_metodo " +
                "WHERE b.id_boleta = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idBoleta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new DatosBoleta(
                        rs.getString("numero_boleta"),
                        rs.getTimestamp("fecha_emision"),
                        rs.getDouble("total"),
                        rs.getString("nombre") + " " + rs.getString("apellido_paterno") +
                                " " + rs.getString("apellido_materno"),
                        rs.getString("dni"),
                        rs.getString("cajero"),
                        rs.getString("metodo_pago")
                );
            }
        }
        return null;
    }

    public List<DetalleBoletaInfo> obtenerDetallesBoleta(int idBoleta) throws SQLException {
        List<DetalleBoletaInfo> detalles = new ArrayList<>();
        String sql = "SELECT c.nombre, db.cantidad, db.precio_unitario, db.subtotal " +
                "FROM detalle_boleta db " +
                "JOIN concepto_pago c ON db.id_concepto = c.id_concepto " +
                "WHERE db.id_boleta = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idBoleta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                detalles.add(new DetalleBoletaInfo(
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                ));
            }
        }
        return detalles;
    }

}
