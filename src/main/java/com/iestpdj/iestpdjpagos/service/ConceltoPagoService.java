package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.ConceptoPagoDAO;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;

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
}
