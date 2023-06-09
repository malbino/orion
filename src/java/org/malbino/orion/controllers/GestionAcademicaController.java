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
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("GestionAcademicaController")
@SessionScoped
public class GestionAcademicaController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;

    private List<GestionAcademica> gestionesAcademicas;
    private GestionAcademica nuevaGestionAcademica;
    private GestionAcademica seleccionGestionAcademica;
    private Regimen seleccionRegimen;

    private String keyword;

    @PostConstruct
    public void init() {
        gestionesAcademicas = new ArrayList();
        nuevaGestionAcademica = new GestionAcademica();
        seleccionGestionAcademica = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionRegimen != null) {
            gestionesAcademicas = gestionAcademicaFacade.listaGestionAcademica(seleccionRegimen);
        }
        nuevaGestionAcademica = new GestionAcademica();
        seleccionGestionAcademica = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionRegimen != null) {
            gestionesAcademicas = gestionAcademicaFacade.buscar(keyword, seleccionRegimen);
        }
    }

    public void crearGestionAcademica() throws IOException {
        nuevaGestionAcademica.setRegimen(seleccionRegimen);
        if (gestionAcademicaFacade.buscarPorCodigoRegimen(nuevaGestionAcademica.getGestion(), nuevaGestionAcademica.getPeriodo(), seleccionRegimen) == null) {
            if (gestionAcademicaFacade.create(nuevaGestionAcademica)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.GESTION_ACADEMICA, nuevaGestionAcademica.getId_gestionacademica(), "Creación gestión académica", loginController.getUsr().toString()));

                this.toGestionesAcademicas();
            }
        } else {
            this.mensajeDeError("Gestión académica repetida.");
        }
    }

    public void editarGestionAcademica() throws IOException {
        if (gestionAcademicaFacade.buscarPorCodigoRegimen(seleccionGestionAcademica.getGestion(), seleccionGestionAcademica.getPeriodo(), seleccionRegimen, seleccionGestionAcademica.getId_gestionacademica()) == null) {
            if (gestionAcademicaFacade.edit(seleccionGestionAcademica)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.GESTION_ACADEMICA, seleccionGestionAcademica.getId_gestionacademica(), "Actualización gestión académica", loginController.getUsr().toString()));

                this.toGestionesAcademicas();
            }
        } else {
            this.mensajeDeError("Gestión académica repetida.");
        }
    }

    public void toNuevaGestionAcademica() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/gestionAcademica/nuevaGestionAcademica");
    }

    public void toEditarGestionAcademica() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/gestionAcademica/editarGestionAcademica");
    }

    public void toGestionesAcademicas() throws IOException {
        reinit();

        this.redireccionarViewId("/gestionesAcademicas/gestionAcademica/gestionesAcademicas");
    }

    /**
     * @return the gestionesAcademicas
     */
    public List<GestionAcademica> getGestionesAcademicas() {
        return gestionesAcademicas;
    }

    /**
     * @param gestionesAcademicas the gestionesAcademicas to set
     */
    public void setGestionesAcademicas(List<GestionAcademica> gestionesAcademicas) {
        this.gestionesAcademicas = gestionesAcademicas;
    }

    /**
     * @return the nuevaGestionAcademica
     */
    public GestionAcademica getNuevaGestionAcademica() {
        return nuevaGestionAcademica;
    }

    /**
     * @param nuevaGestionAcademica the nuevaGestionAcademica to set
     */
    public void setNuevaGestionAcademica(GestionAcademica nuevaGestionAcademica) {
        this.nuevaGestionAcademica = nuevaGestionAcademica;
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
     * @return the seleccionRegimen
     */
    public Regimen getSeleccionRegimen() {
        return seleccionRegimen;
    }

    /**
     * @param seleccionRegimen the seleccionRegimen to set
     */
    public void setSeleccionRegimen(Regimen seleccionRegimen) {
        this.seleccionRegimen = seleccionRegimen;
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
