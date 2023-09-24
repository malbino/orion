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
import org.malbino.orion.enums.ModalidadEvaluacion;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "gestionacademica", uniqueConstraints = @UniqueConstraint(columnNames = {"gestion", "periodo", "regimen"}))
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
    private ModalidadEvaluacion modalidadEvaluacion;

    public GestionAcademica() {
    }

    public String idnumberMoodle() {
        return "ga" + this.id_gestionacademica;
    }

    public String nameMoodle() {
        return periodo.getPeriodoRomano() + gestion + " " + regimen.getNombre();
    }

    public String fullNameMoodle() {
        return nameMoodle();
    }

    public String shortNameMoodle() {
        return toString();
    }
    
    public long inicioMoodle() {
        return inicio.getTime() / 1000;
    }

    public long finMoodle() {
        return fin.getTime() / 1000;
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
     * @return the codigo
     */
    public Integer getGestion() {
        return gestion;
    }

    /**
     * @param gestion the codigo to set
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

    public String inicio_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(inicio);
    }

    public String fin_ddMMyyyy() {
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

    public String codigo() {
        String s = "";
        if (this.getRegimen() == Regimen.SEMESTRAL) {
            s = periodo.getPeriodoRomano() + "/" + gestion;
        } else if (this.getRegimen() == Regimen.ANUAL) {
            s = gestion + " ";
        }
        return s;
    }

    /**
     * @return the modalidadEvaluacion
     */
    public ModalidadEvaluacion getModalidadEvaluacion() {
        return modalidadEvaluacion;
    }

    /**
     * @param modalidadEvaluacion the modalidadEvaluacion to set
     */
    public void setModalidadEvaluacion(ModalidadEvaluacion modalidadEvaluacion) {
        this.modalidadEvaluacion = modalidadEvaluacion;
    }
}
