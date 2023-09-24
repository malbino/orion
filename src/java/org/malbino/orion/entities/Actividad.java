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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "actividad")
public class Actividad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_actividad;

    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;
    private String nombre;
    private String descripcion;
    private Funcionalidad funcionalidad;

    @JoinColumn(name = "id_gestionAcademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    public Actividad() {
    }

    /**
     * @return the id_actividad
     */
    public Integer getId_actividad() {
        return id_actividad;
    }

    /**
     * @param id_actividad the id_actividad to set
     */
    public void setId_actividad(Integer id_actividad) {
        this.id_actividad = id_actividad;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fin
     */
    public Date getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(Date fin) {
        this.fin = fin;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * @return the funcionalidad
     */
    public Funcionalidad getFuncionalidad() {
        return funcionalidad;
    }

    /**
     * @param funcionalidad the funcionalidad to set
     */
    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    /**
     * @return the gestionAcademica
     */
    public GestionAcademica getGestionAcademica() {
        return gestionAcademica;
    }

    /**
     * @param gestionAcademica the gestionAcademica to set
     */
    public void setGestionAcademica(GestionAcademica gestionAcademica) {
        this.gestionAcademica = gestionAcademica;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id_actividad);
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
        final Actividad other = (Actividad) obj;
        if (!Objects.equals(this.id_actividad, other.id_actividad)) {
            return false;
        }
        return true;
    }

    public String inicio_ddMMyyyyHHmm() {
        return Fecha.formatearFecha_ddMMyyyyHHmm(inicio);
    }

    public String fin_ddMMyyyyHHmm() {
        return Fecha.formatearFecha_ddMMyyyyHHmm(fin);
    }

    public String inicio_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(inicio);
    }

    public String fin_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fin);
    }
}
