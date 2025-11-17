package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Estudiante;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConceptoPagoDAO implements IConceptoPagoDAO {

    private static final Logger log = LoggerFactory.getLogger(IConceptoPagoDAO.class);

    private Connection connection;

    public ConceptoPagoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean CreateConceptoPago(ConceptoPago conceptoPago) {
        String sql = "INSERT INTO concepto_pago (nombre, descripcion, precio, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, conceptoPago.getNombre());
            pstmt.setString(2, conceptoPago.getDescripcion());
            pstmt.setDouble(3, conceptoPago.getPrecio());
            pstmt.setString(4, conceptoPago.getEstado());
            int filas = pstmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e) {
            log.error("Error al crear Metodo de pago {}", e.getMessage());
            return false;
        }
    }

    @Override
    public ConceptoPago buscarConceptoPago(String dni) {
        return null;
    }

    @Override
    public List<ConceptoPago> ListarConceptoPago() {

        List<ConceptoPago> conceptoPago = new ArrayList<>();

        String sql = "SELECT * FROM concepto_pago";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ConceptoPago concepto = new ConceptoPago();
                concepto.setId_concepto(rs.getInt("id_concepto"));
                concepto.setNombre(rs.getString("nombre"));
                concepto.setDescripcion(rs.getString("descripcion"));
                concepto.setPrecio(rs.getDouble("precio"));
                concepto.setEstado(rs.getString("estado"));
                conceptoPago.add(concepto);
            }

        } catch (SQLException ex) {
            System.out.println("Error al Obtener concepto de pago" + ex.getMessage());
        }
        return conceptoPago;
    }

    @Override
    public Boolean ActualizarConceptoPago(ConceptoPago conceptoPago) {
        String sql = "UPDATE concepto_pago SET nombre=?, descripcion=?, precio=?,estado=? WHERE id_concepto=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, conceptoPago.getNombre());
            pstmt.setString(2, conceptoPago.getDescripcion());
            pstmt.setDouble(3, conceptoPago.getPrecio());
            pstmt.setString(4, conceptoPago.getEstado());
            pstmt.setInt(5, conceptoPago.getId_concepto());
            int filas = pstmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e){
            log.error("Error al actualizar concepto de pago {}", e.getMessage());
            return false;
        }

    }

    @Override
    public Boolean EliminarConceptoPago(int id) {
        String sql = "DELETE FROM concepto_pago WHERE id_concepto=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int filas = pstmt.executeUpdate();
            return (filas > 0);
        } catch (SQLException e){
            log.error("Error al eliminar concepto de pago {}", e.getMessage());
            return  false;
        }
    }
}
