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
import org.malbino.orion.entities.IndicadorPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.IndicadorPasantiaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.facades.negocio.EvaluacionPasantiaFacade;

/**
 *
 * @author Tincho
 */
@Named("NotaPasantiaController")
@SessionScoped
public class NotaPasantiaController extends AbstractController implements Serializable {

    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;
    @EJB
    IndicadorPasantiaFacade indicadorPasantiaFacade;
    @EJB
    EvaluacionPasantiaFacade evaluacionPasantiaFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private GrupoPasantia seleccionGrupoPasantia;
    private List<NotaPasantia> notasPasantias;
    private NotaPasantia seleccionNotaPasantia;

    private Boolean filter;
    private String keyword;

    private List<IndicadorPasantia> indicadoresPasantia;

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
    
    public void update(){
        this.ejecutar("PF");
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

    public void actualizarIndicadoresPasantia() {
        if (seleccionNotaPasantia != null) {
            indicadoresPasantia = indicadorPasantiaFacade.listaIndicadoresPasantias(seleccionNotaPasantia);
        }
    }

    public void evaluacionEmpresa() throws IOException {
        if (evaluacionPasantiaFacade.evaluacionEmpresa(seleccionNotaPasantia, indicadoresPasantia)) {
            toPasantias();
        }
    }

    public void evaluacionTutor() throws IOException {
        if (evaluacionPasantiaFacade.evaluacionTutor(seleccionNotaPasantia)) {
            toPasantias();
        }
    }

    public void editarPasantia() throws IOException {
        if (notaPasantiaFacade.edit(seleccionNotaPasantia)) {
            toPasantias();
        }
    }

    public void toEditarPasantia() throws IOException {
        this.redireccionarViewId("/pasantias/pasantias/editarPasantia");
    }

    public void toEvaluacionEmpresa() throws IOException {
        actualizarIndicadoresPasantia();

        this.redireccionarViewId("/pasantias/pasantias/evaluacionEmpresa");
    }

    public void toEvaluacionTutor() throws IOException {
        this.redireccionarViewId("/pasantias/pasantias/evaluacionTutor");
    }

    public void toPasantias() throws IOException {
        reinit();

        this.redireccionarViewId("/pasantias/pasantias/pasantias");
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
     * @return the indicadoresPasantia
     */
    public List<IndicadorPasantia> getIndicadoresPasantia() {
        return indicadoresPasantia;
    }

    /**
     * @param indicadoresPasantia the indicadoresPasantia to set
     */
    public void setIndicadoresPasantia(List<IndicadorPasantia> indicadoresPasantia) {
        this.indicadoresPasantia = indicadoresPasantia;
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
