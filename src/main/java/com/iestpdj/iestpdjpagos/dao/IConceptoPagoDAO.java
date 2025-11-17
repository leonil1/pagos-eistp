package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Estudiante;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;

import java.util.List;

public interface IConceptoPagoDAO {
    boolean CreateConceptoPago(ConceptoPago conceptoPago);
    ConceptoPago buscarConceptoPago(String dni);
    List<ConceptoPago> ListarConceptoPago();
    Boolean ActualizarConceptoPago(ConceptoPago conceptoPago);
    Boolean EliminarConceptoPago(int id);
}
