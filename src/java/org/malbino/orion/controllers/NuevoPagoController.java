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
import org.malbino.orion.entities.Detalle;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.PagosFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("NuevoPagoController")
@SessionScoped
public class NuevoPagoController extends AbstractController implements Serializable {

    @EJB
    PagosFacade pagosFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @EJB
    PagoFacade pagoFacade;
    @Inject
    LoginController loginController;

    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    private Comprobante nuevoComprobante;
    private List<Detalle> detalles;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;

        nuevoComprobante = new Comprobante();
        detalles = new ArrayList<>();
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;

        nuevoComprobante = new Comprobante();
        detalles = new ArrayList<>();
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
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarreraEstudiante.getCarrera().getRegimen(), true);
        }
        return l;
    }

    public void actualizarDetalles() {
        if (seleccionCarreraEstudiante != null) {
            Detalle detalle = new Detalle();
            detalle.setCodigo("");
            detalle.setNombre("");
            detalle.setMonto(0);

            detalles.add(detalle);
        }
    }

    public void crearPago() throws IOException {
        nuevoComprobante.setFecha(Fecha.getDate());
        nuevoComprobante.setValido(true);
        nuevoComprobante.setEstudiante(seleccionEstudiante);
        nuevoComprobante.setCarrera(seleccionCarreraEstudiante.getCarrera());
        nuevoComprobante.setGestionAcademica(seleccionGestionAcademica);
        nuevoComprobante.setUsuario(loginController.getUsr());
        if (!detalles.isEmpty()) {
            if (pagosFacade.nuevoComprobante(nuevoComprobante, detalles)) {
                this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                this.insertarParametro("est", null);

                this.reinit();

                this.toComprobantePago();
            }
        } else {
            this.mensajeDeError("Ningun pago seleccionado.");
        }
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/pagos/nuevoPago/comprobantePago");
    }

    public void toNuevoPago() throws IOException {
        this.redireccionarViewId("/pagos/nuevoPago/nuevoPago");
    }

    /**
     * @return the nuevoComprobante
     */
    public Comprobante getNuevoComprobante() {
        return nuevoComprobante;
    }

    /**
     * @param nuevoComprobante the nuevoComprobante to set
     */
    public void setNuevoComprobante(Comprobante nuevoComprobante) {
        this.nuevoComprobante = nuevoComprobante;
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
     * @return the detalles
     */
    public List<Detalle> getDetalles() {
        return detalles;
    }

    /**
     * @param detalles the detalles to set
     */
    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
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
