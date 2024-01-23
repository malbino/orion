/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.AdmisionesFacade;
import org.malbino.orion.util.Fecha;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Tincho
 */
@Named("PostulantesController")
@SessionScoped
public class PostulantesController extends AbstractController implements Serializable {

    @EJB
    ActividadFacade actividadFacade;
    @EJB
    AdmisionesFacade admisionesFacade;
    @EJB
    PagoFacade pagoFacade;
    @Inject
    LoginController loginController;

    private Postulante nuevoPostulante;
    private Postulante seleccionPostulante;

    private String ci;
    private Date fechaNacimiento;
    private GestionAcademica gestionAcademica;
    private Carrera carrera;

    @PostConstruct
    public void init() {
        nuevoPostulante = new Postulante();
        seleccionPostulante = null;

        ci = null;
        fechaNacimiento = null;
        gestionAcademica = null;
        carrera = null;
    }

    public void reinit() {
        nuevoPostulante = new Postulante();
        seleccionPostulante = null;

        ci = null;
        fechaNacimiento = null;
        gestionAcademica = null;
        carrera = null;
    }

    public void subirArchivoRegistrarPostulante(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/photos");
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        Path file = null;
        try (InputStream input = event.getFile().getInputStream()) {
            file = Files.createTempFile(folder, null, "." + extension);
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);

            nuevoPostulante.setFoto(file.getFileName().toString());
        } catch (IOException ex) {

        }
    }

    public void subirArchivoModificarPostulante(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/photos");
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        Path file = null;
        try (InputStream input = event.getFile().getInputStream()) {
            file = Files.createTempFile(folder, null, "." + extension);
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);

            seleccionPostulante.setFoto(file.getFileName().toString());
        } catch (IOException ex) {

        }
    }

    public List<Carrera> listaCarrerasRegistrarPostulante() {
        List<Carrera> l = new ArrayList();
        if (nuevoPostulante.getGestionAcademica() != null) {
            l = carreraFacade.listaCarreras(nuevoPostulante.getGestionAcademica().getRegimen());
        }
        return l;
    }

    public List<Carrera> listaCarrerasModificarPostulante() {
        List<Carrera> l = new ArrayList();
        if (seleccionPostulante.getGestionAcademica() != null) {
            l = carreraFacade.listaCarreras(seleccionPostulante.getGestionAcademica().getRegimen());
        }
        return l;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (gestionAcademica != null) {
            l = carreraFacade.listaCarreras(gestionAcademica.getRegimen());
        }
        return l;
    }

    public void registrarPostulante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.ADMISION, nuevoPostulante.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            if (postulanteFacade.buscarPostulante(nuevoPostulante.getCi(), nuevoPostulante.getGestionAcademica().getId_gestionacademica(), nuevoPostulante.getCarrera().getId_carrera()) == null) {
                if (admisionesFacade.registrarPostulante(nuevoPostulante)) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.POSTULANTE, nuevoPostulante.getId_postulante(), "Creación por registro de formulario de postulación"));

                    this.insertarParametro("id_postulante", nuevoPostulante.getId_postulante());

                    toFormularioPostulante();
                } else {
                    this.mensajeDeError("No se pudo registrar al postulante.");
                }
            } else {
                this.mensajeDeError("Postulante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void buscarPostulanteModificarPostulante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.ADMISION, gestionAcademica.getId_gestionacademica()).isEmpty()) {
            seleccionPostulante = postulanteFacade.buscarPostulante(ci, fechaNacimiento, gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
            if (seleccionPostulante != null) {
                if (!pagoFacade.listaPagosAdeudadosPostulante(seleccionPostulante.getId_postulante()).isEmpty()) {
                    toModificarPostulante();
                } else {
                    this.mensajeDeError("Postulante inscrito.");
                }
            } else {
                this.mensajeDeError("Postulante no encontrado.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void buscarPostulanteImprimir() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.ADMISION, gestionAcademica.getId_gestionacademica()).isEmpty()) {
            seleccionPostulante = postulanteFacade.buscarPostulante(ci, fechaNacimiento, gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
            if (seleccionPostulante != null) {
                if (!pagoFacade.listaPagosAdeudadosPostulante(seleccionPostulante.getId_postulante()).isEmpty()) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.POSTULANTE, seleccionPostulante.getId_postulante(), "Impresión formulario de postulación"));

                    this.insertarParametro("id_postulante", seleccionPostulante.getId_postulante());

                    toFormularioPostulante();
                } else {
                    this.mensajeDeError("Postulante inscrito.");
                }
            } else {
                this.mensajeDeError("Postulante no encontrado.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void modificarPostulante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.ADMISION, seleccionPostulante.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            if (postulanteFacade.buscarPostulante(seleccionPostulante.getCi(), seleccionPostulante.getGestionAcademica().getId_gestionacademica(), seleccionPostulante.getCarrera().getId_carrera(), seleccionPostulante.getId_postulante()) == null) {
                if (postulanteFacade.edit(seleccionPostulante)) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.POSTULANTE, seleccionPostulante.getId_postulante(), "Creación por actualización de formulario de postulación"));

                    toHome();
                } else {
                    this.mensajeDeError("No se pudo modificar al postulante.");
                }
            } else {
                this.mensajeDeError("Postulante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void toRegistrarPostulante() throws IOException {
        this.redireccionarViewId("/postulantes/registrarPostulante");
    }

    public void toModificarPostulante() throws IOException {
        this.redireccionarViewId("/postulantes/modificar/modificarPostulante");
    }

    public void toFormularioPostulante() throws IOException {
        this.redireccionarViewId("/postulantes/imprimir/formularioPostulante");
    }

    public void toHome() throws IOException {
        this.reinit();

        this.redireccionarViewId("/postulantes/home");
    }

    /**
     * @return the nuevoPostulante
     */
    public Postulante getNuevoPostulante() {
        return nuevoPostulante;
    }

    /**
     * @param nuevoPostulante the nuevoPostulante to set
     */
    public void setNuevoPostulante(Postulante nuevoPostulante) {
        this.nuevoPostulante = nuevoPostulante;
    }

    /**
     * @return the seleccionPostulante
     */
    public Postulante getSeleccionPostulante() {
        return seleccionPostulante;
    }

    /**
     * @param seleccionPostulante the seleccionPostulante to set
     */
    public void setSeleccionPostulante(Postulante seleccionPostulante) {
        this.seleccionPostulante = seleccionPostulante;
    }

    /**
     * @return the ci
     */
    public String getCi() {
        return ci;
    }

    /**
     * @param ci the ci to set
     */
    public void setCi(String ci) {
        this.ci = ci;
    }

    /**
     * @return the fechaNacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento the fechaNacimiento to set
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * @return the gestionAcademica
     */
    public GestionAcademica getGestionAcademica() {
        return gestionAcademica;
    }

    /**
     * @param gestionAcademica the gestionAcademica to set
     */
    public void setGestionAcademica(GestionAcademica gestionAcademica) {
        this.gestionAcademica = gestionAcademica;
    }

    /**
     * @return the carrera
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }
}
