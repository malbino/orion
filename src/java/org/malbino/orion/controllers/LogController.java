/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;

/**
 *
 * @author Tincho
 */
@Named("LogController")
@SessionScoped
public class LogController extends AbstractController implements Serializable {

    private List<Log> logs;

    private Date desde;
    private Date hasta;
    private EventoLog eventoLog;
    private EntidadLog entidadLog;
    private String descripcion;
    private String usuario;

    @PostConstruct
    public void init() {
        logs = new ArrayList<>();

        desde = null;
        hasta = null;
        eventoLog = null;
        entidadLog = null;
        descripcion = null;
        usuario = null;
    }

    public void reinit() {
        logs = new ArrayList<>();

        desde = null;
        hasta = null;
        eventoLog = null;
        entidadLog = null;
        descripcion = null;
        usuario = null;
    }

    public void buscar() {
        logs = logFacade.buscarLogs(desde, hasta, eventoLog, entidadLog, descripcion, usuario);
    }

    /**
     * @return the logs
     */
    public List<Log> getLogs() {
        return logs;
    }

    /**
     * @param logs the logs to set
     */
    public void setLogs(List<Log> logs) {
        this.logs = logs;
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
     * @return the eventoLog
     */
    public EventoLog getEventoLog() {
        return eventoLog;
    }

    /**
     * @param eventoLog the eventoLog to set
     */
    public void setEventoLog(EventoLog eventoLog) {
        this.eventoLog = eventoLog;
    }

    /**
     * @return the entidadLog
     */
    public EntidadLog getEntidadLog() {
        return entidadLog;
    }

    /**
     * @param entidadLog the entidadLog to set
     */
    public void setEntidadLog(EntidadLog entidadLog) {
        this.entidadLog = entidadLog;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
