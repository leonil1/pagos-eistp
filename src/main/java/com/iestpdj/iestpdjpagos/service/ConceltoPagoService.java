package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;
import com.iestpdj.iestpdjpagos.model.Estudiante;

import java.util.Iterator;
import java.util.List;

public class ConceltoPagoService implements IUConceptoPagoService{

    private ConceptoPagoDAO conceptoPagoDAO;


    public ConceltoPagoService() {
        this.conceptoPagoDAO = new ConceptoPagoDAO();
    }

    @Override
    public List<ConceptoPago> getConceptosPago() {
        return conceptoPagoDAO.ListarConceptoPago();
    }

    @Override
    public boolean crearConceptoPago(ConceptoPago conceptoPago) {
        if (!validarDatosRegistro(conceptoPago)) {
            return false;
        }

        ConceptoPago concepto = new  ConceptoPago();
        concepto.setNombre(conceptoPago.getNombre());
        concepto.setDescripcion(conceptoPago.getDescripcion());
        concepto.setPrecio(conceptoPago.getPrecio());
        concepto.setEstado(conceptoPago.getEstado());

        return conceptoPagoDAO.CreateConceptoPago(concepto);
    }

    @Override
    public boolean actualizarConceptoPago(ConceptoPago conceptoPago) {
        return conceptoPagoDAO.ActualizarConceptoPago(conceptoPago);
    }

    @Override
    public boolean eliminarConceptoPago(int id) {
        return conceptoPagoDAO.EliminarConceptoPago(id);
    }

    private boolean validarDatosRegistro(ConceptoPago conceptoPago) {
        if (conceptoPago.getNombre() == null || conceptoPago.getNombre().trim().isEmpty()){
            System.out.println("El nombre no puede ser vacio");
            return false;
        }
        return true;
    }
}
