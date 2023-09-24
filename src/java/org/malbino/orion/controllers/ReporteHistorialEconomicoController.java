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
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteHistorialEconomicoController")
@SessionScoped
public class ReporteHistorialEconomicoController extends AbstractController implements Serializable {

    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @Inject
    LoginController loginController;
    
    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionEstudiante != null && seleccionCarreraEstudiante != null) {
            this.insertarParametro("id_persona", seleccionEstudiante.getId_persona());
            this.insertarParametro("id_carrera", seleccionCarreraEstudiante.getCarrera().getId_carrera());

            toHistorialEconomico();
            
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte historial económico", loginController.getUsr().toString()));
        }
    }

    public void toReporteHistorialEconomico() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/pagos/historialEconomico/reporteHistorialEconomico");
    }

    public void toHistorialEconomico() throws IOException {
        this.redireccionarViewId("/reportes/pagos/historialEconomico/historialEconomico");
    }

    /**
     * @return the seleccionEstudiante
     */
    public Estudiante getSeleccionEstudiante() {
        return seleccionEstudiante;
    }

    /**
     * @param seleccionEstudiante the seleccionEstudiante to set
     */
    public void setSeleccionEstudiante(Estudiante seleccionEstudiante) {
        this.seleccionEstudiante = seleccionEstudiante;
    }

    /**
     * @return the seleccionCarreraEstudiante
     */
    public CarreraEstudiante getSeleccionCarreraEstudiante() {
        return seleccionCarreraEstudiante;
    }

    /**
     * @param seleccionCarreraEstudiante the seleccionCarreraEstudiante to set
     */
    public void setSeleccionCarreraEstudiante(CarreraEstudiante seleccionCarreraEstudiante) {
        this.seleccionCarreraEstudiante = seleccionCarreraEstudiante;
    }
}
