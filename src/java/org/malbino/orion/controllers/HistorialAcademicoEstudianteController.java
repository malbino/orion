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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.facades.CarreraEstudianteFacade;
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
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

    private CarreraEstudiante seleccionCarreraEstudiante;
    private List<Nota> historialAcademico;

    @PostConstruct
    public void init() {
        historialAcademico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarreraEstudiante != null) {
            Estudiante estudiante = estudianteFacade.find(loginController.getUsr().getId_persona());
            if (estudiante != null) {
                historialAcademico = notaFacade.historialAcademico(estudiante, seleccionCarreraEstudiante.getCarrera(), seleccionCarreraEstudiante.getMencion());
            }
        }
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
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
