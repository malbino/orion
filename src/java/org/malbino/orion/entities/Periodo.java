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
import javax.persistence.UniqueConstraint;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "periodo", uniqueConstraints = @UniqueConstraint(columnNames = {"dia", "inicio", "fin"}))
public class Periodo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_periodo;

    private Date inicio;
    private Date fin;

    @JoinColumn(name = "id_instituto")
    @ManyToOne
    private Instituto instituto;

    public Periodo() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id_periodo);
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
        final Periodo other = (Periodo) obj;
        return Objects.equals(this.id_periodo, other.id_periodo);
    }

    @Override
    public String toString() {
        return Fecha.formatearFecha_HHmm(inicio) + " - " + Fecha.formatearFecha_HHmm(fin);
    }

    public String toHtml() {
        return "<br/>" + Fecha.formatearFecha_HHmm(inicio) + "<br/>" + Fecha.formatearFecha_HHmm(fin) + "<br/><br/>";
    }

    public String inicio_HHmm() {
        return Fecha.formatearFecha_HHmm(inicio);
    }

    public String fin_HHmm() {
        return Fecha.formatearFecha_HHmm(fin);
    }

    public long minutos() {
        return Fecha.minutos(inicio, fin);
    }

    /**
     * @return the id_periodo
     */
    public Integer getId_periodo() {
        return id_periodo;
    }

    /**
     * @param id_periodo the id_periodo to set
     */
    public void setId_periodo(Integer id_periodo) {
        this.id_periodo = id_periodo;
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
     * @return the instituto
     */
    public Instituto getInstituto() {
        return instituto;
    }

    /**
     * @param instituto the instituto to set
     */
    public void setInstituto(Instituto instituto) {
        this.instituto = instituto;
    }

}
