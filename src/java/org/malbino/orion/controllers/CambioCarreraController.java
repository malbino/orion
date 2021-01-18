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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;

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
    @EJB
    ActividadFacade actividadFacade;

    private Estudiante seleccionEstudiante;
    private Carrera seleccionCarrera;
    private GestionAcademica seleccionGestionAcademica;

    private Comprobante nuevoComprobante;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        seleccionGestionAcademica = null;

        nuevoComprobante = new Comprobante();
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        seleccionGestionAcademica = null;

        nuevoComprobante = new Comprobante();
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarrera != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarrera.getRegimen(), true);
        }
        return l;
    }

    public void registrarEstudiante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (inscritoFacade.buscarInscrito(seleccionEstudiante.getId_persona(), seleccionCarrera.getId_carrera(), seleccionGestionAcademica.getId_gestionacademica()) == null) {
                if (seleccionEstudiante.getDiplomaBachiller()) {
                    nuevoComprobante.setFecha(Fecha.getDate());
                    nuevoComprobante.setValido(true);

                    String contrasena = Generador.generarContrasena();
                    seleccionEstudiante.setContrasena(Encriptador.encriptar(contrasena));
                    seleccionEstudiante.setContrasenaSinEncriptar(contrasena);
                    if (inscripcionesFacade.cambioCarrera(seleccionEstudiante, seleccionCarrera, seleccionGestionAcademica, nuevoComprobante)) {
                        this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                        this.insertarParametro("est", seleccionEstudiante);

                        reinit();

                        this.toComprobantePago();
                    } else {
                        this.mensajeDeError("No se pudo registrar al estudiante.");
                    }
                } else {
                    this.mensajeDeError("Estudiante sin titulo de bachiller.");
                }
            } else {
                this.mensajeDeError("Estudiante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void toCambioCarrera() throws IOException {
        this.redireccionarViewId("/inscripciones/cambioCarrera/cambioCarrera");
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/inscripciones/cambioCarrera/comprobantePago");
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

    /**
     * @return the nuevoComprobante
     */
    public Comprobante getNuevoComprobante() {
        return nuevoComprobante;
    }

    /**
     * @param nuevoComprobante the nuevoComprobante to set
     */
    public void setNuevoComprobante(Comprobante nuevoComprobante) {
        this.nuevoComprobante = nuevoComprobante;
    }

}
