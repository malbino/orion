/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;

/**
 *
 * @author Tincho
 */
@Named("ReporteFichaEvaluacionTutorController")
@SessionScoped
public class ReporteFichaEvaluacionTutorController extends AbstractController implements Serializable {

    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private GrupoPasantia seleccionGrupoPasantia;
    private List<NotaPasantia> notasPasantias;
    private NotaPasantia seleccionNotaPasantia;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        notasPasantias = new ArrayList<>();
        seleccionNotaPasantia = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        notasPasantias = notaPasantiaFacade.listaNotasPasantias(seleccionGrupoPasantia.getId_grupopasantia());
        seleccionNotaPasantia = null;

        filter = false;
        keyword = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public List<GrupoPasantia> listaGruposPasantias() {
        List<GrupoPasantia> l = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            l = grupoPasantiaFacade.listaGrupoPasantias(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public List<GrupoPasantia> listaGruposPasantiasEditarPasantia() {
        List<GrupoPasantia> l = new ArrayList<>();
        if (seleccionNotaPasantia != null) {
            l = grupoPasantiaFacade.listaGrupoPasantiasAbiertos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            notasPasantias = notaPasantiaFacade.listaNotasPasantias(seleccionGrupoPasantia.getId_grupopasantia());
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionGrupoPasantia != null) {
            notasPasantias = notaPasantiaFacade.buscar(seleccionGrupoPasantia, keyword);
        }
    }

    public void toFichaEvaluacionTutor() throws IOException {
        this.insertarParametro("id_notapasantia", seleccionNotaPasantia.getId_notapasantia());

        this.redireccionarViewId("/reportes/fichaEvaluacionTutor/fichaEvaluacionTutor");
    }

    public void toReporteFichaEvaluacionTutor() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/fichaEvaluacionTutor/reporteFichaEvaluacionTutor");
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
     * @return the notasPasantias
     */
    public List<NotaPasantia> getNotasPasantias() {
        return notasPasantias;
    }

    /**
     * @param notasPasantias the notasPasantias to set
     */
    public void setNotasPasantias(List<NotaPasantia> notasPasantias) {
        this.notasPasantias = notasPasantias;
    }

    /**
     * @return the seleccionNotaPasantia
     */
    public NotaPasantia getSeleccionNotaPasantia() {
        return seleccionNotaPasantia;
    }

    /**
     * @param seleccionNotaPasantia the seleccionNotaPasantia to set
     */
    public void setSeleccionNotaPasantia(NotaPasantia seleccionNotaPasantia) {
        this.seleccionNotaPasantia = seleccionNotaPasantia;
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
     * @return the seleccionGrupoPasantia
     */
    public GrupoPasantia getSeleccionGrupoPasantia() {
        return seleccionGrupoPasantia;
    }

    /**
     * @param seleccionGrupoPasantia the seleccionGrupoPasantia to set
     */
    public void setSeleccionGrupoPasantia(GrupoPasantia seleccionGrupoPasantia) {
        this.seleccionGrupoPasantia = seleccionGrupoPasantia;
    }

}
