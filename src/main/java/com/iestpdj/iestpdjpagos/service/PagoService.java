package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.PagoDAO;
import com.iestpdj.iestpdjpagos.model.Pago;

import java.util.List;

public class PagoService implements IPagoService{

    private final PagoDAO pagoDAO = new PagoDAO();

    PagoService(PagoDAO pagoDAO) {
        super();
    }

    @Override
    public Void guardarPago(Pago pago) {
        return pagoDAO.guardarPago(pago);
    }

    @Override
    public List<Pago> listarPagos() {
        return pagoDAO.listarPagos();
    }
}
