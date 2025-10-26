package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Pago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO implements IPagoDAO{

    private Connection connection;

    public PagoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Void guardarPago(Pago pago) {

        String sql = "INSERT INTO pagos (id_estudiante, id_usuario, id_metodo, monto_total, estado) VALUES (?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, pago.getIdEstudiante());
            pstmt.setInt(2, pago.getIdUsuario());
            pstmt.setInt(3, pago.getIdMetodo());
            pstmt.setBigDecimal(4, pago.getMontoTotal());
            pstmt.setString(5, pago.getEstado());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) pago.setIdPago(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Pago> listarPagos() {

        List<Pago> listaPagos = new ArrayList<>();
        String sql = "SELECT * FROM pagos ORDER BY fecha_pago DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pago pago = new Pago();
                pago.setIdPago(rs.getInt("id_pago"));
                pago.setIdEstudiante(rs.getInt("id_estudiante"));
                pago.setIdUsuario(rs.getInt("id_usuario"));
                pago.setIdMetodo(rs.getInt("id_metodo"));
                pago.setMontoTotal(rs.getBigDecimal("monto_total"));
                pago.setFechaPago(rs.getTimestamp("fecha_pago"));
                pago.setEstado(rs.getString("estado"));
                listaPagos.add(pago);
            }
        } catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }

        return listaPagos;
    }


}
