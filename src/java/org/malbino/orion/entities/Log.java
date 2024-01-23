/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "log")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_log;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private EventoLog eventoLog;
    private EntidadLog entidadLog;
    private Integer idEntidad;
    private String descripcion;
    private String usuario;

    public Log() {
    }

    public Log(Date fecha, EventoLog eventoLog, String descripcion, String usuario) {
        this.fecha = fecha;
        this.eventoLog = eventoLog;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public Log(Date fecha, EventoLog eventoLog, EntidadLog entidadLog, Integer idEntidad, String descripcion, String usuario) {
        this.fecha = fecha;
        this.eventoLog = eventoLog;
        this.entidadLog = entidadLog;
        this.idEntidad = idEntidad;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public Log(Date fecha, EventoLog eventoLog, EntidadLog entidadLog, Integer idEntidad, String descripcion) {
        this.fecha = fecha;
        this.eventoLog = eventoLog;
        this.entidadLog = entidadLog;
        this.idEntidad = idEntidad;
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id_log);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Log other = (Log) obj;
        if (!Objects.equals(this.id_log, other.id_log)) {
            return false;
        }
        return true;
    }

    public String fechaFormateada() {
        return Fecha.formatearFecha_ddMMyyyyHHmmss(fecha);
    }

    /**
     * @return the id_log
     */
    public Integer getId_log() {
        return id_log;
    }

    /**
     * @param id_log the id_log to set
     */
    public void setId_log(Integer id_log) {
        this.id_log = id_log;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
     * @return the idEntidad
     */
    public Integer getIdEntidad() {
        return idEntidad;
    }

    /**
     * @param idEntidad the idEntidad to set
     */
    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
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
