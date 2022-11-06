/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Evaluacion;
import org.malbino.orion.enums.Indicador;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "notapasantia", uniqueConstraints = @UniqueConstraint(columnNames = {"indicador", "id_pasantia"}))
public class NotaPasantia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_notapasantia;

    private Indicador indicador;
    private Evaluacion evaluacion;

    @JoinColumn(name = "id_pasantia")
    @ManyToOne
    private Pasantia pasantia;

    public NotaPasantia() {
    }

    public NotaPasantia(Indicador indicador, Evaluacion evaluacion, Pasantia pasantia) {
        this.indicador = indicador;
        this.evaluacion = evaluacion;
        this.pasantia = pasantia;
    }

    /**
     * @return the id_notapasantia
     */
    public Integer getId_notapasantia() {
        return id_notapasantia;
    }

    /**
     * @param id_notapasantia the id_notapasantia to set
     */
    public void setId_notapasantia(Integer id_notapasantia) {
        this.id_notapasantia = id_notapasantia;
    }

    /**
     * @return the indicador
     */
    public Indicador getIndicador() {
        return indicador;
    }

    /**
     * @param indicador the indicador to set
     */
    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    /**
     * @return the evaluacion
     */
    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    /**
     * @return the pasantia
     */
    public Pasantia getPasantia() {
        return pasantia;
    }

    /**
     * @param pasantia the pasantia to set
     */
    public void setPasantia(Pasantia pasantia) {
        this.pasantia = pasantia;
    }
    
    
}
