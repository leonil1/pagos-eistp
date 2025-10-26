package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.MetodoPago;

import java.util.List;

public interface IMetodoPago {
    boolean CreateMetodoPago(MetodoPago metodoPago);
    Boolean GetMetodoPagoById(Integer id);
    List<MetodoPago> listaMetodoPago();
    boolean UpdateMetodoPago(MetodoPago metodoPago);
    Boolean EliminarMetodoPago(Long id);
}
