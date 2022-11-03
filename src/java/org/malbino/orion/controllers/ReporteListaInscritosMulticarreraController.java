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
import org.malbino.orion.facades.GrupoFacade;

/**
 *
 * @author Tincho
 */
@Named("ReporteListaInscritosMulticarreraController")
@SessionScoped
public class ReporteListaInscritosMulticarreraController extends AbstractController implements Serializable {

    private GestionAcademica seleccionGestionAcademica;

    @EJB
    GrupoFacade grupoFacade;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionGestionAcademica != null) {
            this.insertarParametro("id_gestionacademica", seleccionGestionAcademica.getId_gestionacademica());

            toListaInscritosMulticarrera();
        }
    }

    public void toReporteListaInscritosMulticarrera() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/listaInscritosMulticarrera/reporteListaInscritosMulticarrera");
    }

    public void toListaInscritosMulticarrera() throws IOException {
        this.redireccionarViewId("/reportes/listaInscritosMulticarrera/listaInscritosMulticarrera");
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
}
