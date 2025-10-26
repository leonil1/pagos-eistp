package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Pago;

import java.util.List;

public interface IPagoDAO {

    Void guardarPago(Pago pago);

    List<Pago> listarPagos();
}
