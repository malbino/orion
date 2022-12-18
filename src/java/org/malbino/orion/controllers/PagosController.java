/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.negocio.PagosFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Generador;

/**
 *
 * @author Tincho
 */
@Named("PagosController")
@SessionScoped
public class PagosController extends AbstractController implements Serializable {

    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    PagosFacade pagosFacade;

    private Date desde;
    private Date hasta;
    private Usuario seleccionUsuario;

    private List<Comprobante> comprobantes;
    private Comprobante seleccionComprobante;

    private String keyword;

    @PostConstruct
    public void init() {
        comprobantes = new ArrayList<>();
        seleccionComprobante = null;

        keyword = null;
    }

    public void reinit() {
        if (desde != null && hasta != null && seleccionUsuario == null) {
            comprobantes = comprobanteFacade.listaComprobantes(desde, hasta);
        } else if (desde != null && hasta != null && seleccionUsuario != null) {
            comprobantes = comprobanteFacade.listaComprobantes(desde, hasta, seleccionUsuario.getId_persona());
        } else {
            comprobantes = comprobanteFacade.listaComprobantes();
        }
        seleccionComprobante = null;

        keyword = null;
    }

    public void buscar() {
        if (desde != null && hasta != null && seleccionUsuario == null) {
            comprobantes = comprobanteFacade.buscar(desde, hasta, keyword);
        } else if (desde != null && hasta != null && seleccionUsuario != null) {
            comprobantes = comprobanteFacade.buscar(desde, hasta, seleccionUsuario.getId_persona(), keyword);
        } else {
            comprobantes = comprobanteFacade.buscar(keyword);
        }
    }

    public void imprimirComprobante() throws IOException {
        if (seleccionComprobante.getInscrito() != null && seleccionComprobante.getPostulante() == null
                && seleccionComprobante.getEstudiante() == null && seleccionComprobante.getCarrera() == null
                && seleccionComprobante.getGestionAcademica() == null) {
            Estudiante estudiante = seleccionComprobante.getInscrito().getEstudiante();

            String contrasena = Generador.generarContrasena();
            estudiante.setContrasena(Encriptador.encriptar(contrasena));
            estudiante.setContrasenaSinEncriptar(contrasena);

            if (estudianteFacade.edit(estudiante)) {
                this.insertarParametro("id_comprobante", seleccionComprobante.getId_comprobante());
                this.insertarParametro("est", estudiante);

                this.reinit();

                this.toComprobantePagoEstudiante();
            } else {
                this.mensajeDeError("No se puede imprimir el comprobante.");
            }
        } else if (seleccionComprobante.getInscrito() == null && seleccionComprobante.getPostulante() != null
                && seleccionComprobante.getEstudiante() == null && seleccionComprobante.getCarrera() == null
                && seleccionComprobante.getGestionAcademica() == null) {
            this.insertarParametro("id_comprobante", seleccionComprobante.getId_comprobante());

            this.reinit();

            this.toComprobantePagoPostulante();
        } else if (seleccionComprobante.getInscrito() == null && seleccionComprobante.getPostulante() == null
                && seleccionComprobante.getEstudiante() != null && seleccionComprobante.getCarrera() != null
                && seleccionComprobante.getGestionAcademica() != null) {
            this.insertarParametro("id_comprobante", seleccionComprobante.getId_comprobante());

            this.reinit();

            this.toComprobantePago();
        }
    }

    public void anularPago() {
        if (pagosFacade.anularPago(seleccionComprobante)) {
            reinit();
        } else {
            this.mensajeDeError("No se pudo eliminar el pago.");
        }
    }

    public void toPagos() throws IOException {
        this.redireccionarViewId("/pagos/pagos/pagos");
    }

    public void toComprobantePagoEstudiante() throws IOException {
        this.redireccionarViewId("/pagos/pagos/comprobantePagoEstudiante");
    }

    public void toComprobantePagoPostulante() throws IOException {
        this.redireccionarViewId("/pagos/pagos/comprobantePagoPostulante");
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/pagos/pagos/comprobantePago");
    }

    /**
     * @return the comprobantes
     */
    public List<Comprobante> getComprobantes() {
        return comprobantes;
    }

    /**
     * @param comprobantes the comprobantes to set
     */
    public void setComprobantes(List<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }

    /**
     * @return the seleccionComprobante
     */
    public Comprobante getSeleccionComprobante() {
        return seleccionComprobante;
    }

    /**
     * @param seleccionComprobante the seleccionComprobante to set
     */
    public void setSeleccionComprobante(Comprobante seleccionComprobante) {
        this.seleccionComprobante = seleccionComprobante;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the desde
     */
    public Date getDesde() {
        return desde;
    }

    /**
     * @param desde the desde to set
     */
    public void setDesde(Date desde) {
        this.desde = desde;
    }

    /**
     * @return the hasta
     */
    public Date getHasta() {
        return hasta;
    }

    /**
     * @param hasta the hasta to set
     */
    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    /**
     * @return the seleccionUsuario
     */
    public Usuario getSeleccionUsuario() {
        return seleccionUsuario;
    }

    /**
     * @param seleccionUsuario the seleccionUsuario to set
     */
    public void setSeleccionUsuario(Usuario seleccionUsuario) {
        this.seleccionUsuario = seleccionUsuario;
    }
}
