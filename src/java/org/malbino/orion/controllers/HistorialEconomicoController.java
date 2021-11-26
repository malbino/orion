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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.FileEstudianteFacade;

/**
 *
 * @author Tincho
 */
@Named("HistorialEconomicoController")
@SessionScoped
public class HistorialEconomicoController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    PagoFacade pagoFacade;
    @EJB
    FileEstudianteFacade fileEstudianteFacade;
    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;
    private List<Pago> historialEconomico;

    private Pago seleccionPago;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        historialEconomico = new ArrayList();

        seleccionPago = null;
    }

    public void reinit() {
        if (seleccionEstudiante != null && seleccionCarreraEstudiante != null) {
            historialEconomico = pagoFacade.kardexEconomico(seleccionEstudiante.getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera());
        }

        seleccionPago = null;
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarreraEstudiante != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarreraEstudiante.getCarrera().getRegimen());
        }
        return l;
    }

    public Comprobante comprobante(Pago pago) {
        return comprobanteFacade.buscarComprobanteValido(pago.getId_pago());
    }

    public void editarPago() throws IOException {
        if (fileEstudianteFacade.editarPago(seleccionPago)) {
            toHistorialEconomico();
        }
    }

    public void toHistorialEconomico() throws IOException {
        reinit();

        this.redireccionarViewId("/fileEstudiante/historialEconomico/historialEconomico");
    }

    public void toEditarPago() throws IOException {
        this.redireccionarViewId("/fileEstudiante/historialEconomico/editarPago");
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

    /**
     * @return the seleccionPago
     */
    public Pago getSeleccionPago() {
        return seleccionPago;
    }

    /**
     * @param seleccionPago the seleccionPago to set
     */
    public void setSeleccionPago(Pago seleccionPago) {
        this.seleccionPago = seleccionPago;
    }

}
