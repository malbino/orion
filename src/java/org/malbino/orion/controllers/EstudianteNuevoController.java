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
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;

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
    ActividadFacade actividadFacade;

    private Estudiante nuevoEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    private Comprobante nuevoComprobante;

    @PostConstruct
    public void init() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarrera = null;

        nuevoComprobante = new Comprobante();
    }

    public void reinit() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarrera = null;

        nuevoComprobante = new Comprobante();
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void registrarEstudiante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (estudianteFacade.buscarPorDni(nuevoEstudiante.getDni()) == null) {
                nuevoComprobante.setFecha(Fecha.getDate());
                nuevoComprobante.setValido(true);

                String contrasena = Generador.generarContrasena();
                nuevoEstudiante.setContrasena(Encriptador.encriptar(contrasena));
                nuevoEstudiante.setContrasenaSinEncriptar(contrasena);

                if (inscripcionesFacade.registrarEstudianteNuevo(nuevoEstudiante, seleccionCarrera, seleccionGestionAcademica, nuevoComprobante)) {
                    this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                    this.insertarParametro("est", nuevoEstudiante);

                    reinit();

                    this.toComprobantePago();
                } else {
                    this.mensajeDeError("No se pudo registrar al estudiante.");
                }
            } else {
                this.mensajeDeError("Estudiante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }
    
    public void toEstudianteNuevo() throws IOException {
        this.redireccionarViewId("/inscripciones/estudianteNuevo/estudianteNuevo");
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/inscripciones/estudianteNuevo/comprobantePago");
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
