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
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteMallaCurricularController")
@SessionScoped
public class ReporteMallaCurricularController extends AbstractController implements Serializable {

    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;

    private Carrera seleccionCarrera;
    private Mencion seleccionMencion;

    @PostConstruct
    public void init() {
        seleccionCarrera = null;
        seleccionMencion = null;
    }

    public void reinit() {
        seleccionCarrera = null;
        seleccionMencion = null;
    }

    public List<Mencion> listaMenciones() {
        List<Mencion> l = new ArrayList<>();
        if (seleccionCarrera != null) {
            l = mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionCarrera != null) {
            this.insertarParametro("carrera", seleccionCarrera);
            this.insertarParametro("mencion", seleccionMencion);

            toMallaCurricular();
            
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte malla curricular", loginController.getUsr().toString()));
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

    /**
     * @return the seleccionMencion
     */
    public Mencion getSeleccionMencion() {
        return seleccionMencion;
    }

    /**
     * @param seleccionMencion the seleccionMencion to set
     */
    public void setSeleccionMencion(Mencion seleccionMencion) {
        this.seleccionMencion = seleccionMencion;
    }

}
