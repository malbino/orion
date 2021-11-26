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
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.CarreraEstudianteFacade;
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
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

    private CarreraEstudiante seleccionCarreraEstudiante;
    private List<Pago> historialEconomico;

    @PostConstruct
    public void init() {
        historialEconomico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarreraEstudiante != null) {
            historialEconomico = pagoFacade.kardexEconomico(loginController.getUsr().getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera());
        }
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public Comprobante comprobante(Pago pago) {
        return comprobanteFacade.buscarComprobanteValido(pago.getId_pago());
    }

    /**
     * @return the seleccionCarreraEstudiante
     */
    public CarreraEstudiante getSeleccionCarreraEstudiante() {
        return seleccionCarreraEstudiante;
    }

    /**
     * @param seleccionCarreraEstudiante the seleccionCarreraEstudiante to set
     */
    public void setSeleccionCarreraEstudiante(CarreraEstudiante seleccionCarreraEstudiante) {
        this.seleccionCarreraEstudiante = seleccionCarreraEstudiante;
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
