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
import org.malbino.orion.entities.Nota;
import org.malbino.orion.facades.NotaFacade;

/**
 *
 * @author Tincho
 */
@Named("HistorialAcademicoEstudianteController")
@SessionScoped
public class HistorialAcademicoEstudianteController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    NotaFacade notaFacade;

    private Carrera seleccionCarrera;
    private List<Nota> historialAcademico;

    @PostConstruct
    public void init() {
        historialAcademico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            historialAcademico = notaFacade.historialAcademico(loginController.getUsr().getId_persona(), seleccionCarrera.getId_carrera());
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
     * @return the historialAcademico
     */
    public List<Nota> getHistorialAcademico() {
        return historialAcademico;
    }

    /**
     * @param historialAcademico the historialAcademico to set
     */
    public void setHistorialAcademico(List<Nota> historialAcademico) {
        this.historialAcademico = historialAcademico;
    }

}
