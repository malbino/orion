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
import org.malbino.orion.enums.Dia;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "periodo")
public class Periodo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_periodo;

    private Dia dia;
    private Date desde;
    private Date hasta;

    public Periodo() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.getId_periodo());
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
        return Objects.equals(this.getId_periodo(), other.getId_periodo());
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
     * @return the dia
     */
    public Dia getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(Dia dia) {
        this.dia = dia;
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

}
