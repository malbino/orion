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
    private List<Nota> kardexAcademico;

    @PostConstruct
    public void init() {
        kardexAcademico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            kardexAcademico = notaFacade.kardexAcademico(loginController.getUsr().getId_persona(), seleccionCarrera.getId_carrera());
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
     * @return the kardexAcademico
     */
    public List<Nota> getKardexAcademico() {
        return kardexAcademico;
    }

    /**
     * @param kardexAcademico the kardexAcademico to set
     */
    public void setKardexAcademico(List<Nota> kardexAcademico) {
        this.kardexAcademico = kardexAcademico;
    }

}
