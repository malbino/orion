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
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "gestionacademica", catalog = "orion", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"gestion", "periodo", "regimen"}))
public class GestionAcademica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_gestionacademica;

    private Integer gestion;
    private Periodo periodo;
    private Regimen regimen;
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Temporal(TemporalType.DATE)
    private Date fin;
    private Boolean vigente;

    public GestionAcademica() {
    }

    /**
     * @return the id_gestionacademica
     */
    public Integer getId_gestionacademica() {
        return id_gestionacademica;
    }

    /**
     * @param id_gestionacademica the id_gestionacademica to set
     */
    public void setId_gestionacademica(Integer id_gestionacademica) {
        this.id_gestionacademica = id_gestionacademica;
    }

    /**
     * @return the gestion
     */
    public Integer getGestion() {
        return gestion;
    }

    /**
     * @param gestion the gestion to set
     */
    public void setGestion(Integer gestion) {
        this.gestion = gestion;
    }

    /**
     * @return the periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the regimen
     */
    public Regimen getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
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
     * @return the vigente
     */
    public Boolean getVigente() {
        return vigente;
    }

    /**
     * @param vigente the vigente to set
     */
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public String inicioToString() {
        return Fecha.formatearFecha_ddMMyyyy(inicio);
    }

    public String finToString() {
        return Fecha.formatearFecha_ddMMyyyy(fin);
    }

    public String vigenteToString() {
        return vigente ? "Sí" : "No";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id_gestionacademica);
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
        final GestionAcademica other = (GestionAcademica) obj;
        if (!Objects.equals(this.id_gestionacademica, other.id_gestionacademica)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return periodo.getPeriodoRomano() + gestion + regimen.getInicial();
    }
}
