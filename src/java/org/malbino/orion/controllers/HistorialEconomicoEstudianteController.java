/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("HistorialEconomicoEstudianteController")
@SessionScoped
public class HistorialEconomicoEstudianteController extends AbstractController implements Serializable {

    @EJB
    PagoFacade pagoFacade;
    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @Inject
    LoginController loginController;

    private CarreraEstudiante seleccionCarreraEstudiante;
    private List<Pago> historialEconomico;

    @PostConstruct
    public void init() {
        historialEconomico = new ArrayList();
    }

    public void reinit() {
        if (seleccionCarreraEstudiante != null) {
            Estudiante estudiante = estudianteFacade.find(loginController.getUsr().getId_persona());
            if (estudiante != null) {
                historialEconomico = pagoFacade.kardexEconomico(estudiante.getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera());

                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.ESTUDIANTE, estudiante.getId_persona(), "Visualización Historial Económico", loginController.getUsr().toString()));
            }
        }
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public Comprobante comprobante(Pago pago) {
        return comprobanteFacade.buscarComprobanteValido(pago.getId_pago());
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

    /**
     * @return the historialEconomico
     */
    public List<Pago> getHistorialEconomico() {
        return historialEconomico;
    }

    /**
     * @param historialEconomico the historialEconomico to set
     */
    public void setHistorialEconomico(List<Pago> historialEconomico) {
        this.historialEconomico = historialEconomico;
    }

}
