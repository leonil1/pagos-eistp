package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.Pago;

import java.util.List;

public interface IPagoService {
    Void guardarPago(Pago pago);

    List<Pago> listarPagos();
}
