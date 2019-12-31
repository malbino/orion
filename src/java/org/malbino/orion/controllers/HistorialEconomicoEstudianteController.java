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
import org.malbino.orion.entities.Detalle;
import org.malbino.orion.facades.DetalleFacade;

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
    DetalleFacade detalleFacade;

    private Carrera seleccionCarrera;
    private List<Detalle> historialEconomico;

    @PostConstruct
    public void init() {
        historialEconomico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            historialEconomico = detalleFacade.kardexEconomico(loginController.getUsr().getId_persona(), seleccionCarrera.getId_carrera());
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

    public int total() {
        int t = 0;
        for (Detalle detalle : historialEconomico) {
            t += detalle.getMonto();
        }
        return t;
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
    public List<Detalle> getHistorialEconomico() {
        return historialEconomico;
    }

    /**
     * @param historialEconomico the historialEconomico to set
     */
    public void setHistorialEconomico(List<Detalle> historialEconomico) {
        this.historialEconomico = historialEconomico;
    }

}
