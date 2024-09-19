/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.malbino.orion.enums.Dia;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "clase")
public class Clase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_clase;

    @JoinColumn(name = "id_aula")
    @ManyToOne
    private Aula aula;
    
    @JoinColumn(name = "id_grupo")
    @ManyToOne
    private Grupo grupo;
    
    @JoinColumn(name = "id_periodo")
    @ManyToOne
    private Periodo periodo;
    
    private Dia dia;
    
    @Transient
    private String nombre;

    public Clase() {
    }

    public Clase(String nombre) {
        this.nombre = nombre;
    }

    public Clase(Periodo periodo, Dia dia) {
        this.periodo = periodo;
        this.dia = dia;
    }   

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.getId_clase());
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
        final Clase other = (Clase) obj;
        return Objects.equals(this.getId_clase(), other.getId_clase());
    }

    @Override
    public String toString() {
        return this.dia.toString() + " | " + this.periodo.toString() + " | " + this.grupo.toString();
    }

    /**
     * @return the id_clase
     */
    public Integer getId_clase() {
        return id_clase;
    }

    /**
     * @param id_clase the id_clase to set
     */
    public void setId_clase(Integer id_clase) {
        this.id_clase = id_clase;
    }

    /**
     * @return the aula
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * @param aula the aula to set
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    /**
     * @return the grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
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
    
    
}
