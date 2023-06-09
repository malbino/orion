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
import org.malbino.orion.entities.Actividad;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("CalendarioAcademicoController")
@SessionScoped
public class CalendarioAcademicoController extends AbstractController implements Serializable {

    @EJB
    ActividadFacade actividadFacade;
    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;

    private List<Actividad> actividades;
    private Actividad nuevaActividad;
    private Actividad seleccionActividad;

    private String keyword;

    @PostConstruct
    public void init() {
        actividades = new ArrayList();
        nuevaActividad = new Actividad();
        seleccionActividad = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null) {
            actividades = actividadFacade.listaActividad(seleccionGestionAcademica.getId_gestionacademica());
        }
        nuevaActividad = new Actividad();
        seleccionActividad = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionGestionAcademica != null) {
            actividades = actividadFacade.buscar(keyword, seleccionGestionAcademica.getId_gestionacademica());
        }
    }

    public void crearActividad() throws IOException {
        nuevaActividad.setGestionAcademica(seleccionGestionAcademica);
        if (actividadFacade.create(nuevaActividad)) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.ACTIVIDAD, nuevaActividad.getId_actividad(), "Creación actividad", loginController.getUsr().toString()));

            this.toCalendarioAcademico();
        }
    }

    public void editarActividad() throws IOException {
        if (actividadFacade.edit(seleccionActividad)) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.ACTIVIDAD, seleccionActividad.getId_actividad(), "Actualización actividad", loginController.getUsr().toString()));

            this.toCalendarioAcademico();
        }
    }

    public void toNuevaActividad() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/calendarioAcademico/nuevaActividad");
    }

    public void toEditarActividad() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/calendarioAcademico/editarActividad");
    }

    public void toCalendarioAcademico() throws IOException {
        reinit();

        this.redireccionarViewId("/gestionesAcademicas/calendarioAcademico/calendarioAcademico");
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
     * @return the actividades
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     * @param actividades the actividades to set
     */
    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    /**
     * @return the nuevaActividad
     */
    public Actividad getNuevaActividad() {
        return nuevaActividad;
    }

    /**
     * @param nuevaActividad the nuevaActividad to set
     */
    public void setNuevaActividad(Actividad nuevaActividad) {
        this.nuevaActividad = nuevaActividad;
    }

    /**
     * @return the seleccionActividad
     */
    public Actividad getSeleccionActividad() {
        return seleccionActividad;
    }

    /**
     * @param seleccionActividad the seleccionActividad to set
     */
    public void setSeleccionActividad(Actividad seleccionActividad) {
        this.seleccionActividad = seleccionActividad;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
