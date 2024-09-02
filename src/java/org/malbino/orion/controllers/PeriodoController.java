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
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Periodo;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.InstitutoFacade;
import org.malbino.orion.facades.PeriodoFacade;
import org.malbino.orion.util.Constantes;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("PeriodoController")
@SessionScoped
public class PeriodoController extends AbstractController implements Serializable {

    @EJB
    InstitutoFacade institutoFacade;
    @EJB
    PeriodoFacade periodoFacade;
    @Inject
    LoginController loginController;

    private List<Periodo> periodos;
    private Periodo nuevoPeriodo;
    private Periodo seleccionPeriodo;
    private Instituto instituto;

    private String keyword;

    @PostConstruct
    public void init() {
        periodos = periodoFacade.listaPeriodos();
        nuevoPeriodo = new Periodo();
        seleccionPeriodo = null;
        instituto = institutoFacade.buscarPorId(Constantes.ID_INSTITUTO);

        keyword = null;
    }

    public void reinit() {
        periodos = periodoFacade.listaPeriodos();
        nuevoPeriodo = new Periodo();
        seleccionPeriodo = null;

        keyword = null;
    }

    public void buscar() {
        periodos = periodoFacade.buscar(keyword);
    }

    public void crearPeriodo() throws IOException {
        nuevoPeriodo.setInstituto(instituto);
        if (periodoFacade.buscar(nuevoPeriodo.getDia(), nuevoPeriodo.getInicio(), nuevoPeriodo.getFin()) == null) {
            if (nuevoPeriodo.getFin().compareTo(nuevoPeriodo.getInicio()) > 0) {
                if (periodoFacade.create(nuevoPeriodo)) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.PERIODO, nuevoPeriodo.getId_periodo(), "Creación periodo", loginController.getUsr().toString()));

                    this.toPeriodo();
                }
            } else {
                this.mensajeDeError("Fin debe ser mayor que Inicio.");
            }
        } else {
            this.mensajeDeError("Periodo repetido.");
        }
    }

    public void editarPeriodo() throws IOException {
        if (periodoFacade.buscar(seleccionPeriodo.getDia(), seleccionPeriodo.getInicio(), seleccionPeriodo.getFin(), seleccionPeriodo.getId_periodo()) == null) {
            if (seleccionPeriodo.getFin().compareTo(seleccionPeriodo.getInicio()) > 0) {
                if (periodoFacade.edit(seleccionPeriodo)) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.PERIODO, seleccionPeriodo.getId_periodo(), "Actualización periodo", loginController.getUsr().toString()));

                    this.toPeriodo();
                }
            } else {
                this.mensajeDeError("Fin debe ser mayor que Inicio.");
            }
        } else {
            this.mensajeDeError("Periodo repetido.");
        }
    }

    public void toNuevoPeriodo() throws IOException {
        this.redireccionarViewId("/horarios/periodo/nuevoPeriodo");
    }

    public void toEditarPeriodo() throws IOException {
        this.redireccionarViewId("/horarios/periodo/editarPeriodo");
    }

    public void toPeriodo() throws IOException {
        reinit();

        this.redireccionarViewId("/horarios/periodo/periodos");
    }

    /**
     * @return the periodos
     */
    public List<Periodo> getPeriodos() {
        return periodos;
    }

    /**
     * @param periodos the periodos to set
     */
    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    /**
     * @return the nuevoPeriodo
     */
    public Periodo getNuevoPeriodo() {
        return nuevoPeriodo;
    }

    /**
     * @param nuevoPeriodo the nuevoPeriodo to set
     */
    public void setNuevoPeriodo(Periodo nuevoPeriodo) {
        this.nuevoPeriodo = nuevoPeriodo;
    }

    /**
     * @return the seleccionPeriodo
     */
    public Periodo getSeleccionPeriodo() {
        return seleccionPeriodo;
    }

    /**
     * @param seleccionPeriodo the seleccionPeriodo to set
     */
    public void setSeleccionPeriodo(Periodo seleccionPeriodo) {
        this.seleccionPeriodo = seleccionPeriodo;
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
