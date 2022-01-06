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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;

/**
 *
 * @author Tincho
 */
@Named("ReporteNotasFaltantesController")
@SessionScoped
public class ReporteNotasFaltantesController extends AbstractController implements Serializable {

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
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
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            this.insertarParametro("id_gestionacademica", seleccionGestionAcademica.getId_gestionacademica());
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());

            toNotasFaltantes();
        }
    }

    public void toReporteNotasFaltantes() throws IOException {
        reinit();
        
        this.redireccionarViewId("/reportes/notasFaltantes/reporteNotasFaltantes");
    }

    public void toNotasFaltantes() throws IOException {
        this.redireccionarViewId("/reportes/notasFaltantes/notasFaltantes");
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

}
