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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.facades.PostulanteFacade;

/**
 *
 * @author Tincho
 */
@Named("AdmisionesController")
@SessionScoped
public class AdmisionesController extends AbstractController implements Serializable {

    @EJB
    PostulanteFacade postulanteFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    private List<Postulante> postulantes;
    private Postulante seleccionPostulante;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;

        postulantes = new ArrayList<>();
        seleccionPostulante = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null) {
            postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica());
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionPostulante = null;

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

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            if (seleccionGestionAcademica != null && seleccionCarrera == null) {
                postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica());
            } else if (seleccionGestionAcademica != null && seleccionCarrera != null) {
                postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
            }
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null) {
            postulantes = postulanteFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            postulantes = postulanteFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void imprimirFormularioPostulante() throws IOException {
        if (seleccionPostulante != null) {
            this.insertarParametro("id_postulante", seleccionPostulante.getId_postulante());

            toFormularioPostulante();
        }
    }

    public void toFormularioPostulante() throws IOException {
        this.redireccionarViewId("/admisiones/formularioPostulante");
    }

    public void toPostulantes() throws IOException {
        this.redireccionarViewId("/admisiones/postulantes");
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
     * @return the postulantes
     */
    public List<Postulante> getPostulantes() {
        return postulantes;
    }

    /**
     * @param postulantes the postulantes to set
     */
    public void setPostulantes(List<Postulante> postulantes) {
        this.postulantes = postulantes;
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
