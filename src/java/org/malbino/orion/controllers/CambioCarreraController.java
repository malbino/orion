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
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.MencionFacade;
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
    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;
    
    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    
    private Comprobante nuevoComprobante;
    
    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;
        
        nuevoComprobante = new Comprobante();
    }
    
    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;
        
        nuevoComprobante = new Comprobante();
    }
    
    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList<>();
        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {
            
            List<Mencion> menciones = mencionFacade.listaMenciones(carrera.getId_carrera());
            if (menciones.isEmpty()) {
                CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                carreraEstudianteId.setId_persona(0);
                CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                carreraEstudiante.setCarrera(carrera);
                
                l.add(carreraEstudiante);
            } else {
                for (Mencion mencion : menciones) {
                    CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                    carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                    carreraEstudianteId.setId_persona(0);
                    CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                    carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                    carreraEstudiante.setMencion(mencion);
                    carreraEstudiante.setCarrera(carrera);
                    
                    l.add(carreraEstudiante);
                }
            }
        }
        return l;
    }
    
    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarreraEstudiante != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarreraEstudiante.getCarrera().getRegimen(), true);
        }
        return l;
    }
    
    public void registrarEstudiante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (inscritoFacade.buscarInscrito(seleccionEstudiante.getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera(), seleccionGestionAcademica.getId_gestionacademica()) == null) {
                if (seleccionEstudiante.getDiplomaBachiller()) {
                    nuevoComprobante.setFecha(Fecha.getDate());
                    nuevoComprobante.setValido(true);
                    nuevoComprobante.setUsuario(loginController.getUsr());
                    
                    String contrasena = Generador.generarContrasena();
                    seleccionEstudiante.setContrasena(Encriptador.encriptar(contrasena));
                    seleccionEstudiante.setContrasenaSinEncriptar(contrasena);
                    if (inscripcionesFacade.cambioCarrera(seleccionEstudiante, seleccionCarreraEstudiante, seleccionGestionAcademica, nuevoComprobante)) {
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
