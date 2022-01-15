/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.negocio.PagosFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Generador;

/**
 *
 * @author Tincho
 */
@Named("AnularPagoController")
@SessionScoped
public class AnularPagoController extends AbstractController implements Serializable {

    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    PagosFacade pagosFacade;

    private List<Comprobante> comprobantes;
    private Comprobante seleccionComprobante;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        comprobantes = comprobanteFacade.listaComprobantes();
        seleccionComprobante = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        comprobantes = comprobanteFacade.listaComprobantes();
        seleccionComprobante = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            comprobantes = comprobanteFacade.listaComprobantes();
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        comprobantes = comprobanteFacade.buscar(keyword);
    }

    public void imprimirComprobante() throws IOException {
        if (seleccionComprobante.getInscrito() != null) {
            Estudiante estudiante = seleccionComprobante.getInscrito().getEstudiante();

            String contrasena = Generador.generarContrasena();
            estudiante.setContrasena(Encriptador.encriptar(contrasena));
            estudiante.setContrasenaSinEncriptar(contrasena);

            if (estudianteFacade.edit(estudiante)) {
                this.insertarParametro("id_comprobante", seleccionComprobante.getId_comprobante());
                this.insertarParametro("est", estudiante);

                this.reinit();

                this.toComprobantePago();
            } else {
                this.mensajeDeError("No se puede imprimir el comprobante.");
            }
        } else if (seleccionComprobante.getPostulante() != null) {
            this.insertarParametro("id_comprobante", seleccionComprobante.getId_comprobante());

            this.reinit();

            this.toComprobantePagoPostulante();
        }
    }

    public void anularPago() {
        if (pagosFacade.anularPago(seleccionComprobante)) {
            reinit();
        } else {
            this.mensajeDeError("No se pudo eliminar el pago.");
        }
    }

    public void toAnularPago() throws IOException {
        this.redireccionarViewId("/pagos/anularPago/anularPago");
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/pagos/anularPago/comprobantePago");
    }

    public void toComprobantePagoPostulante() throws IOException {
        this.redireccionarViewId("/pagos/anularPago/comprobantePagoPostulante");
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
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
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
}
