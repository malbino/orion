/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;

/**
 *
 * @author Tincho
 */
@Named("CambioCarreraController")
@SessionScoped
public class CambioCarreraController extends AbstractController implements Serializable {

    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    InscripcionesFacade inscripcionesFacade;

    private Estudiante seleccionEstudiante;
    private Carrera seleccionCarrera;
    private GestionAcademica seleccionGestionAcademica;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        seleccionGestionAcademica = null;
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        seleccionGestionAcademica = null;
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarrera != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarrera.getRegimen());
        }
        return l;
    }

    public void registrarEstudiante() throws IOException {
        if (inscritoFacade.buscarInscrito(seleccionEstudiante.getId_persona(), seleccionCarrera.getId_carrera(), seleccionGestionAcademica.getId_gestionacademica()) == null) {
            if (inscripcionesFacade.cambioCarrera(seleccionEstudiante, seleccionCarrera, seleccionGestionAcademica)) {
                reinit();

                this.mensajeDeInformacion("Guardado.");
            } else {
                this.mensajeDeError("No se pudo registrar al estudiante.");
            }
        } else {
            this.mensajeDeError("Estudiante repetido.");
        }
    }

    /**
     * @return the seleccionEstudiante
     */
    public Estudiante getSeleccionEstudiante() {
        return seleccionEstudiante;
    }

    /**
     * @param seleccionEstudiante the seleccionEstudiante to set
     */
    public void setSeleccionEstudiante(Estudiante seleccionEstudiante) {
        this.seleccionEstudiante = seleccionEstudiante;
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
     * @return the seleccionGestionAcademica
     */
    public GestionAcademica getSeleccionGestionAcademica() {
        return seleccionGestionAcademica;
    }

    /**
     * @param seleccionGestionAcademica the seleccionGestionAcademica to set
     */
    public void setSeleccionGestionAcademica(GestionAcademica seleccionGestionAcademica) {
        this.seleccionGestionAcademica = seleccionGestionAcademica;
    }

}
