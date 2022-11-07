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
@Table(name = "indicadorpasantia", uniqueConstraints = @UniqueConstraint(columnNames = {"indicador", "id_notapasantia"}))
public class IndicadorPasantia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_indicadorpasantia;

    private Indicador indicador;
    private Evaluacion evaluacion;

    @JoinColumn(name = "id_notapasantia")
    @ManyToOne
    private NotaPasantia notaPasantia;

    public IndicadorPasantia() {
    }

    public IndicadorPasantia(Indicador indicador, Evaluacion evaluacion, NotaPasantia pasantia) {
        this.indicador = indicador;
        this.evaluacion = evaluacion;
        this.notaPasantia = pasantia;
    }

    /**
     * @return the id_indicadorpasantia
     */
    public Integer getId_indicadorpasantia() {
        return id_indicadorpasantia;
    }

    /**
     * @param id_indicadorpasantia the id_indicadorpasantia to set
     */
    public void setId_indicadorpasantia(Integer id_indicadorpasantia) {
        this.id_indicadorpasantia = id_indicadorpasantia;
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
     * @return the notaPasantia
     */
    public NotaPasantia getNotaPasantia() {
        return notaPasantia;
    }

    /**
     * @param notaPasantia the notaPasantia to set
     */
    public void setNotaPasantia(NotaPasantia notaPasantia) {
        this.notaPasantia = notaPasantia;
    }

}
