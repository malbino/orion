/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.PagoFacade;

/**
 *
 * @author Tincho
 */
@Named("HistorialEconomicoEstudianteController")
@SessionScoped
public class HistorialEconomicoEstudianteController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    PagoFacade pagoFacade;
    @EJB
    ComprobanteFacade comprobanteFacade;

    private Carrera seleccionCarrera;
    private List<Pago> historialEconomico;

    @PostConstruct
    public void init() {
        historialEconomico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            historialEconomico = pagoFacade.kardexEconomico(loginController.getUsr().getId_persona(), seleccionCarrera.getId_carrera());
        }
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public Comprobante comprobante(Pago pago) {
        return comprobanteFacade.buscarComprobanteValido(pago.getId_pago());
    }

    /**
     * @return the seleccionCarrera
     */
    public Carrera getSeleccionCarrera() {
        return seleccionCarrera;
    }

    /**
     * @param seleccionCarrera the seleccionCarrera to set
     */
    public void setSeleccionCarrera(Carrera seleccionCarrera) {
        this.seleccionCarrera = seleccionCarrera;
    }

    /**
     * @return the historialEconomico
     */
    public List<Pago> getHistorialEconomico() {
        return historialEconomico;
    }

    /**
     * @param historialEconomico the historialEconomico to set
     */
    public void setHistorialEconomico(List<Pago> historialEconomico) {
        this.historialEconomico = historialEconomico;
    }

}
