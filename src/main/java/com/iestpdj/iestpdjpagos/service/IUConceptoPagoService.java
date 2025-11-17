package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.model.ConceptoPago;

import java.util.Iterator;
import java.util.List;

public interface IUConceptoPagoService {

    List<ConceptoPago> getConceptosPago();
    boolean crearConceptoPago(ConceptoPago conceptoPago);
    boolean actualizarConceptoPago(ConceptoPago conceptoPago);
    boolean eliminarConceptoPago(int id);
}
