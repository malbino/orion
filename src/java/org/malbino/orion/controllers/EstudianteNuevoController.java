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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;

/**
 *
 * @author Tincho
 */
@Named("EstudianteNuevoController")
@SessionScoped
public class EstudianteNuevoController extends AbstractController implements Serializable {

    @EJB
    InscripcionesFacade inscripcionesFacade;
    @EJB
    EstudianteFacade estudianteFacade;

    private Estudiante nuevoEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    @PostConstruct
    public void init() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
    }

    public void reinit() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void registrarEstudiante() {
        if (estudianteFacade.buscarPorDni(nuevoEstudiante.getDni()) == null) {
            if (inscripcionesFacade.registrarEstudianteNuevo(nuevoEstudiante, seleccionCarrera, seleccionGestionAcademica)) {
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
     * @return the nuevoEstudiante
     */
    public Estudiante getNuevoEstudiante() {
        return nuevoEstudiante;
    }

    /**
     * @param nuevoEstudiante the nuevoEstudiante to set
     */
    public void setNuevoEstudiante(Estudiante nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
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
}
