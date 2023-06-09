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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteListaPostulantesController")
@SessionScoped
public class ReporteListaPostulantesController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @Inject
    LoginController loginController;

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

            toListaInscritosAdmision();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte lista postulantes", loginController.getUsr().toString()));
        }
    }

    public void toReporteListaInscritosAdmision() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/inscripciones/listaPostulantes/reporteListaPostulantes");
    }

    public void toListaInscritosAdmision() throws IOException {
        this.redireccionarViewId("/reportes/inscripciones/listaPostulantes/listaPostulantes");
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
