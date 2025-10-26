package com.iestpdj.iestpdjpagos.service;

import com.iestpdj.iestpdjpagos.dao.IMetodoPagoDAO;
import com.iestpdj.iestpdjpagos.dao.MetodoPagoDAO;
import com.iestpdj.iestpdjpagos.model.MetodoPago;

import java.util.List;

public class MetodoPagoService implements IMetodoPago{

    private IMetodoPagoDAO iMetodoPagoDAO;

    public MetodoPagoService(){
        this.iMetodoPagoDAO = new MetodoPagoDAO();
    }

    @Override
    public boolean CreateMetodoPago(MetodoPago metodoPago) {
        if (!validarDatosRegistro(metodoPago)){
            return false;
        }

        MetodoPago metodoPagoSaved = new MetodoPago();
        metodoPagoSaved.setNombre(metodoPago.getNombre());
        metodoPagoSaved.setDescripcion(metodoPago.getDescripcion());
        metodoPagoSaved.setActivo(metodoPago.isActivo());
        return iMetodoPagoDAO.ActualizarMetodoPago(metodoPagoSaved);
    }

    @Override
    public Boolean GetMetodoPagoById(Integer id) {
        if (id == null || id < 0){
            return null;
        }
        return iMetodoPagoDAO.getByIdMetodoPago(id);
    }

    @Override
    public List<MetodoPago> listaMetodoPago() {
        return iMetodoPagoDAO.listarMetodoPagos();
    }

    @Override
    public boolean UpdateMetodoPago(MetodoPago metodoPago) {
        return false;
    }

    @Override
    public Boolean EliminarMetodoPago(Long id) {
        return null;
    }

    private boolean validarDatosRegistro(MetodoPago metodoPago){
        if (metodoPago.getNombre() == null || metodoPago.getNombre().trim().isEmpty()){
            System.out.println("No se puede ingresar el nombre es vacio");
            return false;
        }
        return true;
    }
}
