/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "materia", catalog = "orion", schema = "public")
public class Materia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_materia;

    @Column(unique = true)
    private String codigo;
    private String nombre;
    private Integer horasSemana;
    private Double numeroCreditos;
    private Boolean curricular;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    public Materia() {
    }

    /**
     * @return the id_materia
     */
    public Integer getId_materia() {
        return id_materia;
    }

    /**
     * @param id_materia the id_materia to set
     */
    public void setId_materia(Integer id_materia) {
        this.id_materia = id_materia;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
     * @return the horasSemana
     */
    public Integer getHorasSemana() {
        return horasSemana;
    }

    /**
     * @param horasSemana the horasSemana to set
     */
    public void setHorasSemana(Integer horasSemana) {
        this.horasSemana = horasSemana;
    }

    /**
     * @return the numeroCreditos
     */
    public Double getNumeroCreditos() {
        return numeroCreditos;
    }

    /**
     * @param numeroCreditos the numeroCreditos to set
     */
    public void setNumeroCreditos(Double numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    /**
     * @return the curricular
     */
    public Boolean getCurricular() {
        return curricular;
    }

    /**
     * @param curricular the curricular to set
     */
    public void setCurricular(Boolean curricular) {
        this.curricular = curricular;
    }

    /**
     * @return the carrera
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id_materia);
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
        final Materia other = (Materia) obj;
        if (!Objects.equals(this.id_materia, other.id_materia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
    }

}
