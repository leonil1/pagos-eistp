package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Alumno;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConceptoPagoDAO implements IConceptoPagoDAO {

    private Connection connection;

    public ConceptoPagoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean CreateConceptoPago(Alumno alumno) {
        return false;
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
    public Boolean ActualizarConceptoPago(ConceptoPago alumno) {
        return null;
    }

    @Override
    public Boolean EliminarConceptoPago(Long id) {
        return null;
    }
}
