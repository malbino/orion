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
import javax.inject.Named;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.facades.CampusFacade;
import org.malbino.orion.facades.InstitutoFacade;
import org.malbino.orion.util.Constantes;

/**
 *
 * @author Tincho
 */
@Named("CampusController")
@SessionScoped
public class CampusController extends AbstractController implements Serializable {

    @EJB
    CampusFacade campusFacade;
    @EJB
    InstitutoFacade institutoFacade;

    private List<Campus> campus;
    private Campus nuevoCampus;
    private Campus seleccionCampus;
    private Instituto instituto;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        campus = campusFacade.listaCampus();
        nuevoCampus = new Campus();
        seleccionCampus = null;
        instituto = institutoFacade.buscarPorId(Constantes.ID_INSTITUTO);

        filter = false;
        keyword = null;
    }

    public void reinit() {
        campus = campusFacade.listaCampus();
        nuevoCampus = new Campus();
        seleccionCampus = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            campus = campusFacade.listaCampus();
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        campus = campusFacade.buscar(keyword);
    }

    public void crearCampus() throws IOException {
        nuevoCampus.setInstituto(instituto);
        if (campusFacade.buscarPorSucursal(nuevoCampus.getSucursal()) == null) {
            if (campusFacade.create(nuevoCampus)) {
                this.toCampus();
            }
        } else {
            this.mensajeDeError("Campus repetido.");
        }
    }

    public void editarCampus() throws IOException {
        if (campusFacade.buscarPorSucursal(seleccionCampus.getSucursal(), seleccionCampus.getId_campus()) == null) {
            if (campusFacade.edit(seleccionCampus)) {
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
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
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
