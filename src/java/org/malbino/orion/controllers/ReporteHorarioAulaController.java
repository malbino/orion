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
import org.malbino.orion.entities.Aula;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.AulaFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteHorarioAulaController")
@SessionScoped
public class ReporteHorarioAulaController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    AulaFacade aulaFacade;

    private Campus seleccionCampus;
    private Aula seleccionAula;

    @PostConstruct
    public void init() {
        seleccionCampus = null;
        seleccionAula = null;
    }

    public void reinit() {
        seleccionCampus = null;
        seleccionAula = null;
    }

    public List<Aula> listaAulas() {
        List<Aula> l = new ArrayList();
        if (seleccionCampus != null) {
            l = aulaFacade.listaAulas(seleccionCampus.getId_campus());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionCampus != null && seleccionAula != null) {
            this.insertarParametro("id_campus", seleccionCampus.getId_campus());
            this.insertarParametro("id_aula", seleccionAula.getId_aula());

            toHorarioAula();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte horario por aula", loginController.getUsr().toString()));
        }
    }

    public void toReporteHorarioAula() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/horarios/horarioAula/reporteHorarioAula");
    }

    public void toHorarioAula() throws IOException {
        this.redireccionarViewId("/reportes/horarios/horarioAula/horarioAula");
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
     * @return the seleccionAula
     */
    public Aula getSeleccionAula() {
        return seleccionAula;
    }

    /**
     * @param seleccionAula the seleccionAula to set
     */
    public void setSeleccionAula(Aula seleccionAula) {
        this.seleccionAula = seleccionAula;
    }

}
