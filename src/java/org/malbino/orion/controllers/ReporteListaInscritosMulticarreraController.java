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
@Named("ReporteListaInscritosMulticarreraController")
@SessionScoped
public class ReporteListaInscritosMulticarreraController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @Inject
    LoginController loginController;
    
    private GestionAcademica seleccionGestionAcademica;

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
            
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte lista inscritos multicarrera", loginController.getUsr().toString()));
        }
    }

    public void toReporteListaInscritosMulticarrera() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/inscripciones/listaInscritosMulticarrera/reporteListaInscritosMulticarrera");
    }

    public void toListaInscritosMulticarrera() throws IOException {
        this.redireccionarViewId("/reportes/inscripciones/listaInscritosMulticarrera/listaInscritosMulticarrera");
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
