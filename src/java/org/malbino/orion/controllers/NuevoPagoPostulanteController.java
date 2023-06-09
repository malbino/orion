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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
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
    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private List<Postulante> postulantes;
    private Postulante seleccionPostulante;
    private List<Pago> pagos;
    private List<Pago> seleccionPagos;

    @PostConstruct
    public void init() {
        nuevoComprobante = new Comprobante();
        seleccionGestionAcademica = null;
        postulantes = new ArrayList<>();
        seleccionPostulante = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    public void reinit() {
        nuevoComprobante = new Comprobante();
        seleccionGestionAcademica = null;
        postulantes = new ArrayList<>();
        seleccionPostulante = null;
        pagos = new ArrayList();
        seleccionPagos = new ArrayList();
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList<>();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void actualizarPostulantes() {
        seleccionPostulante = null;

        postulantes = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
    }

    @Override
    public List<Postulante> completarPostulante(String consulta) {
        List<Postulante> postulantesFiltrados = new ArrayList();
        for (Postulante p : postulantes) {
            if (p.toString().toLowerCase().contains(consulta.toLowerCase())) {
                postulantesFiltrados.add(p);
            }
        }
        return postulantesFiltrados;
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
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.COMPROBANTE, nuevoComprobante.getId_comprobante(), "Creación comprobante por nuevo pago postulante", loginController.getUsr().toString()));

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

    /**
     * @return the postulantes
     */
    public List<Postulante> getPostulantes() {
        return postulantes;
    }

    /**
     * @param postulantes the postulantes to set
     */
    public void setPostulantes(List<Postulante> postulantes) {
        this.postulantes = postulantes;
    }

}
