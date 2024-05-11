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
import org.malbino.orion.entities.Egresado;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.EgresadoFacade;
import org.malbino.orion.facades.negocio.TitulacionFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("EgresadosController")
@SessionScoped
public class EgresadosController extends AbstractController implements Serializable {

    @EJB
    EgresadoFacade egresadoFacade;
    @EJB
    TitulacionFacade titulacionFacade;

    @Inject
    LoginController loginController;

    private Carrera seleccionCarrera;

    private List<Egresado> egresados;
    private Egresado seleccionEgresado;

    private String keyword;

    @PostConstruct
    public void init() {
        seleccionCarrera = null;

        egresados = new ArrayList<>();
        seleccionEgresado = null;

        keyword = null;
    }

    public void reinit() {
        seleccionCarrera = null;

        egresados = new ArrayList<>();
        seleccionEgresado = null;

        keyword = null;
    }

    public void buscarSinKeyword() {
        keyword = "";

        if (seleccionCarrera == null) {
            egresados = egresadoFacade.buscar(keyword);
        } else {
            egresados = egresadoFacade.buscar(seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void buscarConKeyword() {
        if (seleccionCarrera == null) {
            egresados = egresadoFacade.buscar(keyword);
        } else {
            egresados = egresadoFacade.buscar(seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void actualizarEgresados() {
        if (seleccionCarrera != null) {
            titulacionFacade.actualizarEgresados(seleccionCarrera, null);
        }
    }

    public void imprimirCertificadoEgreso() throws IOException {
        if (seleccionEgresado != null) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.EGRESADO, seleccionEgresado.getId_egresado(), "Impresión certificado de egreso", loginController.getUsr().toString()));

            this.insertarParametro("egresado", seleccionEgresado);
            this.insertarParametro("fecha", Fecha.getDate());
            toCertificadoEgreso();
        }
    }

    public void toCertificadoEgreso() throws IOException {
        this.redireccionarViewId("/titulacion/egresados/certificadoEgreso");
    }

    public void toEgresados() throws IOException {
        this.redireccionarViewId("/titulacion/egresados/egresados");
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
     * @return the egresados
     */
    public List<Egresado> getEgresados() {
        return egresados;
    }

    /**
     * @param egresados the egresados to set
     */
    public void setEgresados(List<Egresado> egresados) {
        this.egresados = egresados;
    }

    /**
     * @return the seleccionEgresado
     */
    public Egresado getSeleccionEgresado() {
        return seleccionEgresado;
    }

    /**
     * @param seleccionEgresado the seleccionEgresado to set
     */
    public void setSeleccionEgresado(Egresado seleccionEgresado) {
        this.seleccionEgresado = seleccionEgresado;
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
