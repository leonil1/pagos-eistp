package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.dao.IPagoDAO;
import com.iestpdj.iestpdjpagos.dto.PagoDTO;
import com.iestpdj.iestpdjpagos.model.DatosBoleta;
import com.iestpdj.iestpdjpagos.model.DetalleBoletaInfo;
import com.iestpdj.iestpdjpagos.model.DetallePago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO implements IPagoDAO {

    private static final int LIMITE_PAGINA = 20; // 🔹 Tamaño de página fijo

    /**
     * Lista todos los pagos paginados (20 en 20)
     *
     * @param pagina número de página (empieza en 1)
     * @return lista de pagos
     */
    public List<PagoDTO> listarTodoLosPagosPaginado(int pagina) {
        String sql = """
        SELECT 
            p.id_pago,
            UPPER(CONCAT(e.nombre, ' ', e.apellido_paterno, ' ', e.apellido_materno)) AS nombre_completo,
            u.username,
            mp.nombre AS metodo_pago,
            p.monto_total,
            p.fecha_pago,
            p.estado
        FROM pagos p
        INNER JOIN usuarios u ON u.id = p.id_usuario
        INNER JOIN estudiante e ON e.id = p.id_estudiante
        INNER JOIN metodo_pago mp ON mp.id_metodo = p.id_metodo
        ORDER BY p.fecha_pago DESC
        LIMIT ? OFFSET ?
        """;

        List<PagoDTO> lista = new ArrayList<>();
        int offset = (pagina - 1) * LIMITE_PAGINA;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, LIMITE_PAGINA);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PagoDTO dto = new PagoDTO();
                    dto.setId_pago(rs.getInt("id_pago"));
                    dto.setNombreEstudiante(rs.getString("nombre_completo"));
                    dto.setUsuario(rs.getString("username"));
                    dto.setMetodoPago(rs.getString("metodo_pago"));
                    dto.setMontoTotal(rs.getDouble("monto_total"));
                    dto.setFechaPago(rs.getDate("fecha_pago").toLocalDate());
                    dto.setEstado(rs.getString("estado"));
                    lista.add(dto);
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al listar pagos paginados: " + ex.getMessage());
        }

        return lista;
    }

    // ===============================================================
    // 🔹 REGISTRAR PAGO COMPLETO
    // ===============================================================

    public int registrarPagoCompleto(Long idEstudiante, int idUsuario, Long idMetodo,
                                     double montoTotal, List<DetallePago> detalles)
            throws SQLException {

        int idPago = 0;
        int idBoleta = 0;

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            // 1. Insertar pago
            String sqlPago = "INSERT INTO pagos (id_estudiante, id_usuario, id_metodo, monto_total, estado) " +
                    "VALUES (?, ?, ?, ?, 'PAGADO')";
            try (PreparedStatement stmtPago = connection.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {
                stmtPago.setLong(1, idEstudiante);
                stmtPago.setInt(2, idUsuario);
                stmtPago.setLong(3, idMetodo);
                stmtPago.setDouble(4, montoTotal);
                stmtPago.executeUpdate();

                try (ResultSet rs = stmtPago.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPago = rs.getInt(1);
                    }
                }
            }

            // 2. Generar número de boleta
            String numeroBoleta = generarNumeroBoleta(connection);

            // 3. Insertar boleta
            String sqlBoleta = "INSERT INTO boleta (id_pago, numero_boleta, total, estado) VALUES (?, ?, ?, 'VIGENTE')";
            try (PreparedStatement stmtBoleta = connection.prepareStatement(sqlBoleta, Statement.RETURN_GENERATED_KEYS)) {
                stmtBoleta.setInt(1, idPago);
                stmtBoleta.setString(2, numeroBoleta);
                stmtBoleta.setDouble(3, montoTotal);
                stmtBoleta.executeUpdate();

                try (ResultSet rs = stmtBoleta.getGeneratedKeys()) {
                    if (rs.next()) {
                        idBoleta = rs.getInt(1);
                    }
                }
            }

            // 4. Insertar detalles
            String sqlDetalle = "INSERT INTO detalle_boleta (id_boleta, id_concepto, cantidad, precio_unitario, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtDetalle = connection.prepareStatement(sqlDetalle)) {
                for (DetallePago detalle : detalles) {
                    stmtDetalle.setInt(1, idBoleta);
                    stmtDetalle.setInt(2, detalle.getConcepto().getId_concepto());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                    stmtDetalle.setDouble(5, detalle.getSubtotal());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }

            connection.commit();
            return idBoleta;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ===============================================================
    // 🔹 GENERAR NÚMERO DE BOLETA
    // ===============================================================

    private String generarNumeroBoleta(Connection conn) throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(numero_boleta, 10) AS UNSIGNED)) as ultimo " +
                "FROM boleta WHERE numero_boleta LIKE 'BOL-" + java.time.Year.now().getValue() + "-%'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int siguiente = 1;
            if (rs.next() && rs.getObject("ultimo") != null) {
                siguiente = rs.getInt("ultimo") + 1;
            }
            return String.format("BOL-%d-%04d", java.time.Year.now().getValue(), siguiente);
        }
    }

    // ===============================================================
    // 🔹 OBTENER DATOS DE BOLETA
    // ===============================================================

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

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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

    // ===============================================================
    // 🔹 OBTENER DETALLES DE BOLETA
    // ===============================================================

    public List<DetalleBoletaInfo> obtenerDetallesBoleta(int idBoleta) throws SQLException {
        List<DetalleBoletaInfo> detalles = new ArrayList<>();
        String sql = "SELECT c.nombre, db.cantidad, db.precio_unitario, db.subtotal " +
                "FROM detalle_boleta db " +
                "JOIN concepto_pago c ON db.id_concepto = c.id_concepto " +
                "WHERE db.id_boleta = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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

    public int contarPagos() {
        String sql = "SELECT COUNT(*) AS total FROM pagos";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al contar pagos: " + ex.getMessage());
        }
        return 0;
    }
}
