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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.PostulanteFacade;
import org.malbino.orion.facades.negocio.AdmisionesFacade;
import org.malbino.orion.util.Fecha;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Tincho
 */
@Named("AdmisionesController")
@SessionScoped
public class AdmisionesController extends AbstractController implements Serializable {

    @EJB
    ActividadFacade actividadFacade;
    @EJB
    PostulanteFacade postulanteFacade;
    @EJB
    AdmisionesFacade admisionesFacade;
    @EJB
    PagoFacade pagoFacade;

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

    public void registrarPostulante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.ADMISION, nuevoPostulante.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            if (postulanteFacade.buscarPostulante(nuevoPostulante.getCi(), nuevoPostulante.getGestionAcademica().getId_gestionacademica(), nuevoPostulante.getCarrera().getId_carrera()) == null) {
                if (nuevoPostulante.getFoto() != null && !nuevoPostulante.getFoto().isEmpty()) {
                    if (admisionesFacade.registrarPostulante(nuevoPostulante)) {
                        reinit();

                        this.mensajeDeInformacion("Postulante registrado exitosamente.");
                    } else {
                        this.mensajeDeError("No se pudo registrar al postulante.");
                    }
                } else {
                    this.mensajeDeError("* Fotografía requerida.");
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
                if (seleccionPostulante.getFoto() != null && !seleccionPostulante.getFoto().isEmpty()) {
                    if (postulanteFacade.edit(seleccionPostulante)) {
                        reinit();

                        this.mensajeDeInformacion("Postulante modificado exitosamente.");
                    } else {
                        this.mensajeDeError("No se pudo modificar al postulante.");
                    }
                } else {
                    this.mensajeDeError("* Fotografía requerida.");
                }
            } else {
                this.mensajeDeError("Postulante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void toRegistrarPostulante() throws IOException {
        this.redireccionarViewId("/admisiones/registrarPostulante");
    }

    public void toModificarPostulante() throws IOException {
        this.redireccionarViewId("/admisiones/modificar/modificarPostulante");
    }

    public void toFormularioPostulante() throws IOException {
        this.redireccionarViewId("/admisiones/imprimir/formularioPostulante");
    }

    public void toHome() throws IOException {
        this.reinit();

        this.redireccionarViewId("/admisiones/home");
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
