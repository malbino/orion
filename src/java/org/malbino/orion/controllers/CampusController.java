/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.InstitutoFacade;
import org.malbino.orion.util.Constantes;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("CampusController")
@SessionScoped
public class CampusController extends AbstractController implements Serializable {

    @EJB
    InstitutoFacade institutoFacade;
    @Inject
    LoginController loginController;

    private List<Campus> campus;
    private Campus nuevoCampus;
    private Campus seleccionCampus;
    private Instituto instituto;

    private String keyword;

    @PostConstruct
    public void init() {
        campus = campusFacade.listaCampus();
        nuevoCampus = new Campus();
        seleccionCampus = null;
        instituto = institutoFacade.buscarPorId(Constantes.ID_INSTITUTO);

        keyword = null;
    }

    public void reinit() {
        campus = campusFacade.listaCampus();
        nuevoCampus = new Campus();
        seleccionCampus = null;

        keyword = null;
    }

    public void buscar() {
        campus = campusFacade.buscar(keyword);
    }

    public void crearCampus() throws IOException {
        nuevoCampus.setInstituto(instituto);
        if (campusFacade.buscarPorSucursal(nuevoCampus.getSucursal()) == null) {
            if (campusFacade.create(nuevoCampus)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.CAMPUS, nuevoCampus.getId_campus(), "Creación campus", loginController.getUsr().toString()));

                this.toCampus();
            }
        } else {
            this.mensajeDeError("Campus repetido.");
        }
    }

    public void editarCampus() throws IOException {
        if (campusFacade.buscarPorSucursal(seleccionCampus.getSucursal(), seleccionCampus.getId_campus()) == null) {
            if (campusFacade.edit(seleccionCampus)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CAMPUS, seleccionCampus.getId_campus(), "Actualización campus", loginController.getUsr().toString()));

                this.toCampus();
            }
        } else {
            this.mensajeDeError("Campus repetido.");
        }
    }

    public void toNuevoCampus() throws IOException {
        this.redireccionarViewId("/planesEstudio/campus/nuevoCampus");
    }

    public void toEditarCampus() throws IOException {
        this.redireccionarViewId("/planesEstudio/campus/editarCampus");
    }

    public void toCampus() throws IOException {
        reinit();

        this.redireccionarViewId("/planesEstudio/campus/campus");
    }

    /**
     * @return the campus
     */
    public List<Campus> getCampus() {
        return campus;
    }

    /**
     * @param campus the campus to set
     */
    public void setCampus(List<Campus> campus) {
        this.campus = campus;
    }

    /**
     * @return the nuevoCampus
     */
    public Campus getNuevoCampus() {
        return nuevoCampus;
    }

    /**
     * @param nuevoCampus the nuevoCampus to set
     */
    public void setNuevoCampus(Campus nuevoCampus) {
        this.nuevoCampus = nuevoCampus;
    }

    /**
     * @return the seleccionCampus
     */
    public Campus getSeleccionCampus() {
        return seleccionCampus;
    }

    /**
     * @param seleccionCampus the seleccionCampus to set
     */
    public void setSeleccionCampus(Campus seleccionCampus) {
        this.seleccionCampus = seleccionCampus;
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
