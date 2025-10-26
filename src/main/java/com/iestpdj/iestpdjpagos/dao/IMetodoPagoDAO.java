package com.iestpdj.iestpdjpagos.dao;


import com.iestpdj.iestpdjpagos.model.MetodoPago;

import java.util.List;

public interface IMetodoPagoDAO {
    boolean CreateMetodoPago(MetodoPago metodoPago);
    List<MetodoPago> listarMetodoPagos();
    Boolean ActualizarMetodoPago(MetodoPago metodoPago);
    Boolean EliminarMetodoPago(Long id);
    Boolean getByIdMetodoPago(Integer id);
}
