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
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.MencionFacade;
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
    @EJB
    MencionFacade mencionFacade;

    private Estudiante nuevoEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    private CarreraEstudiante seleccionCarreraEstudiante;

    private Comprobante nuevoComprobante;

    @PostConstruct
    public void init() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarreraEstudiante = null;

        nuevoComprobante = new Comprobante();
    }

    public void reinit() {
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        seleccionCarreraEstudiante = null;

        nuevoComprobante = new Comprobante();
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList<>();
        if (seleccionGestionAcademica != null) {
            List<Carrera> carreras = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
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

                if (inscripcionesFacade.registrarEstudianteNuevo(nuevoEstudiante, seleccionCarreraEstudiante, seleccionGestionAcademica, nuevoComprobante)) {
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
