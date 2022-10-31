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
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;

/**
 *
 * @author Tincho
 */
@Named("EstadoInscripcionController")
@SessionScoped
public class EstadoInscripcionController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    NotaFacade notaFacade;

    private CarreraEstudiante seleccionCarreraEstudiante;
    private Inscrito seleccionInscrito;

    private List<Nota> estadoInscripcion;

    @PostConstruct
    public void init() {
        seleccionCarreraEstudiante = null;
        seleccionInscrito = null;
        estadoInscripcion = new ArrayList();
    }

    public void reinit() {
        seleccionCarreraEstudiante = null;
        seleccionInscrito = null;
        estadoInscripcion = new ArrayList();
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (loginController.getUsr() != null && seleccionCarreraEstudiante != null) {
            l = inscritoFacade.listaInscritosPorEstudianteCarrera(loginController.getUsr().getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera());
        }
        return l;
    }

    public void actualizarEstadoInscripcion() {
        if (seleccionInscrito != null) {
            estadoInscripcion = notaFacade.listaNotas(seleccionInscrito.getId_inscrito());
        }
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
     * @return the seleccionInscrito
     */
    public Inscrito getSeleccionInscrito() {
        return seleccionInscrito;
    }

    /**
     * @param seleccionInscrito the seleccionInscrito to set
     */
    public void setSeleccionInscrito(Inscrito seleccionInscrito) {
        this.seleccionInscrito = seleccionInscrito;
    }

    /**
     * @return the estadoInscripcion
     */
    public List<Nota> getEstadoInscripcion() {
        return estadoInscripcion;
    }

    /**
     * @param estadoInscripcion the estadoInscripcion to set
     */
    public void setEstadoInscripcion(List<Nota> estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

}
