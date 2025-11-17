package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.ReportePagosDAO;
import com.iestpdj.iestpdjpagos.dao.ReportePagosDAOImpl;
import com.iestpdj.iestpdjpagos.model.PagoReporte;

import java.time.LocalDate;
import java.util.List;

public class ReportePagoService {
    private final ReportePagosDAO dao = new ReportePagosDAOImpl();

    public List<PagoReporte> pagosPorDia(LocalDate fecha) {
        return dao.obtenerPagosPorDia(fecha);
    }

    public List<PagoReporte> pagosPorRango(LocalDate inicio, LocalDate fin) {
        return dao.obtenerPagosPorRango(inicio, fin);
    }
}
