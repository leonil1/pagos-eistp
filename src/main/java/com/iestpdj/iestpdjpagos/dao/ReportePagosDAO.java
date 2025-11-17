package com.iestpdj.iestpdjpagos.dao;

import com.iestpdj.iestpdjpagos.model.PagoReporte;

import java.time.LocalDate;
import java.util.List;

public interface ReportePagosDAO {
    List<PagoReporte> obtenerPagosPorDia(LocalDate fecha);

    List<PagoReporte> obtenerPagosPorRango(LocalDate inicio, LocalDate fin);

    List<PagoReporte> buscarPorRango(LocalDate inicio, LocalDate fin);

    double totalIngresosPorDia(LocalDate fecha);
}
