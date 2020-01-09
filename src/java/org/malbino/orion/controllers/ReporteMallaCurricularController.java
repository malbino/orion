/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;

/**
 *
 * @author Tincho
 */
@Named("ReporteMallaCurricularController")
@SessionScoped
public class ReporteMallaCurricularController extends AbstractController implements Serializable {

    private Carrera seleccionCarrera;

    @PostConstruct
    public void init() {
        seleccionCarrera = null;
    }

    public void reinit() {
        seleccionCarrera = null;
    }

    public void generarReporte() throws IOException {
        if (seleccionCarrera != null) {
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());

            toMallaCurricular();
        }
    }

    public void toReporteMallaCurricular() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/mallaCurricular/reporteMallaCurricular");
    }

    public void toMallaCurricular() throws IOException {
        this.redireccionarViewId("/reportes/mallaCurricular/mallaCurricular");
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
