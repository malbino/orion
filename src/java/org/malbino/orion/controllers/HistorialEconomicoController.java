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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Pago;
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

    private Estudiante seleccionEstudiante;
    private Carrera seleccionCarrera;
    private List<Pago> historialEconomico;

    private Pago seleccionPago;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        historialEconomico = new ArrayList();

        seleccionPago = null;
    }

    public void reinit() {
        if (seleccionEstudiante != null && seleccionCarrera != null) {
            historialEconomico = pagoFacade.kardexEconomico(seleccionEstudiante.getId_persona(), seleccionCarrera.getId_carrera());
        }

        seleccionPago = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarrera != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarrera.getRegimen());
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
