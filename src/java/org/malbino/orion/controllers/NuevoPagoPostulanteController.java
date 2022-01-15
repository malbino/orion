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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.PagosFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("NuevoPagoPostulanteController")
@SessionScoped
public class NuevoPagoPostulanteController extends AbstractController implements Serializable {

    @EJB
    private PagosFacade pagosFacade;
    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    PagoFacade pagoFacade;
    @Inject
    LoginController loginController;

    private Comprobante nuevoComprobante;
    private Postulante seleccionPostulante;
    private List<Pago> pagos;
    private List<Pago> seleccionPagos;

    @PostConstruct
    public void init() {
        nuevoComprobante = new Comprobante();
        seleccionPostulante = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    public void reinit() {
        nuevoComprobante = new Comprobante();
        seleccionPostulante = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    public void actualizarPagos() {
        if (seleccionPostulante != null) {
            nuevoComprobante.setDeposito(seleccionPostulante.getDeposito());

            pagos = pagoFacade.listaPagosAdeudadosPostulante(seleccionPostulante.getId_postulante());
            int montoAcumulado = 0;
            for (Pago pago : pagos) {
                montoAcumulado += pago.getMonto();

                pago.setMontoAcumulado(montoAcumulado);
            }
        }
    }

    public void crearPago() throws IOException {
        nuevoComprobante.setFecha(Fecha.getDate());
        nuevoComprobante.setValido(true);
        nuevoComprobante.setPostulante(seleccionPostulante);
        nuevoComprobante.setUsuario(loginController.getUsr());
        if (!seleccionPagos.isEmpty()) {
            if (pagosFacade.nuevoPago(nuevoComprobante, seleccionPagos)) {
                this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());

                this.reinit();

                this.toComprobantePagoPostulante();
            }
        } else {
            this.mensajeDeError("Ningun pago seleccionado.");
        }
    }

    public void toComprobantePagoPostulante() throws IOException {
        this.redireccionarViewId("/pagos/nuevoPagoPostulante/comprobantePagoPostulante");
    }

    public void toNuevoPagoPostulante() throws IOException {
        this.redireccionarViewId("/pagos/nuevoPagoPostulante/nuevoPagoPostulante");
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
     * @return the seleccionPostulante
     */
    public Postulante getSeleccionPostulante() {
        return seleccionPostulante;
    }

    /**
     * @param seleccionPostulante the seleccionPostulante to set
     */
    public void setSeleccionPostulante(Postulante seleccionPostulante) {
        this.seleccionPostulante = seleccionPostulante;
    }

    /**
     * @return the pagos
     */
    public List<Pago> getPagos() {
        return pagos;
    }

    /**
     * @param pagos the pagos to set
     */
    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    /**
     * @return the seleccionPagos
     */
    public List<Pago> getSeleccionPagos() {
        return seleccionPagos;
    }

    /**
     * @param seleccionPagos the seleccionPagos to set
     */
    public void setSeleccionPagos(List<Pago> seleccionPagos) {
        this.seleccionPagos = seleccionPagos;
    }

}
