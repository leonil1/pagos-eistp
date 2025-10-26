package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.Alumno;
import com.iestpdj.iestpdjpagos.model.ConceptoPago;

import java.util.List;

public interface IConceptoPagoDAO {
    boolean CreateConceptoPago(Alumno alumno);
    ConceptoPago buscarConceptoPago(String dni);
    List<ConceptoPago> ListarConceptoPago();
    Boolean ActualizarConceptoPago(ConceptoPago alumno);
    Boolean EliminarConceptoPago(Long id);
}
