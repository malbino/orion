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
import javax.inject.Named;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.PagosFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;

/**
 *
 * @author Tincho
 */
@Named("NuevoPagoController")
@SessionScoped
public class NuevoPagoController extends AbstractController implements Serializable {

    @EJB
    private PagosFacade pagosFacade;
    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    PagoFacade pagoFacade;

    private Comprobante nuevoComprobante;
    private Estudiante seleccionEstudiante;
    private Inscrito seleccionInscrito;
    private List<Pago> pagos;
    private List<Pago> seleccionPagos;
    
    @PostConstruct
    public void init() {
        nuevoComprobante = new Comprobante();
        seleccionEstudiante = null;
        seleccionInscrito = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    public void reinit() {
        nuevoComprobante = new Comprobante();
        seleccionEstudiante = null;
        seleccionInscrito = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = inscritoFacade.listaInscritosPersona(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    public void actualizarPagos() {
        if (seleccionInscrito != null) {
            pagos = pagoFacade.listaPagosAdeudados(seleccionInscrito.getId_inscrito());
            int montoAcumulado = 0;
            for (Pago pago : pagos) {
                montoAcumulado += pago.getMonto();

                pago.setMontoAcumulado(montoAcumulado);
            }
        }
    }

    public void crearPago() throws IOException {
        List<Pago> pagosPagados = pagoFacade.listaPagosPagados(seleccionInscrito.getId_inscrito());
        if (pagosPagados.isEmpty()) {
            nuevoComprobante.setFecha(Fecha.getDate());
            nuevoComprobante.setValido(true);
            nuevoComprobante.setInscrito(seleccionInscrito);

            String contrasena = Generador.generarContrasena();
            seleccionEstudiante.setContrasena(Encriptador.encriptar(contrasena));
            seleccionEstudiante.setContrasenaSinEncriptar(contrasena);

            if (!seleccionPagos.isEmpty()) {
                if (pagosFacade.nuevoPago(nuevoComprobante, seleccionPagos, seleccionEstudiante)) {
                    this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                    this.insertarParametro("est", seleccionEstudiante);

                    this.reinit();

                    this.toComprobantePago();
                }
            } else {
                this.mensajeDeError("Ningun pago seleccionado.");
            }
        } else {
            nuevoComprobante.setFecha(Fecha.getDate());
            nuevoComprobante.setValido(true);
            nuevoComprobante.setInscrito(seleccionInscrito);
            if (!seleccionPagos.isEmpty()) {
                if (pagosFacade.nuevoPago(nuevoComprobante, seleccionPagos)) {
                    this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                    this.insertarParametro("est", null);

                    this.reinit();

                    this.toComprobantePago();
                }
            } else {
                this.mensajeDeError("Ningun pago seleccionado.");
            }
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
     * @return the seleccionInscrito
     */
    public Inscrito getSeleccionInscrito() {
        return seleccionInscrito;
    }

    /**
     * @param seleccionInscrito the seleccionInscrito to set
     */
    public void setSeleccionInscrito(Inscrito seleccionInscrito) {
        this.seleccionInscrito = seleccionInscrito;
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
