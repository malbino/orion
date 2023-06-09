/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteLibroInscripcionesController")
@SessionScoped
public class ReporteLibroInscripcionesController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;

    private Integer seleccionGestion;
    private Periodo seleccionPeriodo;

    @PostConstruct
    public void init() {
        seleccionGestion = null;
        seleccionPeriodo = null;
    }

    public void reinit() {
        seleccionGestion = null;
        seleccionPeriodo = null;
    }

    public List<Integer> listaGestiones() {
        return gestionAcademicaFacade.listaGestiones();
    }

    public void generarReporte() throws IOException {
        if (seleccionGestion != null && seleccionPeriodo != null) {
            this.insertarParametro("gestion", seleccionGestion);
            this.insertarParametro("periodo", seleccionPeriodo);

            toLibroInscripciones();
            
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte libro de inscripciones", loginController.getUsr().toString()));
        }
    }

    public void toReporteLibroInscripciones() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/inscripciones/libroInscripciones/reporteLibroInscripciones");
    }

    public void toLibroInscripciones() throws IOException {
        this.redireccionarViewId("/reportes/inscripciones/libroInscripciones/libroInscripciones");
    }

    /**
     * @return the seleccionGestion
     */
    public Integer getSeleccionGestion() {
        return seleccionGestion;
    }

    /**
     * @param seleccionGestion the seleccionGestion to set
     */
    public void setSeleccionGestion(Integer seleccionGestion) {
        this.seleccionGestion = seleccionGestion;
    }

    /**
     * @return the seleccionPeriodo
     */
    public Periodo getSeleccionPeriodo() {
        return seleccionPeriodo;
    }

    /**
     * @param seleccionPeriodo the seleccionPeriodo to set
     */
    public void setSeleccionPeriodo(Periodo seleccionPeriodo) {
        this.seleccionPeriodo = seleccionPeriodo;
    }
}
