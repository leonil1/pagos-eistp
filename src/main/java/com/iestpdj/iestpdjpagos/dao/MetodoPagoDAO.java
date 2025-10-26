package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.MetodoPago;
import com.iestpdj.iestpdjpagos.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDAO implements IMetodoPagoDAO{

    private Connection connection;

    public MetodoPagoDAO(){

        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean CreateMetodoPago(MetodoPago metodoPago) {
        String sql = "INSERT INTO metodo_pago (nombre, descripcion, activo) VALUES (?,?,?)";
        try(PreparedStatement ps = this.connection.prepareStatement(sql)) {

            ps.setString(1, metodoPago.getNombre());
            ps.setString(2, metodoPago.getDescripcion());
            ps.setBoolean(3, metodoPago.isActivo());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException ex){
            System.out.println("Error al crear Matodo de pago: " + ex.getMessage());
            return false;
        }

    }

    @Override
    public List<MetodoPago> listarMetodoPagos() {
        List<MetodoPago> listarMetodoPagos = new ArrayList<>();
        String sql = "SELECT * FROM metodo_pago WHERE activo = TRUE ORDER BY nombre";

        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MetodoPago metodoPago = new MetodoPago();
                metodoPago.setId_metodo(rs.getLong("id_metodo"));
                metodoPago.setNombre(rs.getString("nombre"));
                metodoPago.setDescripcion(rs.getString("descripcion"));
                metodoPago.setActivo(rs.getBoolean("activo"));
                listarMetodoPagos.add(metodoPago);
            }

        } catch (SQLException es){
            System.out.println("Error allistar Metodo pago"+ es.getMessage());
        }
        return listarMetodoPagos;
    }

    @Override
    public Boolean ActualizarMetodoPago(MetodoPago metodoPago) {
        String sql = "UPDATE metodo_pago SET nombre=?, descripcion=?, activo=? WHERE id_metodo=?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, metodoPago.getNombre());
            ps.setString(2, metodoPago.getDescripcion());
            ps.setBoolean(3, metodoPago.isActivo());
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException ex){
            System.out.println("Error alActualizar el Metodo pago");
            return false;
        }
    }

    @Override
    public Boolean EliminarMetodoPago(Long id) {
        String sql = "DELETE FROM metodo_pago WHERE id_metodo=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException ex){
            System.out.println("Error al Eliminar metodo de pago " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Boolean getByIdMetodoPago(Integer id) {
        String sql = "SElECT * FROM metodo_pago WHERE id_metodo=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException ex){
         System.out.println("No se Encuanta el Metodo Pago: " + ex.getMessage());
         return false;
        }
    }
}
